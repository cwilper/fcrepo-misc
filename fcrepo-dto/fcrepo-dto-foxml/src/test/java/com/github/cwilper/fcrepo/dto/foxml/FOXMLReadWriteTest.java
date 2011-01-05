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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Date;
import java.util.Set;

public class FOXMLReadWriteTest {

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
        writeThenReadCheck("objEmpty");
    }

    @Test
    public void objPid() {
        obj.pid("test:objPid");
        writeThenReadCheck("objPid");
    }

    @Test
    public void objStateActive() {
        obj.state(State.ACTIVE);
        writeThenReadCheck("objStateActive");
    }

    @Test
    public void objStateInactive() {
        obj.state(State.INACTIVE);
        writeThenReadCheck("objStateInactive");
    }

    @Test
    public void objStateDeleted() {
        obj.state(State.DELETED);
        writeThenReadCheck("objStateDeleted");
    }

    @Test
    public void objLabel() {
        obj.label("objLabel");
        writeThenReadCheck("objLabel");
    }

    @Test
    public void objOwnerId() {
        obj.ownerId("objOwnerId");
        writeThenReadCheck("objOwnerId");
    }

    @Test
    public void objCreatedDate() {
        obj.createdDate(new Date(0));
        writeThenReadCheck("objCreatedDate");
    }

    @Test
    public void objLastModifiedDate() {
        obj.lastModifiedDate(new Date(0));
        writeThenReadCheck("objLastModifiedDate");
    }

    @Test
    public void dsId() {
        obj.putDatastream(new Datastream("dsId"));
        writeThenReadCheck("dsId");
    }

    @Test
    public void dsStateActive() {
        obj.putDatastream(new Datastream("ds").state(State.ACTIVE));
        writeThenReadCheck("dsStateActive");
    }

    @Test
    public void dsStateInactive() {
        obj.putDatastream(new Datastream("ds").state(State.INACTIVE));
        writeThenReadCheck("dsStateInactive");
    }

    @Test
    public void dsStateDeleted() {
        obj.putDatastream(new Datastream("ds").state(State.DELETED));
        writeThenReadCheck("dsStateDeleted");
    }

    @Test
    public void dsControlGroupE() {
        obj.putDatastream(new Datastream("ds").controlGroup(
                ControlGroup.EXTERNAL_REFERENCE));
        writeThenReadCheck("dsControlGroupE");
    }

    @Test
    public void dsControlGroupM() {
        obj.putDatastream(new Datastream("ds").controlGroup(
                ControlGroup.MANAGED_CONTENT));
        writeThenReadCheck("dsControlGroupM");
    }

    @Test
    public void dsControlGroupR() {
        obj.putDatastream(new Datastream("ds").controlGroup(
                ControlGroup.REDIRECT));
        writeThenReadCheck("dsControlGroupR");
    }

    @Test
    public void dsControlGroupX() {
        obj.putDatastream(new Datastream("ds").controlGroup(
                ControlGroup.INLINE_XML));
        writeThenReadCheck("dsControlGroupX");
    }

    @Test
    public void dsVersionableTrue() {
        obj.putDatastream(new Datastream("ds").versionable(true));
        writeThenReadCheck("dsVersionableTrue");
    }

    @Test
    public void dsVersionableFalse() {
        obj.putDatastream(new Datastream("ds").versionable(false));
        writeThenReadCheck("dsVersionableFalse");
    }

    @Test
    public void dsMulti() {
        obj.putDatastream(new Datastream("ds2"));
        obj.putDatastream(new Datastream("ds1"));
        writeThenReadCheck("dsMulti");
    }

    @Test
    public void dsvId() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null);
        obj.putDatastream(ds);
        writeThenReadCheck("dsvId");
    }

    @Test
    public void dsvCreatedDate() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(new Date(0));
        obj.putDatastream(ds);
        writeThenReadCheck("dsvCreatedDate");
    }

    @Test
    public void dsvLabel() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).label("dsvLabel");
        obj.putDatastream(ds);
        writeThenReadCheck("dsvLabel");
    }

    @Test
    public void dsvMimeType() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).mimeType("dsvMimeType");
        obj.putDatastream(ds);
        writeThenReadCheck("dsvMimeType");
    }

    @Test
    public void dsvFormatURI() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).formatURI(URI.create("urn:dsvFormatURI"));
        obj.putDatastream(ds);
        writeThenReadCheck("dsvFormatURI");
    }

    @Test
    public void dsvSize() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).size(0L);
        obj.putDatastream(ds);
        writeThenReadCheck("dsvSize");
    }

    @Test
    public void dsvContentDigest() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).contentDigest(new ContentDigest().type("someType").
                hexValue("someHexValue"));
        obj.putDatastream(ds);
        writeThenReadCheck("dsvContentDigest");
    }

    @Test
    public void dsvInlineXML() throws IOException {
        Datastream ds = new Datastream("ds").controlGroup(
                ControlGroup.INLINE_XML);
        // TODO: Use a more complex inline xml document...with namespaces, etc
        ds.addVersion(null).setInlineXML(new StringReader("<doc/>"));
        obj.putDatastream(ds);
        writeThenReadCheck("dsvInlineXML");
    }

    @Test
    public void dsvBinaryContent() throws IOException {
        final String content = "A word starting with c, followed by o, "
                + "followed by n, followed by t, followed by e, "
                + "followed by n, and ending with t.";
        File tempFile = File.createTempFile("fcrepo-dto-test", null);
        FileOutputStream sink = null;
        try {
            //
            // Write test
            //
            sink = new FileOutputStream(tempFile);
            IOUtils.copy(new StringReader(content),
                    new FileOutputStream(tempFile));
            IOUtils.closeQuietly(sink);
            Datastream ds = new Datastream("ds").controlGroup(
                    ControlGroup.MANAGED_CONTENT);
            ds.addVersion(null).contentLocation(tempFile.toURI());
            DatastreamVersion origDSV = ds.versions().first();
            obj.putDatastream(ds);
            // serialize obj to FOXML, requesting that the managed content is
            // embedded as binaryContent (base64), and ensure the resulting
            // XML is exactly as expected.
            writeCheck("dsvBinaryContent", obj.datastreams().keySet());
            //
            // Read test
            //
            FOXMLReader reader = new FOXMLReader();
            FedoraObject result = readCheck("dsvBinaryContent", reader, false);
            DatastreamVersion resultDSV = result.datastreams().get("ds").versions().first();
            // should have new file: contentLocation
            URI resultLocation = resultDSV.contentLocation();
            Assert.assertNotNull(resultLocation);
            Assert.assertFalse(resultLocation.equals(origDSV.contentLocation()));
            Assert.assertEquals(resultLocation.getScheme(), "file");
            // ...whose content matches that of the original
            File resultFile = new File(resultLocation.getRawSchemeSpecificPart());
            Assert.assertTrue(resultFile.exists());
            FileInputStream resultIn = new FileInputStream(resultFile);
            String resultContent = IOUtils.toString(resultIn, "UTF-8");
            IOUtils.closeQuietly(resultIn);
            Assert.assertEquals(resultContent, content);
            // other than the contentLocation, the newly-read object should
            // be equivalent to the original one
            resultDSV.contentLocation(origDSV.contentLocation());
            Assert.assertEquals(obj, result);
            // when the reader is closed, it should delete resultFile
            reader.close();
            Assert.assertFalse(resultFile.exists());
        } finally {
            IOUtils.closeQuietly(sink);
            tempFile.delete();
        }
    }

    @Test
    public void dsvContentLocation() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).contentLocation(URI.create("http://example.org/"));
        obj.putDatastream(ds);
        writeThenReadCheck("dsvContentLocation");
    }

    @Test
    public void dsvContentLocationInternal() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).contentLocation(
                URI.create("internal:test:obj+ds+ds.0"));
        obj.putDatastream(ds);
        writeThenReadCheck("dsvContentLocationInternal");
    }

    @Test
    public void dsvAltIds() {
        Datastream ds = new Datastream("ds");
        ds.addVersion(null).altIds().add(URI.create("urn:a"));
        obj.putDatastream(ds);
        writeThenReadCheck("dsvAltIds");
    }

    @Test
    public void dsvAltIdsMulti() {
        Datastream ds = new Datastream("ds");
        DatastreamVersion dsv = ds.addVersion(null);
        dsv.altIds().add(URI.create("urn:c"));
        dsv.altIds().add(URI.create("urn:a"));
        dsv.altIds().add(URI.create("urn:b"));
        obj.putDatastream(ds);
        writeThenReadCheck("dsvAltIdsMulti");
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
        writeThenReadCheck("dsvMulti");
    }

    private void writeThenReadCheck(String testName) {
        writeCheck(testName, null);
        readCheck(testName, new FOXMLReader(), true);
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

    private FedoraObject readCheck(String testName, FOXMLReader reader,
                                   boolean check) {
        String filename = "foxml/" + testName + ".xml";
        try {
            InputStream in = getClass().getClassLoader()
                    .getResourceAsStream(filename);
            FedoraObject result = reader.readObject(in);
            if (check) {
                Assert.assertEquals(obj, result);
            }
            return result;
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
