package com.github.cwilper.fcrepo.dto.core.io;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date-related utility methods.
 */
public abstract class DateUtil {

    /**
     * The preferred date format, output by {@link #toString(java.util.Date)}
     * and checked first for parsing via {@link #toDate(String)}.
     */
    public static final String PREFERRED_DATE_FORMAT =
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * Acceptable date formats for parsing via {@link #toDate(String)}.
     */
    public static final String[] ALLOWED_DATE_FORMATS = new String[] {
            PREFERRED_DATE_FORMAT,
            "yyyy-MM-dd'T'HH:mm:ss.SS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.S'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SS",
            "yyyy-MM-dd'T'HH:mm:ss.S",
            "yyyy-MM-dd'T'HH:mm:ss"
    };

    /**
     * Gets a UTC <code>Date</code> from a UTC ISO-8601 <code>String</code>.
     * <p>
     * The string should be in one of the {@link #ALLOWED_DATE_FORMATS},
     * ideally {@link #PREFERRED_DATE_FORMAT} (e.g.
     * <code>2011-01-16T08:27:01.002Z</code>).
     *
     * @param dateString the string to parse.
     * @return the date if parsing was successful, or <code>null</code> if
     *         if dateString was given as null or parsing was unsuccessful
     *         for any other reason.
     */
    public static Date toDate(String dateString) {
        if (dateString == null) return null;
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

    /**
     * Gets a UTC ISO-8601 <code>String</code> from a given <code>Date</code>.
     * <p>
     * The string will be in the {@link #PREFERRED_DATE_FORMAT}
     * (e.g. <code>2011-01-16T08:27:01.002Z</code>).
     *
     * @param date the date.
     * @return the string, or <code>null</code> if the date was given as null.
     */
    public static String toString(Date date) {
        if (date == null) return null;
        DateFormat dateFormat = new SimpleDateFormat(PREFERRED_DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

}
