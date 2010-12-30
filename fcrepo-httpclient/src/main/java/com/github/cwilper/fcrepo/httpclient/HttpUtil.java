package com.github.cwilper.fcrepo.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public abstract class HttpUtil {

    // NOTE: the sink is not auto-closed
    public static HttpEntity get(HttpClient httpClient,
                                 URI requestURI,
                                 OutputStream sink) throws IOException {
        HttpGet httpGet = new HttpGet(requestURI);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            entity.writeTo(sink);
        }
        return entity;
    }
}
