package com.github.cwilper.fcrepo.riclient;

import com.github.cwilper.ttff.AbstractSource;
import org.apache.http.protocol.HTTP;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.rio.ntriples.NTriplesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * The result of a Resource Index query.
 */
public class RIQueryResult
        extends AbstractSource<List<Value>> {

    private static final Logger logger =
            LoggerFactory.getLogger(RIQueryResult.class);

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
    protected List<Value> computeNext() throws IOException {
        if (exhausted) {
            return endOfData();
        } else {
            List<Value> values = new ArrayList<Value>();
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
        }
    }

    @PreDestroy
    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            logger.warn("Error closing reader", e);
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
}