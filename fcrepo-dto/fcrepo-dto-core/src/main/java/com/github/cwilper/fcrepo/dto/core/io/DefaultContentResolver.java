package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.httpclient.HttpUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class DefaultContentResolver implements ContentResolver {

    private final HttpClient defaultHttpClient = new DefaultHttpClient();
    
    private HttpClient httpClient;

    public DefaultContentResolver() {
        httpClient = defaultHttpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        if (httpClient != defaultHttpClient
                && this.httpClient == defaultHttpClient) {
            defaultHttpClient.getConnectionManager().shutdown();
        }
        this.httpClient = httpClient;
    }

    @Override
    @PreDestroy
    public void close() {
        if (this.httpClient == defaultHttpClient) {
            defaultHttpClient.getConnectionManager().shutdown();
        }
    }

    @Override
    public InputStream resolveContent(URI ref) throws IOException {
        String scheme = ref.getScheme();
        if (ref.getScheme().equals("file")) {
            return resolveFileContent(ref);
        } else if (scheme.equals("http") || scheme.equals("https")) {
            return resolveHttpContent(ref);
        } else {
            throw new IOException("Unsupported URI scheme: " + scheme + " -- "
                    + "cannot resolve " + ref);
        }
    }

    @Override
    public void resolveContent(URI ref, OutputStream sink) throws IOException {
        String scheme = ref.getScheme();
        if (ref.getScheme().equals("file")) {
            resolveFileContent(ref, sink);
        } else if (scheme.equals("http") || scheme.equals("https")) {
            resolveHttpContent(ref, sink);
        } else {
            throw new IOException("Unsupported URI scheme: " + scheme + " -- "
                    + "cannot resolve " + ref);
        }
    }

    private static InputStream resolveFileContent(URI ref) throws IOException {
        return new FileInputStream(new File(ref.getSchemeSpecificPart()));
    }

    private static void resolveFileContent(URI ref, OutputStream sink)
            throws IOException {
        InputStream source = resolveFileContent(ref);
        try {
            IOUtils.copy(source, sink);
        } finally {
            IOUtils.closeQuietly(source);
        }
    }

    private InputStream resolveHttpContent(URI ref) throws IOException {
        return HttpUtil.get(httpClient, ref);
    }

    private void resolveHttpContent(URI ref, OutputStream sink)
            throws IOException {
        HttpUtil.get(httpClient, ref, sink);
    }

}
