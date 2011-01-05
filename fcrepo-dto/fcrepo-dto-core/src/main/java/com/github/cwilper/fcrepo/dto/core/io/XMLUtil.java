package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import org.apache.axiom.c14n.CanonicalizerSpi;
import org.apache.axiom.c14n.impl.Canonicalizer20010315ExclOmitComments;
import org.apache.commons.io.IOUtils;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public abstract class XMLUtil implements XMLStreamConstants {

    private final static String prettyXSL = "<xsl:stylesheet version=\"1.0\" " +
            " xmlns:xalan=\"http://xml.apache.org/xalan\"" +
            " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" +
            "<xsl:output method=\"xml\" indent=\"yes\"" +
            " xalan:indent-amount=\"2\"/>" +
            "<xsl:strip-space elements=\"*\"/>" +
            "<xsl:template match=\"/\">" +
            "  <xsl:copy-of select=\".\"/>" +
            "</xsl:template>" +
            "</xsl:stylesheet>";

    public static void prettyPrint(DTOWriter writer,
                                   FedoraObject obj,
                                   Writer sink) throws IOException {
        ByteArrayOutputStream rawOutput = new ByteArrayOutputStream();
        writer.writeObject(obj, rawOutput);
        ByteArrayInputStream source = new ByteArrayInputStream(
                rawOutput.toByteArray());
        prettyPrint(source, sink);
    }

    public static byte[] prettyPrint(byte[] inBytes) throws IOException {
        InputStream source = new ByteArrayInputStream(inBytes);
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        prettyPrint(new ByteArrayInputStream(inBytes),
                new OutputStreamWriter(sink, "UTF-8"));
        return sink.toByteArray();
    }

    public static void prettyPrint(InputStream source,
                                   Writer sink) throws IOException {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer t = tFactory.newTransformer(
                    new StreamSource(new StringReader(prettyXSL)));
            t.transform(new StreamSource(source), new StreamResult(sink));
        } catch (TransformerException e) {
            throw new IOException(e);
        } finally {
            IOUtils.closeQuietly(source);
        }
    }

    public static String prettyPrint(InputStream source) throws IOException {
        StringWriter sink = new StringWriter();
        prettyPrint(source, sink);
        return sink.toString();
    }

    // produces exclusive xml canonicalized form of the input document,
    // without comments, as defined by http://www.w3.org/TR/xml-exc-c14n/
    public static byte[] canonicalize(byte[] inBytes) throws IOException {
        try {
            CanonicalizerSpi c = new Canonicalizer20010315ExclOmitComments();
            return c.engineCanonicalize(inBytes);
        } catch (Exception e) {
            throw new IOException(e);
        }
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

    public static void copy(XMLStreamReader source, Writer sink,
                            boolean repair) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(sink);
        copy(source, writer, repair);
        writer.flush();
    }

    public static void copy(XMLStreamReader source, OutputStream sink,
            boolean repair) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(sink, "UTF-8");
        copy(source, writer, repair);
        writer.flush();
    }

    /**
     * Copies the current element and all children.
     *
     * NOTE: This implementation was lifted from activesoap's XMLStreamHelper,
     *       available at http://svn.codehaus.org/activesoap/trunk/activesoap
     *       /src/java/org/codehaus/activesoap/util/XMLStreamHelper.java
     */
    public static void copy(XMLStreamReader source, XMLStreamWriter sink,
            boolean repair) throws XMLStreamException {
        int elementCount = 0;
        for (int code = source.getEventType(); source.hasNext();
             code = source.next()) {
          elementCount = copyOne(source, sink, repair, code, elementCount);
        }
        while (elementCount-- > 0) {
            sink.writeEndElement();
        }
    }

    private static int copyOne(XMLStreamReader in, XMLStreamWriter out,
            boolean repair, int code, int elementCount)
            throws XMLStreamException {
      switch (code) {
        case START_ELEMENT:
            elementCount++;
            writeStartElementAndAttributes(out, in, repair);
            break;
        case END_ELEMENT:
            if (--elementCount < 0) {
                return elementCount;
            }
            out.writeEndElement();
            break;
        case CDATA:
            out.writeCData(in.getText());
            break;
        case CHARACTERS:
            out.writeCharacters(in.getText());
            break;
      }
      return elementCount;
    }

    private static void writeStartElementAndAttributes(XMLStreamWriter out,
            XMLStreamReader in, boolean repair) throws XMLStreamException {
        writeStartElement(out, in, repair);
        if (!repair) {
            writeNamespaces(out, in, in.getPrefix());
        }
        writeAttributes(out, in);
    }

    private static void writeAttributes(XMLStreamWriter out,
            XMLStreamReader in) throws XMLStreamException {
        int count = in.getAttributeCount();
        for (int i = 0; i < count; i++) {
            out.writeAttribute(in.getAttributePrefix(i),
                    in.getAttributeNamespace(i),
                    in.getAttributeLocalName(i),
                    in.getAttributeValue(i));
        }
    }

    private static void writeNamespaces(XMLStreamWriter out,
            XMLStreamReader in, String prefixOfCurrentElement)
            throws XMLStreamException {
        int count = in.getNamespaceCount();
        for (int i = 0; i < count; i++) {
            String prefix = in.getNamespacePrefix(i);
            String uri = in.getNamespaceURI(i);
            
            if ( prefixOfCurrentElement == null && prefix.length()==0 ) {
              continue;
            }

            if (prefixOfCurrentElement != null
                    && prefixOfCurrentElement.equals(prefix)) {
                continue;
            }
            if (isPrefixNotMappedToUri(out, prefix, uri)) {
                out.writeNamespace(prefix, uri);
            }
        }
    }

    private static void writeStartElement(XMLStreamWriter out,
            XMLStreamReader in, boolean repair) throws XMLStreamException {
        String prefix = in.getPrefix();

        // we can avoid this step if in repair mode
        int count = in.getNamespaceCount();
        for (int i = 0; i < count; i++) {
            String aPrefix = in.getNamespacePrefix(i);
            if (prefix == aPrefix
                    || (prefix != null && prefix.equals(aPrefix))) {
                continue;
            }
            String uri = in.getNamespaceURI(i);

            if (isPrefixNotMappedToUri(out, aPrefix, uri)) {
                if (aPrefix != null && aPrefix.length() > 0) {
                    out.setPrefix(aPrefix, uri);
                }
                else {
                    out.setDefaultNamespace(uri);
                }
            }
        }
        String localName = in.getLocalName();
        String uri = in.getNamespaceURI();
        writeStartElement(out, prefix, uri, localName, repair);
    }

    private static void writeStartElement(XMLStreamWriter out, String prefix,
            String uri, String localName, boolean repair)
            throws XMLStreamException {
        boolean map = isPrefixNotMappedToUri(out, prefix, uri);
        if (prefix != null && prefix.length() > 0) {
            if (map) {
                out.setPrefix(prefix, uri);
            }
            out.writeStartElement(prefix, localName, uri);
            if (map && !repair) {
                out.writeNamespace(prefix, uri);
            }
        } else {
            boolean skipDefaultNamespace = false;
            boolean hasURI = uri != null && uri.length() > 0;
            if (map && hasURI) {

                if (uri.equals("info:fedora/fedora-system:def/foxml#")) {
                    skipDefaultNamespace = true;
                    // TODO: Fix this hack; it prevents wrapping foxml in foxml.
                    // It's probably avoidable if foxml docs are consistently
                    // written to be bound to a namespace prefix rather than
                    // the default namespace.  Otherwise, non-namespaced XML
                    // written within an xmlContent section is assumed to be
                    // bound to the default namespace, which causes this
                    // extraneous declaration when a copy is performed.
                } else {
                    out.setDefaultNamespace(uri);
                }
            }
            if (skipDefaultNamespace) {
                out.writeStartElement(localName);
            } else {
                out.writeStartElement(uri, localName);
            }
            if (map && !repair && hasURI && !skipDefaultNamespace) {
                out.writeDefaultNamespace(uri);
            }
        }
    }

    private static boolean isPrefixNotMappedToUri(XMLStreamWriter out,
            String prefix, String uri) {
        if (prefix == null) {
            prefix = "";
        }
        NamespaceContext context = out.getNamespaceContext();
        if (context == null) {
            return false;
        }
        String mappedUri = context.getPrefix(prefix);
        boolean map = (mappedUri == null || !mappedUri.equals(uri));
        return map;
    }
}
