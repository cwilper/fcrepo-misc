package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import javanet.staxutils.XMLStreamUtils;
import org.apache.axiom.c14n.CanonicalizerSpi;
import org.apache.axiom.c14n.impl.Canonicalizer20010315ExclOmitComments;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.ErrorListener;
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

    private final static Logger logger =
            LoggerFactory.getLogger(XMLUtil.class);

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

    private final static String prettyXSLLite =
            "<xsl:stylesheet version=\"1.0\" " +
            " xmlns:xalan=\"http://xml.apache.org/xalan\"" +
            " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" +
            "<xsl:output method=\"xml\" indent=\"yes\"" +
            " xalan:indent-amount=\"2\" omit-xml-declaration=\"yes\"/>" +
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
        prettyPrint(source, sink, false);
    }

    public static byte[] prettyPrint(byte[] inBytes,
                                     boolean omitXMLDeclaration)
            throws IOException {
        InputStream source = new ByteArrayInputStream(inBytes);
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        prettyPrint(new ByteArrayInputStream(inBytes),
                new OutputStreamWriter(sink, "UTF-8"), omitXMLDeclaration);
        return sink.toByteArray();
    }

    public static void prettyPrint(InputStream source,
                                   Writer sink,
                                   boolean omitXMLDeclaration)
            throws IOException {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            String xsl = prettyXSL;
            if (omitXMLDeclaration) xsl = prettyXSLLite;
            Transformer t = tFactory.newTransformer(
                    new StreamSource(new StringReader(xsl)));
            t.setErrorListener(new DebugLoggingErrorListener());
            t.transform(new StreamSource(source), new StreamResult(sink));
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            IOUtils.closeQuietly(source);
        }
    }

    public static String prettyPrint(InputStream source,
                                     boolean omitXMLDeclaration)
            throws IOException {
        StringWriter sink = new StringWriter();
        prettyPrint(source, sink, omitXMLDeclaration);
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
        XMLStreamUtils.copy(new BalancedXMLStreamReader(source), sink);
    }

    private static class DebugLoggingErrorListener implements ErrorListener {

        @Override
        public void warning(TransformerException e) {
            logger.debug("Warning during transformation", e);
        }

        @Override
        public void error(TransformerException e) {
            logger.debug("Error during transformation", e);
        }

        @Override
        public void fatalError(TransformerException e)
                throws TransformerException {
            logger.debug("Fatal error during transformation");
        }
    }

}
