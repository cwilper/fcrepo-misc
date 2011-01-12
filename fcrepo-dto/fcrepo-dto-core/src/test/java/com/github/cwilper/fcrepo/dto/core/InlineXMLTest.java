package com.github.cwilper.fcrepo.dto.core;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Unit tests for <code>InlineXML</code>.
 */
public class InlineXMLTest extends FedoraDTOTest {

    @Override
    public Object[] getEqualInstances() {
        try {
            return new Object[] {
                    new InlineXML("<doc/>"),
                    new InlineXML("<doc></doc>")
            };
        } catch (IOException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
    }

    @Override
    public Object[] getNonEqualInstances() {
        try {
            return new Object[] {
                    new InlineXML("<doc/>"),
                    new InlineXML("<doctor/>")
            };
        } catch (IOException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
    }

    @Test (expected=IOException.class)
    public void emptyString() throws IOException {
        new InlineXML("");
    }

    @Test (expected=IOException.class)
    public void emptyBytes() throws IOException {
        new InlineXML(new byte[0]);
    }

    @Test (expected=IOException.class)
    public void malformedString() throws IOException {
        new InlineXML("<nonClosingElement>");
    }

    @Test (expected=IOException.class)
    public void malformedBytes() throws IOException {
        new InlineXML(Util.getBytes("<nonClosingElement>"));
    }

    @Test
    public void canonicalize() throws IOException {
        InlineXML xml;
        String expected = "<a b=\"c\"></a>";
        byte[] expectedBytes = Util.getBytes(expected);

        // empty elements should be expanded
        xml = new InlineXML("<a b=\"c\"/>");
        Assert.assertTrue(xml.canonical());
        Assert.assertEquals(expected, xml.value());
        Assert.assertArrayEquals(expectedBytes, xml.bytes());

        // comments and leading and trailing whitespace should be dropped
        xml = new InlineXML(" <!-- comment -->\n<a b='c'/>\n<!-- --> ");
        Assert.assertTrue(xml.canonical());
        Assert.assertEquals(expected, xml.value());
        Assert.assertArrayEquals(expectedBytes, xml.bytes());
    }

    @Test
    public void normalize() throws IOException {
        InlineXML xml;
        String expected = "<a xmlns=\"b\"/>";
        byte[] expectedBytes = Util.getBytes(expected);

        // not canonicalizable because namespace uri is relative. so:
        // quotes around attribute values should be changed to double-quotes,
        // empty elements should be collapsed, and
        // leading and trailing whitespace should be dropped
        xml = new InlineXML("  \n  <a xmlns='b'></a>\n  ");
        Assert.assertFalse(xml.canonical());
        Assert.assertEquals(expected, xml.value());
        Assert.assertArrayEquals(expectedBytes, xml.bytes());
    }
}
