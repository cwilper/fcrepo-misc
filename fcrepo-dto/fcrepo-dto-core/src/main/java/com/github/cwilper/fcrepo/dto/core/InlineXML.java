package com.github.cwilper.fcrepo.dto.core;

import com.github.cwilper.fcrepo.dto.core.io.XMLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The inline XML content of a <code>DatastreamVersion</code>.
 * <p>
 * <h2>Canonicalization and Normalization</h2>
 * The XML given at construction time must be well-formed.  It will be
 * automatically <i>canonicalized</i> according to
 * <a href="http://www.w3.org/TR/xml-c14n11/">Canonical XML Version 1.1</a>
 *(c14n11). In the rare event that the XML is well-formed but canonicalization
 * is impossible, (for example, if a namespace declaration in the XML has a
 * relative URI) it will be <i>normalized</i> using the following non-standard
 * algorithm:
 * <ol>
 *   <li> Leading and trailing whitespace will be removed.</li>
 *   <li> All XML declaration, processing instructions, and doctype
 *        declarations will be removed. This ensures it can be
 *        embedded within an XML document.</li>
 *   <li> It will be reformatted to have two-space indents.</li>
 *   <li> Attributes will use double-quotes around values.</li>
 *   <li> All empty elements (e.g.
 *        <code>&lt;element&gt;&lt;element&gt;</code>) will be
 *        collapsed (e.g. <code>&lt;element/&gt;</code></li>
 * </ol>
 * If standard canonicalization (c14n11) is successful at construction time,
 * {@link #canonical()} will always return <code>true</code>.
 */
public class InlineXML extends FedoraDTO {

    private static final Logger logger =
            LoggerFactory.getLogger(InlineXML.class);

    private final String value;

    private byte[] bytes;
    private boolean canonical;

    /**
     * Creates an instance from a string.
     *
     * @param value a well-formed, standalone XML document.
     * @throws IOException if the value is not a well-formed, standalone
     *         XML document.
     */
    public InlineXML(String value) throws IOException {
        this(Util.getBytes(value));
    }

    /**
     * Creates an instance from a UTF-8 encoded byte array.
     *
     * @param bytes a well-formed, standalone XML document.
     * @throws IOException if the value is not a well-formed, standalone
     *         XML document or cannot be decoded as UTF-8.
     */
    public InlineXML(byte[] bytes) throws IOException {
        // verify it's well-formed first; canonicalization doesn't
        byte[] normalized = XMLUtil.prettyPrint(bytes, true);
        // try to canonicalize, otherwise, use normalized form
        try {
            this.bytes = XMLUtil.canonicalize(bytes);
            this.canonical = true;
        } catch (IOException e) {
            logger.warn("Unable to canonicalize (c14n11); using non-standard "
                    + "normalization instead", e);
            this.bytes = normalized;
        }
        this.value = Util.getString(this.bytes);
    }

    /**
     * Gets the canonicalized (or normalized) XML as a string.
     *
     * @return the value, never <code>null</code>.
     */
    public String value() {
        return value;
    }

    /**
     * Gets the canonicalized (or normalized) XML as a UTF-8 byte array.
     *
     * @return the value, never <code>null</code>.
     */
    public byte[] bytes() {
        return bytes;
    }

    /**
     * Tells whether the XML is in canonical (c14n11) form.
     *
     * @return true if it was successfully converted to c14n11 form at
     *         construction time, false otherwise. If false, the caller can
     *         assume the XML is in the non-standard normalized form
     *         described above.
     */
    public boolean canonical() {
        return canonical;
    }

    @Override
    protected Object[] getEqArray() {
        return new Object[] { value };
    }

}
