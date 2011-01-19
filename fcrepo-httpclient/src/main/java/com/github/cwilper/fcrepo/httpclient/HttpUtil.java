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
 * Utility methods for working with <code>HTTPClient</code>s.
 */
public final class HttpUtil {

    private HttpUtil() { }

    /**
     * Performs an HTTP GET using the given client, sending the response body
     * to the given stream (which won't be auto-closed).
     *
     * @param httpClient the client to use.
     * @param requestURI the http or https url.
     * @param sink the stream to send the content to (it won't be auto-closed)
     * @return the resulting entity
     * @throws IOException if the request fails for any reason, including
     *         a non-200 status code.
     */
    public static HttpEntity get(HttpClient httpClient,
                                 URI requestURI,
                                 OutputStream sink) throws IOException {
        HttpEntity entity = doGet(httpClient, requestURI);
        if (entity != null) {
            entity.writeTo(sink);
        }
        return entity;
    }

    /**
     * Performs an HTTP GET using the given client, returning an input stream
     * over the response body. The caller MUST close the stream when finished.
     *
     * @param httpClient the client to use.
     * @param requestURI the http or https url.
     * @return an input stream over the response body.
     * @throws IOException if the request fails for any reason, including
     *         a non-200 status code.
     */
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
