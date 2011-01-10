/**
 * Provides a client for performing RDF queries against a Fedora repository.
 * <p>
 * <h2>Usage</h2>
 * The following example code lists all content model objects:
 * <pre>
 * String query = "SELECT ?object\n"
 *              + "WHERE { \n"
 *              + "  ?object\n"
 *              + "     &lt;info:fedora/fedora-system:def/model#hasModel&gt;\n"
 *              + "       &lt;info:fedora/fedora-system:ContentModel-3.0&gt;.";
 *
 * FedoraHttpClient httpClient = new FedoraHttpClient(new HttpClientConfig(),
 *         java.net.URI.create("http://localhost:8080/fedora"),
 *         "fedoraAdmin", "fedoraAdmin);
 * RIQueryResult result = null;
 * try {
 *     RIClient riClient = new RIClient(httpClient);
 *     result = riClient.sparql(sparqlQuery, false);
 *     while (result.hasNext()) {
 *         List&lt;Value&gt; tuple = result.next();
 *         org.openrdf.model.URI uri = (org.openrdf.model.URI) tuple.get(0);
 *         System.out.println("Content model found: " + uri.getURI());
 *     }
 * } finally {
 *     if (result != null) {
 *         result.close();
 *     }
 *     httpClient.close();
 * }
 * </pre>
 */
package com.github.cwilper.fcrepo.riclient;