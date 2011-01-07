package com.github.cwilper.fcrepo.dto.core.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class XMLUtilTest {

    @Test
    public void canonicalizeSimple() throws Exception {
        assertCanonical("<simple/>", "<simple></simple>");
    }

    @Test
    public void canonicalizeComplex() throws Exception {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + " <!-- leading --> \n"
                + "<doc xmlns=\"urn:test\">\n"
                + "  <!-- Here is a comment -->\n"
                + "  <element\n"
                + "      ATTRIBUTE=\"value \"/>\n"
                + "</doc> <!-- trailing --> ";
        String expectedOutput = "<doc xmlns=\"urn:test\">\n"
                + "  \n"
                + "  <element ATTRIBUTE=\"value \"></element>\n"
                + "</doc>";
        assertCanonical(input, expectedOutput);
    }

    private static void assertCanonical(String input, String expectedOutput)
            throws IOException {
        Assert.assertEquals(expectedOutput, new String(XMLUtil.canonicalize(
                input.getBytes("UTF-8")), "UTF-8"));
    }
    
}
