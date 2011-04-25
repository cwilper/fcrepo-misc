package com.github.cwilper.fcrepo.cloudsync.service.rest;

import com.github.cwilper.fcrepo.cloudsync.api.CloudSyncService;

import javax.ws.rs.core.MediaType;
import java.net.URI;

abstract class AbstractResource {

    protected static final String XML = MediaType.APPLICATION_XML;
    protected static final String JSON = MediaType.APPLICATION_JSON;
    protected static final String TEXT = MediaType.TEXT_PLAIN;

    protected static final String STATUS_200_OK = "Status: 200 OK";
    protected static final String STATUS_201_CREATED = "Status: 201 Created";
    protected static final String STATUS_204_NO_CONTENT = "Status: 204 No Content";

    protected final CloudSyncService service;

    AbstractResource(CloudSyncService service) {
        this.service = service;
    }

    URI getResourceURI(URI baseURI, String id) {
        String s = baseURI.toString();
        if (s.endsWith("/")) {
            return URI.create(s + id);
        } else {
            return URI.create(s + "/" + id);
        }
    }
}
