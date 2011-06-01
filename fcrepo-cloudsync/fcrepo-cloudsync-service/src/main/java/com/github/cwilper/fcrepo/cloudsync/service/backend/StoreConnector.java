package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.cloudsync.api.ObjectInfo;
import com.github.cwilper.fcrepo.cloudsync.api.ObjectStore;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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

    // returns null if 404
    protected static String getString(HttpClient httpClient, String url) {
        try {
            HttpEntity entity = get(httpClient, url);
            if (entity == null) return null;
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // returns null if 404
    protected static InputStream getStream(HttpClient httpClient, String url) {
        try {
            HttpEntity entity = get(httpClient, url);
            if (entity == null) return null;
            return entity.getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // returns null if 404
    protected static HttpEntity get(HttpClient httpClient, String url) throws IOException {
        logger.debug("Doing GET request on " + url);
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(get);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 404) {
                return null;
            } else if (responseCode != 200) {
                throw new RuntimeException("Unexpected response code (" + responseCode + ") getting " + url);
            }
            return response.getEntity();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void delete(HttpClient httpClient, String url) {
        logger.debug("Doing DELETE request on " + url);
        HttpDelete delete = new HttpDelete(url);
        HttpEntity entity = null;
        try {
            HttpResponse response = httpClient.execute(delete);
            entity = response.getEntity();
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != 200 && responseCode != 204) {
                throw new RuntimeException("Unexpected response code (" + responseCode + ") deleting " + url);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (Exception e) {
                }
            }
        }
    }

    protected static void post(HttpClient httpClient, String url, File file, String mimeType) {
        logger.debug("Doing POST request on " + url);
        HttpPost post = new HttpPost(url);
        HttpEntity entity = null;
        try {
            post.setHeader("Content-type", mimeType);
            post.setEntity(new FileEntity(file, mimeType));
            HttpResponse response = httpClient.execute(post);
            entity = response.getEntity();
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != 200 && responseCode != 201 && responseCode != 204) {
                throw new RuntimeException("Unexpected response code (" + responseCode + ") posting " + url);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (Exception e) {
                }
            }
        }
    }

    protected static void put(HttpClient httpClient, String url, File file, String mimeType) {
        logger.debug("Doing PUT request on " + url);
        HttpPut put = new HttpPut(url);
        HttpEntity entity = null;
        try {
            put.setEntity(new FileEntity(file, mimeType));
            HttpResponse response = httpClient.execute(put);
            entity = response.getEntity();
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != 200 && responseCode != 201 && responseCode != 204) {
                throw new RuntimeException("Unexpected response code (" + responseCode + ") putting " + url);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (Exception e) {
                }
            }
        }
    }

    public abstract void listObjects(ObjectQuery query,
                                     ObjectListHandler handler);

    protected abstract boolean hasObject(String pid);

    // return null if object doesn't exist
    public abstract FedoraObject getObject(String pid);

    // true if the object previously existed
    public abstract boolean putObject(FedoraObject o, boolean overwrite);

    public abstract InputStream getContent(FedoraObject o,
                                           Datastream ds,
                                           DatastreamVersion dsv);

    public abstract void close();

}
