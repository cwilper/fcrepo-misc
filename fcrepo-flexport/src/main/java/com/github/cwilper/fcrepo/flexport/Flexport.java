package com.github.cwilper.fcrepo.flexport;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import com.github.cwilper.fcrepo.dto.core.io.XMLUtil;
import com.github.cwilper.fcrepo.dto.foxml.FOXMLReader;
import com.github.cwilper.fcrepo.dto.foxml.FOXMLWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * A command-line utility for exporting selected objects and datastream
 * content from Fedora repositories.
 */
public class Flexport {

    /**
     * Command-line entry point.  This currently just reads a FOXML file
     * and writes it.  The default behavior is to pretty-print the output.
     * If a second argument is given, it will be canonicalized (c14n11)
     * instead.
     *
     * @param args one or two arguments as described above.
     * @throws Exception if anything goes wrong, java will print a stack trace.
     */
    public static void main(String[] args) throws Exception {
        FOXMLReader reader = new FOXMLReader();
        FOXMLWriter writer = new FOXMLWriter();
        try {
            FedoraObject obj = reader.readObject(new FileInputStream(new File(args[0])));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writer.writeObject(obj, out);
            byte[] pretty = XMLUtil.prettyPrint(out.toByteArray(), false);
            if (args.length == 1) {
                System.out.println(new String(pretty, "UTF-8"));
            } else {
                byte[] c14n = XMLUtil.canonicalize(pretty);
                System.out.println(new String(c14n, "UTF-8"));
            }
        } finally {
            reader.close();
            writer.close();
        }
    }
}
