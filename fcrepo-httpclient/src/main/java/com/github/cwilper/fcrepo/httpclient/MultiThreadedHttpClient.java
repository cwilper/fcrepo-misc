package com.github.cwilper.fcrepo.httpclient;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * A multi-threaded <code>HttpClient</code> that's easy to construct.
 */
public class MultiThreadedHttpClient extends DefaultHttpClient {

    private final HttpClientConfig config;

    public MultiThreadedHttpClient(HttpClientConfig config) {
        this.config = config;
        if (config.getPreemptiveAuthN()) {
            HttpRequestInterceptor interceptor = new HttpRequestInterceptor() {
                @Override
                public void process(HttpRequest request,
                                    HttpContext context)
                        throws HttpException, IOException {
                    AuthState authState = (AuthState) context.getAttribute(
                            ClientContext.TARGET_AUTH_STATE);
                    CredentialsProvider credsProvider = (CredentialsProvider)
                            context.getAttribute(ClientContext.CREDS_PROVIDER);
                    HttpHost targetHost = (HttpHost) context.getAttribute(
                            ExecutionContext.HTTP_TARGET_HOST);
                    // If no auth scheme has been initialized yet
                    if (authState.getAuthScheme() == null) {
                        AuthScope authScope = new AuthScope(
                                targetHost.getHostName(),
                                targetHost.getPort());
                        // Obtain credentials matching the target host
                        Credentials creds =
                                credsProvider.getCredentials(authScope);
                        // If found, generate BasicScheme preemptively
                        if (creds != null) {
                            authState.setAuthScheme(new BasicScheme());
                            authState.setCredentials(creds);
                        }
                    }
                }

            };
            addRequestInterceptor(interceptor, 0);
        }

    }

    @PreDestroy
    public void close() {
        getConnectionManager().shutdown();
    }

    @Override
    protected HttpParams createHttpParams() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params,
                config.getConnectionTimeout());
        HttpConnectionParams.setSoTimeout(params,
                config.getSocketTimeout());
        ConnManagerParams.setMaxConnectionsPerRoute(params,
                new ConnPerRouteBean(config.getMaxConnectionsPerHost()));
        ConnManagerParams.setMaxTotalConnections(
                params, config.getMaxTotalConnections());
        return params;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(
                "http",
                PlainSocketFactory.getSocketFactory(),
                80));
        schemeRegistry.register(new Scheme(
                "https",
                createSSLSocketFactory(
                        config.getSkipSSLTrustCheck(),
                        config.getSkipSSLHostnameVerification()),
                443));

        return new ThreadSafeClientConnManager(getParams(), schemeRegistry);
    }

    private static SSLSocketFactory createSSLSocketFactory(
            boolean skipSSLTrustCheck,
            boolean skipSSLHostnameVerification) {
        SSLContext sslContext = null;
        try {
            if (skipSSLTrustCheck) {
                sslContext = SSLContext.getInstance("TLS");
                TrustManager easyTrustManager = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(
                            X509Certificate[] chain,
                            String authType) throws CertificateException {
                        // Oh, I am easy!
                    }
                    @Override
                    public void checkServerTrusted(
                            X509Certificate[] chain,
                            String authType) throws CertificateException {
                        // Oh, I am easy!
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                sslContext.init(null, new TrustManager[] { easyTrustManager },
                        null);
            } else {
                sslContext = SSLContext.getDefault();
            }
        } catch (KeyManagementException wontHappen) {
            throw new RuntimeException(wontHappen);
        } catch (NoSuchAlgorithmException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
        SSLSocketFactory factory = new SSLSocketFactory(sslContext);
        if (skipSSLHostnameVerification) {
            factory.setHostnameVerifier(
                    SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }
        return factory;
    }

}
