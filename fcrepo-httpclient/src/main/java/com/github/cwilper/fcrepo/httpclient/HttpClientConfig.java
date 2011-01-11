package com.github.cwilper.fcrepo.httpclient;

/**
 * Configuration for a <code>MultiThreadedHttpClient</code> with reasonable
 * defaults.
 */
public class HttpClientConfig {

    /** 30000 (30 seconds) */
    public static final int     DEFAULT_CONNECTION_TIMEOUT             = 30000;

    /** 30000 (30 seconds) */
    public static final int     DEFAULT_SOCKET_TIMEOUT                 = 30000;

    /** 10 */
    public static final int     DEFAULT_MAX_CONNECTIONS_PER_HOST       = 10;

    /** 100 */
    public static final int     DEFAULT_MAX_TOTAL_CONNECTIONS          = 100;

    /** false */
    public static final boolean DEFAULT_PREEMPTIVE_AUTHN               = false;

    /** false */
    public static final boolean DEFAULT_SKIP_SSL_TRUST_CHECK           = false;

    /** false */
    public static final boolean DEFAULT_SKIP_SSL_HOSTNAME_VERIFICATION = false;

    private int connectionTimeout;
    private int socketTimeout;
    private int maxConnectionsPerHost;
    private int maxTotalConnections;
    private boolean preemptiveAuthN;
    private boolean skipSSLTrustCheck;
    private boolean skipSSLHostnameVerification;

    /**
     * Constructs an <code>HttpClientConfig</code> with default values.
     */
    public HttpClientConfig() {
        connectionTimeout           = DEFAULT_CONNECTION_TIMEOUT;
        socketTimeout               = DEFAULT_SOCKET_TIMEOUT;
        maxConnectionsPerHost       = DEFAULT_MAX_CONNECTIONS_PER_HOST;
        maxTotalConnections         = DEFAULT_MAX_TOTAL_CONNECTIONS;
        preemptiveAuthN             = DEFAULT_PREEMPTIVE_AUTHN;
        skipSSLTrustCheck           = DEFAULT_SKIP_SSL_TRUST_CHECK;
        skipSSLHostnameVerification = DEFAULT_SKIP_SSL_HOSTNAME_VERIFICATION;
    }

    /**
     * Gets the maximum number of milliseconds to wait while attempting
     * to establish a connection
     *
     * @return number of milliseconds.
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets the maximum number of milliseconds to wait while attempting
     * to establish a connection
     *
     * @param connectionTimeout number of milliseconds.
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Gets the maximum number of milliseconds to wait for response data
     * to arrive while blocking on a read operation.
     *
     * @return number of milliseconds.
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * Sets the maximum number of milliseconds to wait for response data
     * to arrive while blocking on a read operation.
     *
     * @param socketTimeout number of milliseconds.
     */
    public void setSocketTimeout(int socketTimeout) {
        this.connectionTimeout = socketTimeout;
    }

    /**
     * Gets the maximum number of connections per host.
     *
     * @return number of connections.
     */
    public int getMaxConnectionsPerHost() {
        return maxConnectionsPerHost;
    }

    /**
     * Sets the maximum number of connections per host.
     *
     * @param maxConnectionsPerHost number of connections.
     */
    public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    /**
     * Gets the maximum number of total connections.
     *
     * @return number of connections.
     */
    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    /**
     * Sets the maximum number of total connections.
     *
     * @param maxTotalConnections number of connections.
     */
    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    /**
     * Gets whether preemptive authentication is enabled.
     *
     * @return true if enabled, false otherwise.
     */
    public boolean getPreemptiveAuthN() {
        return preemptiveAuthN;
    }

    /**
     * Sets whether preemptive authentication is enabled.
     *
     * @param preemptiveAuthN true to enable it, false otherwise.
     */
    public void setPreemptiveAuthN(boolean preemptiveAuthN) {
        this.preemptiveAuthN = preemptiveAuthN;
    }

    /**
     * Gets whether the chain of certificates should be checked when
     * establishing an SSL connection.
     *
     * @return true if checking should be skipped, false otherwise.
     */
    public boolean getSkipSSLTrustCheck() {
        return skipSSLTrustCheck;
    }

    /**
     * Sets whether the chain of certificates should be checked when
     * establishing an SSL connection.
     *
     * @param skipSSLTrustCheck true to skip, false otherwise.
     */
    public void setSkipSSLTrustCheck(boolean skipSSLTrustCheck) {
        this.skipSSLTrustCheck = skipSSLTrustCheck;
    }

    /**
     * Gets whether the hostname in the certificate should be checked to
     * match the one being connected to when establishing an SSL connection.
     *
     * @return true if checking should be skipped, false otherwise.
     */
    public boolean getSkipSSLHostnameVerification() {
        return skipSSLHostnameVerification;
    }

    /**
     * Sets whether the hostname in the certificate should be checked to
     * match the one being connected to when establishing an SSL connection.
     *
     * @param skipSSLHostnameVerification if checking should be skipped, false
     *        otherwise.
     */
    public void setSkipSSLHostnameVerification(
            boolean skipSSLHostnameVerification) {
        this.skipSSLHostnameVerification = skipSSLHostnameVerification;
    }

}
