package com.github.cwilper.fcrepo.dto;

import com.github.cwilper.fcrepo.httpclient.HttpUtil;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;
import java.util.Set;

class FOXMLWriter {

    private static final int BASE64_LINE_LENGTH = 74;
    private static final String CHAR_ENCODING = "UTF-8";
    private static final String LINE_FEED = "\n";

    private final FedoraObject obj;
    private final OutputStream sink;
    private final Set<String> managedDatastreamsToEmbed;
    private final HttpClient httpClient;

    public FOXMLWriter(FedoraObject obj,
                       OutputStream sink,
                       Set<String> managedDatastreamsToEmbed,
                       HttpClient httpClient) {
        this.obj = obj;
        this.sink = sink;
        this.managedDatastreamsToEmbed = managedDatastreamsToEmbed;
        this.httpClient = httpClient;
    }

    public void writeObject() throws IOException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            writeDigitalObject(factory.createXMLStreamWriter(sink,
                    CHAR_ENCODING));
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    private void writeDigitalObject(XMLStreamWriter w)
            throws IOException, XMLStreamException {
        w.writeStartDocument();
        w.setDefaultNamespace(FOXML.xmlns);

        w.writeStartElement(FOXML.digitalObject);
        w.writeAttribute(FOXML.VERSION, FOXML.SUPPORTED_VERSION);
        writeAttribute(w, FOXML.PID, obj.pid());

        writeObjectProperties(w, obj);
        for (String id: obj.datastreams().keySet()) {
            writeDatastream(w, obj.datastreams().get(id));
        }
        w.writeEndDocument();
    }

    private void writeObjectProperties(XMLStreamWriter w,
            FedoraObject obj) throws XMLStreamException {
        w.writeStartElement(FOXML.objectProperties);
        writeProperty(w, FOXML.STATE_URI, obj.state());
        writeProperty(w, FOXML.LABEL_URI, obj.label());
        writeProperty(w, FOXML.OWNERID_URI, obj.ownerId());
        writeProperty(w, FOXML.CREATEDDATE_URI, obj.createdDate());
        writeProperty(w, FOXML.LASTMODIFIEDDATE_URI, obj.lastModifiedDate());
        w.writeEndElement();
    }

    private void writeDatastream(XMLStreamWriter w, Datastream ds)
            throws IOException, XMLStreamException {
        w.writeStartElement(FOXML.datastream);
        writeAttribute(w, FOXML.ID, ds.id());
        writeAttribute(w, FOXML.STATE, ds.state());
        writeAttribute(w, FOXML.CONTROL_GROUP, ds.controlGroup());
        writeAttribute(w, FOXML.VERSIONABLE, ds.versionable());
        for (DatastreamVersion dsv: ds.versions()) {
            writeDatastreamVersion(w, ds, dsv);
        }
        w.writeEndElement();
    }

    private void writeDatastreamVersion(XMLStreamWriter w, Datastream ds,
            DatastreamVersion dsv) throws IOException, XMLStreamException {
        w.writeStartElement(FOXML.datastreamVersion);
        writeAttribute(w, FOXML.ID, dsv.id());
        writeAttribute(w, FOXML.ALT_IDS, dsv.altIds().toArray());
        writeAttribute(w, FOXML.LABEL, dsv.label());
        writeAttribute(w, FOXML.CREATED, dsv.createdDate());
        writeAttribute(w, FOXML.MIMETYPE, dsv.mimeType());
        writeAttribute(w, FOXML.FORMAT_URI, dsv.formatURI());
        writeAttribute(w, FOXML.SIZE, dsv.size());
        writeContentDigest(w, dsv.contentDigest());
        if (ds.controlGroup() == ControlGroup.INLINE_XML) {
            writeXMLContent(w, dsv.xmlContent());
        } else if (ds.controlGroup() == ControlGroup.MANAGED_CONTENT
                && managedDatastreamsToEmbed.contains(ds.id())) {
            writeBinaryContent(w, dsv.contentLocation());
        } else {
            writeContentLocation(w, dsv.contentLocation());
        }
        w.writeEndElement();
    }

    private void writeContentLocation(XMLStreamWriter w, URI ref)
            throws XMLStreamException {
        if (ref != null) {
            w.writeStartElement(FOXML.contentLocation);
            w.writeAttribute(FOXML.TYPE, FOXML.URL_TYPE);
            w.writeAttribute(FOXML.REF, ref.toString());
            w.writeEndElement();
        }
    }

    private void writeBinaryContent(XMLStreamWriter w, URI ref)
            throws IOException, XMLStreamException {
        if (ref != null) {
            w.writeStartElement(FOXML.binaryContent);
            w.writeCharacters(LINE_FEED);
            w.flush();
            Base64OutputStream out = new Base64OutputStream(sink,
                    true, BASE64_LINE_LENGTH,
                    LINE_FEED.getBytes(CHAR_ENCODING));
            HttpUtil.get(httpClient, ref, out);
            out.flush();
            w.writeCharacters(LINE_FEED);
            w.writeEndElement();
        }
    }

    private void writeXMLContent(XMLStreamWriter w, InputStream xmlContent)
            throws IOException, XMLStreamException {
        if (xmlContent != null) {
            w.writeStartElement(FOXML.xmlContent);
            w.flush();
            try {
                IOUtils.copy(xmlContent, sink);
            } finally {
                IOUtils.closeQuietly(xmlContent);
            }
            w.writeEndElement();
        }
    }

    private void writeContentDigest(XMLStreamWriter w,
            ContentDigest contentDigest) throws XMLStreamException {
        if (contentDigest != null) {
            w.writeStartElement(FOXML.contentDigest);
            writeAttribute(w, FOXML.TYPE, contentDigest.type());
            writeAttribute(w, FOXML.DIGEST, contentDigest.hexValue());
            w.writeEndElement();
        }
    }

    private static void writeAttribute(XMLStreamWriter w, String name,
            Object[] values) throws XMLStreamException {
        if (values != null && values.length > 0) {
            StringBuilder b = new StringBuilder();
            for (Object value: values) {
                if (b.length() > 0) {
                    b.append(" ");
                }
                b.append(value);
            }
            w.writeAttribute(name, b.toString());
        }
    }

    private static void writeAttribute(XMLStreamWriter w, String name,
            Date value) throws XMLStreamException {
        if (value != null) {
            writeAttribute(w, name, Util.toString(value));
        }
    }

    private static void writeAttribute(XMLStreamWriter w, String name,
            State value) throws XMLStreamException {
        if (value != null) {
            writeAttribute(w, name, value.shortName());
        }
    }

    private static void writeAttribute(XMLStreamWriter w, String name,
            ControlGroup value) throws XMLStreamException {
        if (value != null) {
            writeAttribute(w, name, value.shortName());
        }
    }

    private static void writeAttribute(XMLStreamWriter w, String name,
            Object value) throws XMLStreamException {
        if (value != null) {
            w.writeAttribute(name, value.toString());
        }
    }

    private static void writeProperty(XMLStreamWriter w, String name,
            State value) throws XMLStreamException {
        if (value != null) {
            writeProperty(w, name, value.longName());
        }
    }

    private static void writeProperty(XMLStreamWriter w, String name,
            Date value) throws XMLStreamException {
        if (value != null) {
            writeProperty(w, name, Util.toString(value));
        }
    }

    private static void writeProperty(XMLStreamWriter w, String name,
            String value) throws XMLStreamException {
        if (value != null) {
            w.writeStartElement(FOXML.property);
            w.writeAttribute(FOXML.NAME, name);
            w.writeAttribute(FOXML.VALUE, value);
            w.writeEndElement();
        }
    }

}
