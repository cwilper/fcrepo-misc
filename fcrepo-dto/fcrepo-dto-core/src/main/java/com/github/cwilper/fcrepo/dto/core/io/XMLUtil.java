package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import javanet.staxutils.XMLStreamUtils;
import org.apache.axiom.c14n.CanonicalizerSpi;
import org.apache.axiom.c14n.impl.Canonicalizer20010315ExclOmitComments;
import org.apache.commons.io.IOUtils;

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

    /*
    public static void copy(XMLStreamReader source, Writer sink)
            throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(sink);
        copy(source, writer);
        writer.flush();
    }
    */

    public static void copy(XMLStreamReader source, OutputStream sink)
            throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(sink, "UTF-8");
        copy(source, writer);
        writer.flush();
    }

    public static void copy(XMLStreamReader source, XMLStreamWriter sink)
            throws XMLStreamException {

        sink.setNamespaceContext(source.getNamespaceContext());

        
//        sink.flush();
        XMLStreamUtils.copy(new BalancedStreamReader(source), sink);
//        XMLStreamUtils.copy(source, sink);
    }

}
