package com.github.cwilper.fcrepo.flexport;

import com.github.cwilper.fcrepo.dto.core.FedoraObject;
import com.github.cwilper.fcrepo.dto.core.io.XMLUtil;
import com.github.cwilper.fcrepo.dto.foxml.FOXMLReader;
import com.github.cwilper.fcrepo.dto.foxml.FOXMLWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class Flexport {

    public static void main(String[] args) throws Exception {
        FOXMLReader reader = new FOXMLReader();
        FOXMLWriter writer = new FOXMLWriter();
        try {
            FedoraObject obj = reader.readObject(new FileInputStream(new File(args[0])));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writer.writeObject(obj, out);
            byte[] pretty = XMLUtil.prettyPrint(out.toByteArray(), false);
            System.out.println(new String(pretty, "UTF-8"));
        } finally {
            reader.close();
            writer.close();
        }
    }
}
