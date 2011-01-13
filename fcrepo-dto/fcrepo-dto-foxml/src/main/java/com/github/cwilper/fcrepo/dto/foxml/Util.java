package com.github.cwilper.fcrepo.dto.foxml;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/** Package-private utility methods. */
abstract class Util {

    static final String PREFERRED_DATE_FORMAT =
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    static final String[] ALLOWED_DATE_FORMATS = new String[] {
            PREFERRED_DATE_FORMAT,
            "yyyy-MM-dd'T'HH:mm:ss.SS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.S'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SS",
            "yyyy-MM-dd'T'HH:mm:ss.S",
            "yyyy-MM-dd'T'HH:mm:ss"
    };

    static Date toDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (String format: ALLOWED_DATE_FORMATS) {
            dateFormat.applyPattern(format);
            try {
                return dateFormat.parse(dateString);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    static String toString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(PREFERRED_DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

}
