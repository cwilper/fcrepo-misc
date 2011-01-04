package com.github.cwilper.fcrepo.dto.foxml;

import com.github.cwilper.fcrepo.dto.core.ContentDigest;
import com.github.cwilper.fcrepo.dto.core.ControlGroup;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.DatastreamVersion;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import com.github.cwilper.fcrepo.dto.core.State;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Date;
import java.util.Set;

public class FOXMLWriterTest {

    // when test files are missing, if this is defined, they'll be generated
    private static File testResources =
            new File("/Users/cwilper/work/cwilper/fcrepo-misc/"
                    + "fcrepo-dto/fcrepo-dto-foxml/src/test/resources");

    final static String prettyXSL = "<xsl:stylesheet version=\"1.0\"\n" +
            " xmlns:xalan=\"http://xml.apache.org/xalan\"\n" +
            " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
            "<xsl:output method=\"xml\" indent=\"yes\"\n" +
            " xalan:indent-amount=\"2\"/>\n" +
            "<xsl:strip-space elements=\"*\"/>\n" +
            "<xsl:template match=\"/\">\n" +
            "  <xsl:copy-of select=\".\"/>\n" +
            "</xsl:template>\n" +
            "</xsl:stylesheet>";

    private FedoraObject obj;

    @Before
    public void setUp() {
        obj = new FedoraObject();
    }

    @Test
    public void objEmpty() {
        writeCheck("objEmpty");
    }

    @Test
    public void objPid() {
        obj.pid("test:objPid");
        writeCheck("objPid");
    }

    @Test
    public void objStateActive() {
        obj.state(State.ACTIVE);
        writeCheck("objStateActive");
    }

    @Test
    public void objStateInactive() {
        obj.state(State.INACTIVE);
        writeCheck("objStateInactive");
    }

    @Test
    public void objStateDeleted() {
        obj.state(State.DELETED);
        writeCheck("objStateDeleted");
    }

    @Test
    public void objLabel() {
        obj.label("objLabel");
        writeCheck("objLabel");
    }

    @Test
    public void objOwnerId() {
        obj.ownerId("objOwnerId");
        writeCheck("objOwnerId");
    }

    @Test
    public void objCreatedDate() {
        obj.createdDate(new Date(0));
        writeCheck("objCreatedDate");
    }

    @Test
    public void objLastModifiedDate() {
        obj.lastModifiedDate(new Date(0));
        writeCheck("objLastModifiedDate");
    }

    @Test
    public void dsId() {
        obj.putDatastream(new Datastream("dsId"));
        writeCheck("dsId");
    }

    @Test
    public void dsStateActive() {
        obj.putDatastream(new Datastream("ds").state(State.ACTIVE));
        writeCheck("dsStateActive");
    }

    @Test
    public void dsStateInactive() {
        obj.putDatastream(new Datastream("ds").state(State.INACTIVE));
        writeCheck("dsStateInactive");
    }

    @Test
    public void dsStateDeleted() {
        obj.putDatastream(new Datastream("ds").state(State.DELETED));
        writeCheck("dsStateDeleted");
    }

    @Test
    public void dsControlGroupE() {
        obj.putDatastream(new Datastream("ds").controlGroup(
                ControlGroup.EXTERNAL_REFERENCE));
        writeCheck("dsControlGroupE");
    }

    @Test
    public void dsControlGroupM() {
        obj.putDatastream(new Datastream("ds").controlGroup(
                ControlGroup.MANAGED_CONTENT));
        writeCheck("dsControlGroupM");
    }

    @Test
    public void dsControlGroupR() {
        obj.putDatastream(new Datastream("ds").controlGroup(
                ControlGroup.REDIRECT));
        writeCheck("dsControlGroupR");
    }

    @Test
    public void dsControlGroupX() {
        obj.putDatastream(new Datastream("ds").controlGroup(
                ControlGroup.INLINE_XML));
        writeCheck("dsControlGroupX");
    }

    @Test
    public void dsVersionableTrue() {
        obj.putDatastream(new Datastream("ds").versionable(true));
        writeCheck("dsVersionableTrue");
    }

    @Test
    public void dsVersionableFalse() {
        obj.putDatastream(new Datastream("ds").versionable(false));
        writeCheck("dsVersionableFalse");
    }

    @Test
    public void dsMulti() {
        obj.putDatastream(new Datastream("ds2"));
        obj.putDatastream(new Datastream("ds1"));
        writeCheck("dsMulti");
    }

    @Test
    public void dsvId() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null);
        obj.putDatastream(ds);
        writeCheck("dsvId");
    }

    @Test
    public void dsvCreatedDate() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(new Date(0));
        obj.putDatastream(ds);
        writeCheck("dsvCreatedDate");
    }

    @Test
    public void dsvLabel() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).label("dsvLabel");
        obj.putDatastream(ds);
        writeCheck("dsvLabel");
    }

    @Test
    public void dsvMimeType() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).mimeType("dsvMimeType");
        obj.putDatastream(ds);
        writeCheck("dsvMimeType");
    }

    @Test
    public void dsvFormatURI() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).formatURI(URI.create("urn:dsvFormatURI"));
        obj.putDatastream(ds);
        writeCheck("dsvFormatURI");
    }

    @Test
    public void dsvSize() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).size(0L);
        obj.putDatastream(ds);
        writeCheck("dsvSize");
    }

    @Test
    public void dsvContentDigest() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).contentDigest(new ContentDigest().type("someType").
                hexValue("someHexValue"));
        obj.putDatastream(ds);
        writeCheck("dsvContentDigest");
    }

    @Test
    public void dsvInlineXML() throws IOException {
        Datastream ds = new Datastream("ds").controlGroup(
                ControlGroup.INLINE_XML);
        ds.addVersion(null).setInlineXML(new StringReader("<doc/>"));
        obj.putDatastream(ds);
        writeCheck("dsvInlineXML");
    }

    @Test
    public void dsvContentLocation() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).contentLocation(URI.create("http://example.org/"));
        obj.putDatastream(ds);
        writeCheck("dsvContentLocation");
    }

    @Test
    public void dsvContentLocationInternal() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).contentLocation(
                URI.create("internal:test:obj+ds+ds.0"));
        obj.putDatastream(ds);
        writeCheck("dsvContentLocationInternal");
    }

    @Test
    public void dsvAltIds() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).altIds().add(URI.create("urn:a"));
        obj.putDatastream(ds);
        writeCheck("dsvAltIds");
    }

    @Test
    public void dsvAltIdsMulti() {
        Datastream ds = new Datastream("ds");
        DatastreamVersion dsv = ds.addVersion(null);
        dsv.altIds().add(URI.create("urn:c"));
        dsv.altIds().add(URI.create("urn:a"));
        dsv.altIds().add(URI.create("urn:b"));
        obj.putDatastream(ds);
        writeCheck("dsvAltIdsMulti");
    }

    @Test
    public void dsvMulti() {
        Datastream ds = new Datastream("ds");
        DatastreamVersion dsv1 = new DatastreamVersion("ds.1", null);
        DatastreamVersion dsv2 = new DatastreamVersion("ds.2", null);
        DatastreamVersion dsv3 = new DatastreamVersion("ds.3", new Date(2));
        DatastreamVersion dsv4 = new DatastreamVersion("ds.4", new Date(1));
        ds.versions().add(dsv4);
        ds.versions().add(dsv2);
        ds.versions().add(dsv1);
        ds.versions().add(dsv3);
        // should be in this order: 2, 1, 3, 4
        obj.putDatastream(ds);
        writeCheck("dsvMulti");
    }

    private void writeCheck(String testName) {
        writeCheck(testName, null);
    }

    private void writeCheck(String testName, Set<String> embedIds) {
        String filename = "foxml/" + testName + ".xml";
        try {
            InputStream actualStream = new ByteArrayInputStream(
                    getFOXML(embedIds).getBytes("UTF-8"));
            String actualXML = prettyPrint(actualStream);
            if (getClass().getClassLoader().getResource(filename) == null) {
                if (testResources != null && testResources.isDirectory()) {
                    File testFile = new File(testResources, filename);
                    testFile.getParentFile().mkdirs();
                    OutputStream out = new FileOutputStream(testFile);
                    IOUtils.copy(new StringReader(actualXML), out);
                    IOUtils.closeQuietly(out);
                    System.out.println("Generated test resource: " + filename);
                } else {
                    Assert.fail("Test resource not found: " + filename);
                }
            } else {
                InputStream expectedStream = getClass().getClassLoader()
                        .getResourceAsStream(filename);
                String expectedXML = prettyPrint(expectedStream);
                Assert.assertEquals(expectedXML, actualXML);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFOXML(Set<String> embedIds) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            FOXMLWriter writer = new FOXMLWriter();
            writer.setManagedDatastreamsToEmbed(embedIds);
            writer.writeObject(obj, out);
            return prettyPrint(new ByteArrayInputStream(out.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    private static String prettyPrint(InputStream xml) {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer t = tFactory.newTransformer(
                    new StreamSource(new StringReader(prettyXSL)));
            StringWriter result = new StringWriter();
            t.transform(new StreamSource(xml), new StreamResult(result));
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(xml);
        }
    }

}
