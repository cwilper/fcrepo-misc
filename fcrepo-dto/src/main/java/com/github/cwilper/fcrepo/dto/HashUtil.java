package com.github.cwilper.fcrepo.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

abstract class Util {

    public static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static int computeHash(Object... objects) {
        int hash = 0;
        for (Object o: objects) {
            if (o != null) hash += o.hashCode();
        }
        return hash;
    }

    public static String toString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(ISO8601_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }
}
