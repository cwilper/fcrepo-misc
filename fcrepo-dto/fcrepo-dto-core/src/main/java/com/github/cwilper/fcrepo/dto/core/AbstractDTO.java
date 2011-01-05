package com.github.cwilper.fcrepo.dto.core;

import java.io.UnsupportedEncodingException;

/**
 * Superclass of all (non-Enum) Fedora Data Transfer Object classes.
 *
 * This superclass exists solely to provide correct implementations of
 * <code>Object.hashCode()</code> and <code>Object.equals(Object)</code>,
 * requiring only that subclasses implement <code>getEqArray()</code>.
 */
abstract class AbstractDTO {

    @Override
    public final int hashCode() {
        int hash = 0;
        for (Object o: getEqArray()) {
            if (o != null) hash += o.hashCode();
        }
        return hash;
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof AbstractDTO && o.toString().equals(toString());
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName() + "(");
        int i = 0;
        for (Object o: getEqArray()) {
            if (i > 0) sb.append(",");
            if (o instanceof String) {
                sb.append("\"" + o + "\"");
            } else if (o instanceof byte[]) {
                byte[] bytes = (byte[]) o;
                try {
                    sb.append("\"" + new String(bytes, "UTF-8") + "\"");
                } catch (UnsupportedEncodingException e) {
                }
            } else {
                sb.append(o);
            }
            i++;
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Gets a comprehensive array of values that embody this object's state
     * for the purpose of equality testing.
     *
     * @return the array of values, never null.
     */
    abstract Object[] getEqArray();

}