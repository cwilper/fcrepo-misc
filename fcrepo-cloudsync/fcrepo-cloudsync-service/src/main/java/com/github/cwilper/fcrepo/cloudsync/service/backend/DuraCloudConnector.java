package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.cloudsync.service.util.JSON;
import com.github.cwilper.fcrepo.cloudsync.service.util.StringUtil;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import com.github.cwilper.fcrepo.dto.foxml.FOXMLReader;
import com.github.cwilper.fcrepo.dto.foxml.FOXMLWriter;
import com.github.cwilper.fcrepo.httpclient.HttpClientConfig;
import com.github.cwilper.fcrepo.httpclient.MultiThreadedHttpClient;
import com.github.cwilper.ttff.Filter;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DuraCloudConnector extends StoreConnector {

    private static final int CHUNKSIZE = 999;

    private final URI spaceURI;
    private final String providerId;
    private final String prefix;

    private final MultiThreadedHttpClient httpClient;

    public DuraCloudConnector(ObjectStore store) {
        Map<String, String> map = JSON.getMap(JSON.parse(store.getData()));
        providerId = StringUtil.validate("providerId", map.get("providerId"));
        prefix = StringUtil.normalize(map.get("prefix"));

        // Determine base URI of space and init httpClient
        String duraStoreUrl = StringUtil.validate("url", map.get("url"));
        while (duraStoreUrl.endsWith("/")) {
            duraStoreUrl = duraStoreUrl.substring(0, duraStoreUrl.length() - 1);
        }

        String space = StringUtil.validate("space", map.get("space"));

        spaceURI = URI.create(duraStoreUrl + "/" + space);
        int port = spaceURI.getPort();
        if (port <= 0) {
            if (spaceURI.getScheme().equals("http")) {
                port = 80;
            } else {
                port = 443;
            }
        }
        httpClient = new MultiThreadedHttpClient(new HttpClientConfig());
        String username = StringUtil.validate("username", map.get("username"));
        String password = StringUtil.validate("password", map.get("password"));
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(spaceURI.getHost(), port),
                new UsernamePasswordCredentials(username, password));
    }

    @Override
    public void listObjects(ObjectQuery query, ObjectListHandler handler) {
        String type = query.getType();
        if (type.equals("pidPattern")) {
            listObjects(new PIDPatternFilter(query.getPidPattern()), handler);
        } else if (type.equals("pidList")) {
            listObjects(query.getPidList().iterator(), handler);
        } else {
            throw new UnsupportedOperationException("DuraCloud does not support " + query.getQueryType() + " queries.");
        }
    }

    @Override
    protected boolean hasObject(String pid) {
        return headCheck(httpClient, getContentURI(pid));
    }

    @Override
    public FedoraObject getObject(String pid) {
        InputStream in = getStream(httpClient, getContentURI(pid));
        if (in == null) return null;
        try {
            return new FOXMLReader().readObject(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean putObject(FedoraObject o, boolean overwrite) {
        boolean existed = hasObject(o.pid());
        if (existed) {
            if (!overwrite) {
                return existed;
            }
        }
        FOXMLWriter writer = new FOXMLWriter();
        File tempFile = null;
        OutputStream out = null;
        try {
            // write to temp file
            tempFile = File.createTempFile("cloudsync", null);
            out = new FileOutputStream(tempFile);
            writer.writeObject(o, out);
            out.close();
            // put it
            put(httpClient, getContentURI(o.pid()), tempFile, "application/xml");
            return existed;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(out);
            if (tempFile != null) {
                tempFile.delete();
            }
            writer.close();
        }
    }

    private String getContentURI(String contentId) {
        StringBuilder s = new StringBuilder();
        s.append(spaceURI.toString());
        s.append('/');
        if (prefix != null) {
            s.append(prefix);
        }
        s.append(contentId);
        s.append("?storeID=");
        s.append(providerId);
        return s.toString();
    }

    @Override
    public InputStream getContent(FedoraObject o, Datastream ds, DatastreamVersion dsv) {
        // https://demo.duracloud.org/durastore/cwilper-test/content-id?storeID=0
        // note: if content-id has slashes, they should not be URL-encoded
        return null;
    }

    @Override
    public void close() {
        httpClient.close();
    }

    private void listObjects(Filter<String> filter, ObjectListHandler handler) {
        boolean keepGoing = true;
        boolean moreChunks = true;
        String marker = null;
        while (moreChunks && keepGoing) {
            String lastItemId = null;
            int chunkSize = 0;
            for (String itemId: getNextChunk(marker, CHUNKSIZE)) {
                String pid;
                if (prefix == null) {
                    pid = itemId;
                } else {
                    pid = itemId.substring(prefix.length());
                }
                try {
                    if (filter.accept(pid) != null) {
                        ObjectInfo o = new ObjectInfo();
                        o.setPid(pid);
                        keepGoing = handler.handleObject(o);
                    }
                } catch (IOException wontHappen) {
                    throw new RuntimeException(wontHappen);
                }
                lastItemId = itemId;
                chunkSize++;
            }
            if (chunkSize == CHUNKSIZE) {
                marker = lastItemId;
            } else {
                moreChunks = false;
            }
        }
    }

    private List<String> getNextChunk(String marker, int maxResults) {
        String url = spaceURI.toString() + "?storeID=" + providerId
                + "&maxResults=" + maxResults;
        if (marker != null) {
            url += "&marker=" + marker;
        }
        if (prefix != null) {
            url += "&prefix=" + prefix;
        }
        List<String> list = new ArrayList<String>();
        try {
            Document doc = parseXML(getString(httpClient, url));
            Node root = doc.getDocumentElement();
            NodeList itemNodes = root.getChildNodes();
            for (int i = 0; i < itemNodes.getLength(); i++) {
                Node itemNode = itemNodes.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    list.add(itemNode.getTextContent().trim());
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Document parseXML(String xmlString)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        InputSource inStream = new InputSource();
        inStream.setCharacterStream(new StringReader(xmlString));
        return db.parse(inStream);
    }

}
