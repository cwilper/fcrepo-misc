package com.github.cwilper.fcrepo.httpclient;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;

import java.net.URI;

/**
 * A <code>MultiThreadedHttpClient</code> that knows where a Fedora
 * respository is located and is configured to authenticate against
 * it if needed.
 */
public class FedoraHttpClient extends MultiThreadedHttpClient {

    private final URI baseURI;

    /**
     * Creates an instance without Fedora credentials.
     *
     * @param config the configuration to use.
     * @param baseURI the location of the Fedora repository. Trailing
     *                slash characters will be dropped.
     */
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

    /**
     * Creates an instance with Fedora credentials.
     *
     * @param config the configuration to use.
     * @param baseURI the location of the Fedora repository. Trailing slash
     *                characters will be dropped.
     * @param username the username to use when challenged at baseURI.
     * @param password the password to use when challenged at baseURI.
     */
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

    /**
     * Gets the location of the Fedora repository this instance knows about.
     *
     * @return the base URI, which is guaranteed not to end with a slash.
     */
    public URI getBaseURI() {
        return baseURI;
    }

}
