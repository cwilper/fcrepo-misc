package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.httpclient.HttpUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.annotation.PreDestroy;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class DefaultContentResolver implements ContentResolver {

    private static final int FILE = 1;
    private static final int HTTP = 2;

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
    public InputStream resolveContent(URI base, URI ref) throws IOException {
        ref = getAbsolute(base, ref);
        switch (getSchemeType(ref)) {
            case FILE:
                return new FileInputStream(ref.getSchemeSpecificPart());
            case HTTP:
                return HttpUtil.get(httpClient, ref);
        }
        return null; // won't happen
    }

    @Override
    public void resolveContent(URI base, URI ref, OutputStream sink)
            throws IOException {
        ref = getAbsolute(base, ref);
        switch (getSchemeType(ref)) {
            case FILE:
                InputStream source = new FileInputStream(
                        ref.getSchemeSpecificPart());
                try {
                    IOUtils.copy(source, sink);
                } finally {
                    IOUtils.closeQuietly(source);
                }
                break;
            case HTTP:
                HttpUtil.get(httpClient, ref, sink);
                break;
        }
    }

    private static URI getAbsolute(URI base, URI ref) {
        if (base != null) {
            if (!base.isAbsolute()) {
                throw new IllegalArgumentException("Base URI must be absolute");
            }
            return base.resolve(ref);
        } else if (ref.isAbsolute()) {
            return ref;
        } else {
            throw new IllegalArgumentException("URI is not absolute and base "
                    + "URI not specified -- cannot resolve " + ref);
        }
    }

    private static int getSchemeType(URI ref) throws IOException {
        String scheme = ref.getScheme();
        if (scheme.equals("file")) {
            return FILE;
        } else if (scheme.equals("http") || scheme.equals("https")) {
            return HTTP;
        } else {
            throw new IOException("Unsupported URI scheme: " + scheme + " -- "
                    + "cannot resolve " + ref);
        }
    }

}
