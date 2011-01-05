package com.github.cwilper.fcrepo.dto.foxml;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

abstract class Util {

    public static final String PREFERRED_DATE_FORMAT =
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String[] ALLOWED_DATE_FORMATS = new String[] {
            PREFERRED_DATE_FORMAT,
            "yyyy-MM-dd'T'HH:mm:ss.SS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.S'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SS",
            "yyyy-MM-dd'T'HH:mm:ss.S",
            "yyyy-MM-dd'T'HH:mm:ss"
    };

    public static Date toDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (String format: ALLOWED_DATE_FORMATS) {
            dateFormat.applyPattern(format);
            try {
                return dateFormat.parse(dateString);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    public static String toString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(PREFERRED_DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static void closeQuietly(XMLStreamWriter w) {
        if (w != null) {
            try {
                w.close();
            } catch (XMLStreamException e) {
            }
        }
    }

    public static void closeQuietly(XMLStreamReader r) {
        if (r != null) {
            try {
                r.close();
            } catch (XMLStreamException e) {
            }
        }
    }

    /**
     * Copies the reader to the writer. The start and end document methods must
     * be handled on the writer manually. TODO: if the namespace on the reader
     * has been declared previously to where we are in the stream, this probably
     * won't work.
     *
     * @param reader
     * @param writer
     * @throws XMLStreamException
     */
    public static void copy(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        copy(reader, writer, false);
    }
    public static void copy(XMLStreamReader reader, XMLStreamWriter writer,
                            boolean fragment) throws XMLStreamException {
        // number of elements read in
        int read = 0;
        int event = reader.getEventType();

        while (reader.hasNext()) {
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
                read++;
                writeStartElement(reader, writer);
                break;
            case XMLStreamConstants.END_ELEMENT:
                writer.writeEndElement();
                read--;
                if (read <= 0 && !fragment) {
                    return;
                }
                break;
            case XMLStreamConstants.CHARACTERS:
                writer.writeCharacters(reader.getText());
                break;
            case XMLStreamConstants.START_DOCUMENT:
            case XMLStreamConstants.END_DOCUMENT:
            case XMLStreamConstants.ATTRIBUTE:
            case XMLStreamConstants.NAMESPACE:
                break;
            default:
                break;
            }
            event = reader.next();
        }
    }

    private static void writeStartElement(XMLStreamReader reader, XMLStreamWriter writer)
        throws XMLStreamException {
        String local = reader.getLocalName();
        String uri = reader.getNamespaceURI();
        String prefix = reader.getPrefix();
        if (prefix == null) {
            prefix = "";
        }

        boolean writeElementNS = false;
        if (uri != null) {
            String boundPrefix = writer.getPrefix(uri);
            if (boundPrefix == null || !prefix.equals(boundPrefix)) {
                writeElementNS = true;
            }
        }

        // Write out the element name
        if (uri != null) {
            if (prefix.length() == 0 && uri.length() > 0) {
                writer.writeStartElement(local);
                writer.setDefaultNamespace(uri);

            } else {
                writer.writeStartElement(prefix, local, uri);
                writer.setPrefix(prefix, uri);
            }
        } else {
            writer.writeStartElement(local);
        }

        // Write out the namespaces
        for (int i = 0; i < reader.getNamespaceCount(); i++) {
            String nsURI = reader.getNamespaceURI(i);
            String nsPrefix = reader.getNamespacePrefix(i);
            if (nsPrefix == null) {
                nsPrefix = "";
            }

            if (nsPrefix.length() == 0) {
                writer.writeDefaultNamespace(nsURI);
            } else {
                writer.writeNamespace(nsPrefix, nsURI);
            }

            if (nsURI.equals(uri) && nsPrefix.equals(prefix)) {
                writeElementNS = false;
            }
        }

        // Check if the namespace still needs to be written.
        // We need this check because namespace writing works
        // different on Woodstox and the RI.
        if (writeElementNS) {
            if (prefix.length() == 0) {
                writer.writeDefaultNamespace(uri);
            } else {
                writer.writeNamespace(prefix, uri);
            }
        }

        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String ns = reader.getAttributeNamespace(i);
            String nsPrefix = reader.getAttributePrefix(i);
            if (ns == null || ns.length() == 0) {
                writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
            } else if (nsPrefix == null || nsPrefix.length() == 0) {
                writer.writeAttribute(reader.getAttributeNamespace(i), reader.getAttributeLocalName(i),
                                      reader.getAttributeValue(i));
            } else {
                writer.writeAttribute(reader.getAttributePrefix(i), reader.getAttributeNamespace(i), reader
                    .getAttributeLocalName(i), reader.getAttributeValue(i));
            }

        }
    }


}
