package com.github.cwilper.fcrepo.dto.foxml;

import com.github.cwilper.fcrepo.dto.core.ContentDigest;
import com.github.cwilper.fcrepo.dto.core.ControlGroup;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import com.github.cwilper.fcrepo.dto.core.State;
import com.github.cwilper.fcrepo.httpclient.HttpUtil;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// not threadsafe!
public class FOXMLWriter {

    private static final Logger logger =
            LoggerFactory.getLogger(FOXMLWriter.class);

    private Set<String> managedDatastreamsToEmbed;
    private HttpClient httpClient;

    private FedoraObject obj;
    private OutputStream sink;

    private XMLStreamWriter w;
    
    public FOXMLWriter() {
        managedDatastreamsToEmbed = new HashSet<String>();
        httpClient = new DefaultHttpClient();
    }

    public void setManagedDatastreamsToEmbed(
            Set<String> managedDatastreamsToEmbed) {
        this.managedDatastreamsToEmbed = managedDatastreamsToEmbed;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void write(FedoraObject obj, OutputStream sink) throws IOException {
        this.obj = obj;
        this.sink = sink;
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            w = factory.createXMLStreamWriter(sink, Constants.CHAR_ENCODING);
            writeDigitalObject();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        } finally {
            try {
                if (w != null) {
                    w.close();
                }
            } catch (XMLStreamException e) {
                logger.warn("Error closing XMLStreamWriter", e);
            }
        }
    }

    private void writeDigitalObject()
            throws IOException, XMLStreamException {
        w.writeStartDocument(Constants.CHAR_ENCODING, Constants.XML_VERSION);

        w.writeStartElement(Constants.digitalObject);
        w.writeDefaultNamespace(Constants.xmlns);
        
        w.writeAttribute(Constants.VERSION, Constants.FOXML_VERSION);
        writeAttribute(Constants.PID, obj.pid());

        writeObjectProperties(obj);
        for (String id: obj.datastreams().keySet()) {
            writeDatastream(obj.datastreams().get(id));
        }
        w.writeEndDocument();
    }

    private void writeObjectProperties(FedoraObject obj)
            throws XMLStreamException {
        if (obj.state() != null || obj.label() != null
                || obj.ownerId() != null || obj.createdDate() != null
                || obj.lastModifiedDate() != null) {
            w.writeStartElement(Constants.objectProperties);
            writeProperty(Constants.STATE_URI, obj.state());
            writeProperty(Constants.LABEL_URI, obj.label());
            writeProperty(Constants.OWNERID_URI, obj.ownerId());
            writeProperty(Constants.CREATEDDATE_URI, obj.createdDate());
            writeProperty(Constants.LASTMODIFIEDDATE_URI, obj.lastModifiedDate());
            w.writeEndElement();
        }
    }

    private void writeDatastream(Datastream ds)
            throws IOException, XMLStreamException {
        w.writeStartElement(Constants.datastream);
        writeAttribute(Constants.ID, ds.id());
        writeAttribute(Constants.STATE, ds.state());
        writeAttribute(Constants.CONTROL_GROUP, ds.controlGroup());
        writeAttribute(Constants.VERSIONABLE, ds.versionable());
        for (DatastreamVersion dsv: ds.versions()) {
            writeDatastreamVersion(ds, dsv);
        }
        w.writeEndElement();
    }

    private void writeDatastreamVersion(Datastream ds, DatastreamVersion dsv)
            throws IOException, XMLStreamException {
        w.writeStartElement(Constants.datastreamVersion);
        writeAttribute(Constants.ID, dsv.id());
        writeAttribute(Constants.ALT_IDS, dsv.altIds().toArray());
        writeAttribute(Constants.LABEL, dsv.label());
        writeAttribute(Constants.CREATED, dsv.createdDate());
        writeAttribute(Constants.MIMETYPE, dsv.mimeType());
        writeAttribute(Constants.FORMAT_URI, dsv.formatURI());
        writeAttribute(Constants.SIZE, dsv.size());
        writeContentDigest(dsv.contentDigest());
        if (ds.controlGroup() == ControlGroup.INLINE_XML) {
            writeXMLContent(dsv);
        } else if (ds.controlGroup() == ControlGroup.MANAGED_CONTENT
                && managedDatastreamsToEmbed.contains(ds.id())) {
            writeBinaryContent(dsv.contentLocation());
        } else {
            writeContentLocation(dsv.contentLocation());
        }
        w.writeEndElement();
    }

    private void writeContentLocation(URI ref) throws XMLStreamException {
        if (ref != null) {
            w.writeStartElement(Constants.contentLocation);
            if (ref.getScheme().equalsIgnoreCase(Constants.INTERNAL_TYPE)) {
                w.writeAttribute(Constants.TYPE, Constants.INTERNAL_TYPE);
                w.writeAttribute(Constants.REF, ref.getRawSchemeSpecificPart());
            } else {
                w.writeAttribute(Constants.TYPE, Constants.URL_TYPE);
                w.writeAttribute(Constants.REF, ref.toString());
            }
            w.writeEndElement();
        }
    }

    private void writeBinaryContent(URI ref)
            throws IOException, XMLStreamException {
        if (ref != null) {
            w.writeStartElement(Constants.binaryContent);
            w.writeCharacters(Constants.LINE_FEED);
            w.flush();
            Base64OutputStream out = new Base64OutputStream(sink,
                    true, Constants.BASE64_LINE_LENGTH,
                    Constants.LINE_FEED.getBytes(Constants.CHAR_ENCODING));
            if (ref.getScheme().equals("http")
                    || ref.getScheme().equals("https")) {
                HttpUtil.get(httpClient, ref, out);
            } else if (ref.getScheme().equals("file")) {
                InputStream in = new FileInputStream(
                        new File(ref.getRawSchemeSpecificPart()));
                try {
                    IOUtils.copy(in, out);
                } finally {
                    IOUtils.closeQuietly(in);
                }
            } else {
                throw new IOException("Cannot read " + ref + " -- unsupported "
                        + "scheme");
            }
            out.flush();
            w.writeEndElement();
        }
    }

    private void writeXMLContent(DatastreamVersion dsv)
            throws IOException, XMLStreamException {
        if (dsv.hasInlineXML()) {
            w.writeStartElement(Constants.xmlContent);
            w.writeCharacters(Constants.LINE_FEED);
            w.flush();
            OutputStreamWriter sinkWriter = new OutputStreamWriter(sink);
            dsv.getInlineXML(sinkWriter);
            sinkWriter.flush();
            w.writeEndElement();
        }
    }

    private void writeContentDigest(ContentDigest contentDigest)
            throws XMLStreamException {
        if (contentDigest != null) {
            w.writeStartElement(Constants.contentDigest);
            writeAttribute(Constants.TYPE, contentDigest.type());
            writeAttribute(Constants.DIGEST, contentDigest.hexValue());
            w.writeEndElement();
        }
    }

    private void writeAttribute(String name, Object[] values)
            throws XMLStreamException {
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

    private void writeAttribute(String name, Date value)
            throws XMLStreamException {
        if (value != null) {
            writeAttribute(name, Util.toString(value));
        }
    }

    private void writeAttribute(String name, State value)
            throws XMLStreamException {
        if (value != null) {
            writeAttribute(name, value.shortName());
        }
    }

    private void writeAttribute(String name, ControlGroup value)
            throws XMLStreamException {
        if (value != null) {
            writeAttribute(name, value.shortName());
        }
    }

    private void writeAttribute(String name, Object value)
            throws XMLStreamException {
        if (value != null) {
            w.writeAttribute(name, value.toString());
        }
    }

    private void writeProperty(String name, State value)
            throws XMLStreamException {
        if (value != null) {
            writeProperty(name, value.longName());
        }
    }

    private void writeProperty(String name, Date value)
            throws XMLStreamException {
        if (value != null) {
            writeProperty(name, Util.toString(value));
        }
    }

    private void writeProperty(String name, String value)
            throws XMLStreamException {
        if (value != null) {
            w.writeStartElement(Constants.property);
            w.writeAttribute(Constants.NAME, name);
            w.writeAttribute(Constants.VALUE, value);
            w.writeEndElement();
        }
    }

}
