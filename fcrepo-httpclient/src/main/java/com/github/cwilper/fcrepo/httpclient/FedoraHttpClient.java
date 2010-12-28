package com.github.cwilper.fcrepo.httpclient;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;

import java.net.URI;

public class FedoraHttpClient extends MultiThreadedHttpClient {

    private final URI baseURI;

    public FedoraHttpClient(HttpClientConfig config,
                            URI baseURI) {
        super(config);
        if (!baseURI.getScheme().equals("http")
                && !baseURI.getScheme().equals("https")) {
            throw new IllegalArgumentException();
        }
        String base = baseURI.toString();
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        this.baseURI = baseURI;
    }
    
    public FedoraHttpClient(HttpClientConfig config,
                            URI baseURI,
                            String username,
                            String password) {
        this(config, baseURI);
        int port = baseURI.getPort();
        if (port <= 0) {
            if (baseURI.getScheme().equals("http")) {
                port = 80;
            } else {
                port = 443;
            }
        }
        getCredentialsProvider().setCredentials(
                new AuthScope(this.baseURI.getHost(), port),
                new UsernamePasswordCredentials(username, password));
    }

    public URI getBaseURI() {
        return baseURI;
    }

}
