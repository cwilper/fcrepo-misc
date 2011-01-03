package com.github.cwilper.fcrepo.dto.core;

import java.util.Arrays;

/**
 * Superclass of all (non-Enum) Fedora Data Transfer Object classes.
 *
 * This superclass exists solely to provide correct implementations of
 * <code>Object.hashCode()</code> and <code>Object.equals(Object)</code>,
 * requiring only that subclasses implement <code>getEqArray()</code>.
 */
abstract class FedoraDTO {

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
        return o instanceof FedoraDTO && Arrays.equals(
                ((FedoraDTO) o).getEqArray(), getEqArray());
    }

    /**
     * Gets a comprehensive array of values that embody this object's state
     * for the purpose of equality testing.
     *
     * @return the array of values, never null.
     */
    abstract Object[] getEqArray();

}