package com.github.cwilper.fcrepo.dto.foxml;

import com.github.cwilper.fcrepo.dto.core.ControlGroup;
import com.github.cwilper.fcrepo.dto.core.Datastream;
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
            writer.write(obj, out);
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
