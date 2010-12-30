package com.github.cwilper.fcrepo.dto.core;

abstract class Util {

    public static int computeHash(Object... objects) {
        int hash = 0;
        for (Object o: objects) {
            if (o != null) hash += o.hashCode();
        }
        return hash;
    }

}
