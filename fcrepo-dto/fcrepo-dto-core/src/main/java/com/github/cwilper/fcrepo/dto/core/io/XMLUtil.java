package com.github.cwilper.fcrepo.dto.core.io;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import javanet.staxutils.XMLStreamUtils;
import org.apache.commons.io.IOUtils;
import org.apache.xml.security.c14n.Canonicalizer;
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

    static {
        // initialize xmlsec
        org.apache.xml.security.Init.init();
    }

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
            StringWriter preTrimmed = new StringWriter();
            t.transform(new StreamSource(source), new StreamResult(preTrimmed));
            preTrimmed.flush();
            sink.write(preTrimmed.toString().trim());
            sink.flush();
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

    // produces canonicalized form of the input document, without comments,
    // as defined by XML Canonicalization 1.1:
    // http://www.w3.org/TR/xml-c14n11/ 
    public static byte[] canonicalize(byte[] inBytes) throws IOException {
        try {
            Canonicalizer c = Canonicalizer.getInstance(
                    Canonicalizer.ALGO_ID_C14N11_OMIT_COMMENTS);
            return c.canonicalize(inBytes);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static void closeQuietly(XMLStreamWriter w) {
        if (w != null) {
            try {
                w.close();
            } catch (XMLStreamException e) {
                logger.warn("Error while closing", e);
            }
        }
    }

    public static void closeQuietly(XMLStreamReader r) {
        if (r != null) {
            try {
                r.close();
            } catch (XMLStreamException e) {
                logger.warn("Error while closing", e);
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
