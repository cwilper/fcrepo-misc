package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public abstract class StoreConnector {

    private static final Logger logger = LoggerFactory.getLogger(StoreConnector.class);

    public static final StoreConnector getInstance(ObjectStore store) {
        if (store.getType().equals("fedora")) {
            return new FedoraConnector(store);
        } else if (store.getType().equals("duracloud")) {
            return new DuraCloudConnector(store);
        } else {
            throw new IllegalArgumentException("Unrecognized ObjectStore type: " + store.getType());
        }
    }

    protected void listObjects(Iterator<String> pidIterator,
                               ObjectListHandler handler) {
        boolean keepGoing = true;
        while (pidIterator.hasNext() && keepGoing) {
            String pid = pidIterator.next();
            if (hasObject(pid)) {
                ObjectInfo o = new ObjectInfo();
                o.setPid(pid);
                keepGoing = handler.handleObject(o);
            }
        }
    }

    protected static boolean headCheck(HttpClient httpClient, String url) {
        logger.debug("Doing HEAD request on " + url);
        HttpHead head = new HttpHead(url);
        try {
            HttpResponse response = httpClient.execute(head);
            int responseCode = response.getStatusLine().getStatusCode();
            logger.debug("responseCode: " + responseCode);
            return responseCode == 200;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String get(HttpClient httpClient, String url) {
        logger.debug("Doing GET request on " + url);
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(get);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != 200) {
                throw new RuntimeException("Unexpected response code (" + responseCode + ") getting " + url);
            }
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void listObjects(ObjectQuery query,
                                     ObjectListHandler handler);

    protected abstract boolean hasObject(String pid);

    public abstract FedoraObject getObject(String pid);

    public abstract InputStream getContent(FedoraObject o,
                                           Datastream ds,
                                           DatastreamVersion dsv);

    public abstract void close();

}
