package com.github.cwilper.fcrepo.riclient;

import com.google.common.collect.AbstractIterator;
import org.apache.http.protocol.HTTP;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.rio.ntriples.NTriplesUtil;

import javax.annotation.PreDestroy;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RIQueryResult extends AbstractIterator<List<Value>> {

    private final BufferedReader reader;

    private final ValueFactory factory;

    private boolean exhausted;

    RIQueryResult(InputStream in) {
        try {
            reader = new BufferedReader(new InputStreamReader(in, HTTP.UTF_8));
        } catch (UnsupportedEncodingException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
        factory = new ValueFactoryImpl();
        exhausted = false;
    }

    @Override
    protected List<Value> computeNext() {
        if (exhausted) {
            return endOfData();
        } else {
            List<Value> values = new ArrayList<Value>();
            try {
                String line = reader.readLine();
                while (line != null && line.length() > 0) {
                    values.add(parse(line));
                    line = reader.readLine();
                }
                if (values.size() == 0) {
                    return endOfData();
                } else if (line == null) {
                    exhausted = true;
                    close();
                }
                return values;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Parse a query result value of the form "name : NTriplesValue"
    private Value parse(String line) {
        int i = line.indexOf(" : ");
        if (i == -1) {
            throw new IllegalArgumentException("Malformed line in RI Query "
                    + "result (expected ' : ' delimiter): '" + line + "'");
        } else {
            return NTriplesUtil.parseValue(line.substring(i + 3), factory);
        }
    }

    @PreDestroy
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}