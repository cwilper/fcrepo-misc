package com.github.cwilper.fcrepo.dto.foxml;

import com.github.cwilper.fcrepo.dto.core.ContentDigest;
import com.github.cwilper.fcrepo.dto.core.ControlGroup;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import com.github.cwilper.fcrepo.dto.core.State;
import com.github.cwilper.fcrepo.dto.core.io.AbstractDTOReader;
import com.github.cwilper.fcrepo.dto.core.io.XMLUtil;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FOXMLReader extends AbstractDTOReader {

    private static final Logger logger = LoggerFactory.getLogger(
            FOXMLReader.class);

    private FedoraObject obj;
    private XMLStreamReader r;

    public FOXMLReader() {
    }

    @Override
    public FedoraObject readObject(InputStream source) throws IOException {
        obj = new FedoraObject();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            r = factory.createXMLStreamReader(source, Constants.CHAR_ENCODING);
            readObject();
            return obj;
        } catch (XMLStreamException e) {
            throw new IOException(e);
        } finally {
            XMLUtil.closeQuietly(r);
            IOUtils.closeQuietly(source);
        }
    }

    private void readObject() throws IOException, XMLStreamException {
        if (moveToStart(Constants.digitalObject, null)) {
            obj.pid(readAttribute(Constants.PID));
            readObjectProperties();
            while (r.getEventType() == XMLStreamConstants.START_ELEMENT
                    && r.getLocalName().equals(Constants.datastream)) {
                readDatastream();
            }
        }
    }

    private void readObjectProperties() throws XMLStreamException {
        while (moveToStart(Constants.property, Constants.datastream)) {
            String name = readAttribute(Constants.NAME);
            String value = readAttribute(Constants.VALUE);
            if (name != null) {
                if (name.equals(Constants.STATE_URI)) {
                    obj.state(parseState(value, "object"));
                } else if (name.equals(Constants.LABEL_URI)) {
                    obj.label(value);
                } else if (name.equals(Constants.OWNERID_URI)) {
                    obj.ownerId(value);
                } else if (name.equals(Constants.CREATEDDATE_URI)) {
                    obj.createdDate(parseDate(value, "object created"));

                } else if (name.equals(Constants.LASTMODIFIEDDATE_URI)) {
                    obj.lastModifiedDate(parseDate(value,
                            "object last modified"));
                } else {
                    logger.warn("Ignoring unrecognized object property name: "
                            + name);
                }
            }
        }
    }

    private void readDatastream() throws IOException, XMLStreamException {
        String id = readAttribute(Constants.ID);
        if (id != null) {
            Datastream ds = new Datastream(id);
            obj.putDatastream(ds);
            ds.state(parseState(readAttribute(Constants.STATE), "datastream"));
            ds.controlGroup(parseControlGroup(readAttribute(
                    Constants.CONTROL_GROUP)));
            ds.versionable(parseVersionable(readAttribute(
                    Constants.VERSIONABLE)));
            while (moveToStart(Constants.datastreamVersion,
                    Constants.datastream)) {
                readDatastreamVersion(ds);
            }
        } else {
            logger.warn("Ignoring datastream; no id specified");
        }
    }

    private void readDatastreamVersion(Datastream ds)
            throws IOException, XMLStreamException {
        String id = readAttribute(Constants.ID);
        if (id != null) {
            Date created = parseDate(readAttribute(Constants.CREATED),
                    "datastream created");
            DatastreamVersion dsv = new DatastreamVersion(id, created);
            ds.versions().add(dsv);
            dsv.altIds().addAll(parseAltIds(readAttribute(Constants.ALT_IDS)));
            dsv.label(readAttribute(Constants.LABEL));
            dsv.mimeType(readAttribute(Constants.MIMETYPE));
            dsv.formatURI(parseURI(readAttribute(Constants.FORMAT_URI),
                    "datastream format uri"));
            dsv.size(parseSize(readAttribute(Constants.SIZE)));
            if (r.nextTag() == XMLStreamConstants.START_ELEMENT) {
                if (r.getLocalName().equals(Constants.contentDigest)) {
                    readContentDigest(dsv);
                    if (r.nextTag() == XMLStreamConstants.END_ELEMENT) {
                        return; // end of datastreamVersion
                    }
                }
                if (r.getLocalName().equals(Constants.xmlContent)) {
                    readXMLContent(dsv);
                } else if (r.getLocalName().equals(Constants.binaryContent)) {
                    readBinaryContent(ds, dsv);
                } else if (r.getLocalName().equals(Constants.contentLocation)) {
                    readContentLocation(dsv);
                }
            }
        } else {
            logger.warn("Ignoring datastream version; no id specified");
        }
    }

    private void readContentLocation(DatastreamVersion dsv)
            throws XMLStreamException {
        String type = readAttribute(Constants.TYPE);
        URI ref = parseURI(readAttribute(Constants.REF), "contentLocation ref");
        if (ref != null) {
            if (Constants.INTERNALREF_TYPE.equals(type)) {
                try {
                    dsv.contentLocation(new URI(Constants.INTERNALREF_SCHEME
                            + ":" + ref));
                } catch (URISyntaxException e) {
                    logger.warn("Ignoring malformed contentLocation "
                            + "internal ref: " + ref);
                }
            } else {
                dsv.contentLocation(ref);
            }
        }
    }

    private void readBinaryContent(Datastream ds, DatastreamVersion dsv)
            throws IOException, XMLStreamException {
        OutputStream sink = contentHandler.handleContent(obj, ds, dsv);
        try {
            Base64OutputStream out = new Base64OutputStream(sink, false);
            while (r.next() != XMLStreamConstants.END_ELEMENT) {
                if (r.isCharacters()) {
                    out.write(r.getText().getBytes(Constants.CHAR_ENCODING));
                }
            }
            out.flush();
        } finally {
            IOUtils.closeQuietly(sink);
        }
    }

    private void readXMLContent(DatastreamVersion dsv)
            throws IOException, XMLStreamException {
        while (r.next() != XMLStreamConstants.START_ELEMENT) {
            if (r.getEventType() == XMLStreamConstants.END_ELEMENT) {
                return; // xmlContent element is empty
            }
        }
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        try {
            XMLUtil.copy(r, sink);
        } catch (Exception e) {
            throw new IOException("Error parsing foxml:xmlContent", e);
        }

        dsv.inlineXML(sink.toByteArray());
    }

    private void readContentDigest(DatastreamVersion dsv)
            throws XMLStreamException {
        String type = readAttribute(Constants.TYPE);
        String digest = readAttribute(Constants.DIGEST);
        if (type != null || digest != null) {
            dsv.contentDigest(new ContentDigest().type(type).hexValue(digest));
        }
        r.nextTag(); // consume closing contentDigest tag
    }

    private boolean moveToStart(String localName,
                                String stopAtLocalName)
            throws XMLStreamException {
        while (r.hasNext()) {
            if (r.next() == XMLStreamConstants.START_ELEMENT) {
                if (r.getLocalName().equals(localName)) {
                    return true;
                } else if (r.getLocalName().equals(stopAtLocalName)) {
                    return false;
                }
            }
        }
        return false;
    }

    private String readAttribute(String localName) throws XMLStreamException {
        String value = r.getAttributeValue(null, localName);
        if (value != null && value.trim().length() > 0) {
            return value.trim();
        } else {
            return null;
        }
    }

    private static Date parseDate(String value, String kind) {
        if (value == null) return null;
        Date date = Util.toDate(value);
        if (date == null) {
            logger.warn("Ignoring malformed " + kind + " date value: " + value);
        }
        return date;
    }

    private static State parseState(String value, String kind) {
        if (value == null) return null;
        try {
            return State.forShortName(value);
        } catch (IllegalArgumentException e) {
            try {
                return State.forLongName(value);
            } catch (IllegalArgumentException e2) {
                logger.warn("Ignoring unrecognized " + kind + " state value: "
                        + value);
                return null;
            }
        }
    }

    private static ControlGroup parseControlGroup(String value) {
        if (value == null) return null;
        try {
            return ControlGroup.forShortName(value);
        } catch (IllegalArgumentException e) {
            logger.warn("Ignoring unrecognized datastream control group value: "
                    + value);
            return null;
        }
    }

    private static Boolean parseVersionable(String value) {
        if (value == null) return null;
        if (value.equalsIgnoreCase("true") || value.equals("1")) {
            return true;
            
        } else if (value.equalsIgnoreCase("false") || value.equals("0")) {
            return false;
        } else {
            logger.warn("Ignoring unrecognized datastream versionable value: "
                    + value);
            return null;
        }
    }

    private static Long parseSize(String value) {
        if (value == null) return null;
        try {
            long n = Long.parseLong(value);
            if (n < 0) {
                logger.warn("Ignoring negative datastream size value: "
                        + value);
                return null;
            } else {
                return n;
            }
        } catch (NumberFormatException e) {
            logger.warn("Ignoring invalid datastream size value: " + value);
            return null;
        }
    }

    private static URI parseURI(String value, String kind) {
        if (value == null) return null;
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            logger.warn("Ignoring malformed " + kind + " value:");
            return null;
        }
    }

    private static Set<URI> parseAltIds(String value) {
        Set<URI> set = new HashSet<URI>();
        if (value != null) {
            for (String uriString: value.split("\\s+")) {
                URI uri = parseURI(uriString, "datastream altId");
                if (uri != null) set.add(uri);
            }
        }
        return set;
    }

}
