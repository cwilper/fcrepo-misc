package com.github.cwilper.fcrepo.dto.core;

/**
 * Utility methods for code in this package.
 */
abstract class Util {

    public static int computeHash(Object... objects) {
        int hash = 0;
        for (Object o: objects) {
            if (o != null) hash += o.hashCode();
        }
        return hash;
    }

}
