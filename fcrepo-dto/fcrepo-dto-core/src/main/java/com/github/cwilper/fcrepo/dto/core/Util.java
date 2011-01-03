package com.github.cwilper.fcrepo.dto.core;

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

}
