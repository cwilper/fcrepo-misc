package com.github.cwilper.fcrepo.dto.core.io;

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

/**
 * XML reading and writing utility methods used by <code>dto.core</code>
 * and available for external use.
 */
public final class XMLUtil implements XMLStreamConstants {

    private final static Logger logger =
            LoggerFactory.getLogger(XMLUtil.class);

    // Stylesheet that does pretty printing (output contains XML declaration)
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

    // Stylesheet that does pretty printing (output omits XML declaration)
    // TODO: Combine these and possibly pre-compile them?
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

    private XMLUtil() { }

    /**
     * Pretty-prints the given XML.
     * <p>
     * The input must be well-formed. The transformation has the following
     * effect:
     * <ul>
     *   <li> Leading and trailing whitespace will be removed.</li>
     *   <li> All processing instructions and doctype declarations will be
     *        removed.</li>
     *   <li> If Xalan is the currently active XML transformer (true by
     *        default in Java 6), it will be reformatted to have two-space
     *        indents. Otherwise, indenting behavior is undefined (it may or
     *        may not be indented).</li>
     *   <li> Attributes will use double-quotes around values.</li>
     *   <li> All empty elements (e.g.
     *        <code>&lt;element&gt;&lt;element&gt;</code>) will be
     *        collapsed (e.g. <code>&lt;element/&gt;</code></li>
     * </ul>
     *
     * @param inBytes the xml to be pretty-printed.
     * @param omitXMLDeclaration whether to omit the xml declaration in the
     *        output. If false, the output will not be directly embeddable in
     *        other XML documents.
     * @return the pretty-printed XML as a UTF-8 encoded byte array.
     * @throws IOException
     */
    public static byte[] prettyPrint(byte[] inBytes,
                                     boolean omitXMLDeclaration)
            throws IOException {
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        prettyPrint(new ByteArrayInputStream(inBytes),
                new OutputStreamWriter(sink, "UTF-8"), omitXMLDeclaration);
        return sink.toByteArray();
    }

    private static void prettyPrint(InputStream source,
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

    /**
     * Produces the canonicalized form of the input document, without comments,
     * as defined by <a href="http://www.w3.org/TR/xml-c14n11/">XML
     * Canonicalization 1.1</a>.
     * <p>
     * <b>NOTE:</b> Although this method will likely fail if the input is
     * malformed XML, successful canonicalization does not imply that the
     * input was well-formed!
     *
     * @param inBytes the xml to be canonicalized.
     * @return the canonicalized XML as a UTF-8 encoded byte array.
     * @throws IOException if the input cannot be canonicalized for any reason.
     */
    public static byte[] canonicalize(byte[] inBytes) throws IOException {
        try {
            Canonicalizer c = Canonicalizer.getInstance(
                    Canonicalizer.ALGO_ID_C14N11_OMIT_COMMENTS);
            return c.canonicalize(inBytes);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * Closes the given writer without throwing an exception on failure.
     * Instead, a warning will be logged.
     *
     * @param writer the writer to close.
     */
    public static void closeQuietly(XMLStreamWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (XMLStreamException e) {
                logger.warn("Error while closing", e);
            }
        }
    }

    /**
     * Closes the given reader without throwing an exception on failure.
     * Instead, a warning will be logged.
     *
     * @param reader the writer to close.
     */
    public static void closeQuietly(XMLStreamReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                logger.warn("Error while closing", e);
            }
        }
    }

    /**
     * Copies the subtree that the reader is currently position at to the
     * given stream. The reader must be positioned at a START_ELEMENT
     * event, and when finished, will be positioned on the corresponding
     * END_ELEMENT event.
     * <p>
     * <b>NOTE:</b> Even if successful, this does not currently ensure that
     * the output has all used namespaces declared. In particular, if a
     * namespace was previously declared in the XML that the reader saw,
     * then used within the scope of the elements being copied but no
     * re-declared within them, the output will not be self-contained.
     *
     * @param source the reader whose subtree should be copied.
     * @param sink the stream to write to (UTF-8 encoding will be used).
     *        It will be left open when finished.
     * @throws XMLStreamException if anything goes wrong during the copy.
     */
    public static void copy(XMLStreamReader source, OutputStream sink)
            throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(sink, "UTF-8");
        copy(source, writer);
        writer.flush();
    }

    /**
     * Copies the subtree that the reader is currently position at to the
     * given writer. The reader must be positioned at a START_ELEMENT
     * event, and when finished, will be positioned on the corresponding
     * END_ELEMENT event.
     * <p>
     * <b>NOTE:</b> Even if successful, this does not currently ensure that
     * the output has all used namespaces declared. In particular, if a
     * namespace was previously declared in the XML that the reader saw,
     * then used within the scope of the elements being copied but no
     * re-declared within them, the output will not be self-contained.
     *
     * @param source the reader whose subtree should be copied.
     * @param sink the writer to copy to. It will be left open when finished.
     * @throws XMLStreamException if anything goes wrong during the copy.
     */
    public static void copy(XMLStreamReader source, XMLStreamWriter sink)
            throws XMLStreamException {
        sink.setNamespaceContext(source.getNamespaceContext());
        // TODO: Make a version of XMLStreamUtils that automatically writes
        // namespace declarations for any namespaces that aren't in it's
        // (new) context yet, but are in the reader's context (currently set
        // above, but it should probably not be...)
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
