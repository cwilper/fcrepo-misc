package com.github.cwilper.fcrepo.dto.core;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Utility methods for code in this package.
 */
abstract class Util {

    static int computeHash(Object... objects) {
        int hash = 0;
        for (Object o: objects) {
            if (o != null) hash += o.hashCode();
        }
        return hash;
    }

    static String normalize(String string) {
        if (string != null) {
            string = string.trim();
            if (string.length() == 0) {
                string = null;
            }
        }
        return string;
    }

    static Date copy(Date date) {
        if (date == null) return null;
        return new Date(date.getTime());
    }

    static byte[] getBytes(String in) {
        try {
            return in.getBytes("UTF-8");
        } catch (UnsupportedEncodingException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
    }

    static String getString(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
    }

}
