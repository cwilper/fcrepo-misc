package com.github.cwilper.fcrepo.riclient;

import com.github.cwilper.fcrepo.httpclient.FedoraHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Fedora Resource Index Client.
 */
public class RIClient {

    private final String riEndpoint;
    private final FedoraHttpClient httpClient;

    /**
     * Creates an instance.
     *
     * @param httpClient The FedoraHTTPClient to use when making requests.
     */
    public RIClient(FedoraHttpClient httpClient) {
        this.httpClient = httpClient;
        this.riEndpoint = httpClient.getBaseURI() + "/risearch";
    }

    /**
     * Performs an iTQL query.
     *
     * @param query the query text.
     * @param flush whether the Fedora server should flush its triple buffer
     *        first. If false, the result could be out of date with what was
     *        actually stored in the repository at the time of the request.
     *        If true, it may take considerably longer to get a response.
     * @return the resulting tuples.
     */
    public RIQueryResult itql(String query, boolean flush) {
        return new RIQueryResult(executeQuery(query, "itql", flush));
    }

    /**
     * Performs a SPARQL query.
     *
     * @param query the query text.
     * @param flush whether the Fedora server should flush its triple buffer
     *        first. If false, the result could be out of date with what was
     *        actually stored in the repository at the time of the request.
     *        If true, it may take considerably longer to get a response.
     * @return the resulting tuples.
     */
    public RIQueryResult sparql(String query, boolean flush) {
        return new RIQueryResult(executeQuery(query, "sparql", flush));
    }

    private InputStream executeQuery(String query,
                                     String lang,
                                     boolean flush) {
        HttpPost post = new HttpPost(riEndpoint);
        List<NameValuePair> args = new ArrayList<NameValuePair>();
        if (flush) {
            args.add(new BasicNameValuePair("flush", "true"));
        }
        args.add(new BasicNameValuePair("type", "tuples"));
        args.add(new BasicNameValuePair("lang", lang));
        args.add(new BasicNameValuePair("format", "Simple"));
        args.add(new BasicNameValuePair("stream", "on"));
        args.add(new BasicNameValuePair("query", query));
        try {
            post.setEntity(new UrlEncodedFormEntity(args, HTTP.UTF_8));
        } catch (UnsupportedEncodingException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
        try {
            HttpResponse response = httpClient.execute(post);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != 200) {
                throw new RuntimeException("Fedora Resource Index query "
                        + "returned an unexpected HTTP response code: "
                        + responseCode + ". Consult Fedora Server log for "
                        + "details.");
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return new ByteArrayInputStream(new byte[0]);
            } else {
                return entity.getContent();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
