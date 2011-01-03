package com.github.cwilper.fcrepo.dto.foxml;

import com.github.cwilper.fcrepo.dto.core.ControlGroup;
import com.github.cwilper.fcrepo.dto.core.Datastream;
import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Date;
import java.util.Set;

public class FOXMLWriterTest {

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

    @Test
    public void simple() throws IOException {
        FedoraObject obj = new FedoraObject().pid("test:simple")
                .createdDate(new Date());

        Datastream ds1 = new Datastream("DS1")
                .controlGroup(ControlGroup.MANAGED_CONTENT);
        ds1.addVersion(null)
                .contentLocation(URI.create("file:///Users/cwilper/.vimrc"));
        obj.putDatastream(ds1);
        
        Datastream ds2 = new Datastream("DS2")
                .controlGroup(ControlGroup.INLINE_XML);
        ds2.addVersion(null)
                .setInlineXML(new StringReader("<doc>hi</doc>"));
        obj.putDatastream(ds2);

        System.out.println(getFOXML(obj, obj.datastreams().keySet()));
    }

    private static String getFOXML(FedoraObject obj,
                                   Set<String> embedIds) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            FOXMLWriter writer = new FOXMLWriter();
            writer.setManagedDatastreamsToEmbed(embedIds);
            writer.write(obj, out);
            return prettyPrint(new ByteArrayInputStream(out.toByteArray()));
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
