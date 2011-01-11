package com.github.cwilper.fcrepo.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * Utility methods for working with HTTPClients.
 */
public abstract class HttpUtil {

    // NOTE: the sink is not auto-closed
    public static HttpEntity get(HttpClient httpClient,
                                 URI requestURI,
                                 OutputStream sink) throws IOException {
        HttpEntity entity = doGet(httpClient, requestURI);
        if (entity != null) {
            entity.writeTo(sink);
        }
        return entity;
    }

    // NOTE: the input stream MUST be closed by the caller!
    public static InputStream get(HttpClient httpClient,
                                  URI requestURI) throws IOException {
        HttpEntity entity = doGet(httpClient, requestURI);
        if (entity == null) {
            return new ByteArrayInputStream(new byte[0]);
        }
        return entity.getContent();
    }

    private static HttpEntity doGet(HttpClient httpClient, URI requestURI)
            throws IOException {
        HttpGet httpGet = new HttpGet(requestURI);
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("Error GETting " 
                    + requestURI + " -- " + response.getStatusLine());
        }
        return response.getEntity();
    }
}
