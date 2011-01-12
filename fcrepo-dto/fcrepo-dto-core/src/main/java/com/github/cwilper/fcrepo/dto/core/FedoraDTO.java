package com.github.cwilper.fcrepo.dto.core;

/**
 * Superclass of all (non-Enum) Fedora Data Transfer Object classes.
 * <p>
 * This superclass exists solely to provide useful and correct implementations
 * of <code>Object.hashCode()</code>, <code>Object.equals(Object)</code>,
 * and <code>Object.toString()</code>, requiring only that subclasses
 * implement {@link #getEqArray()}.
 */
abstract class FedoraDTO {

    /**
     * Gets the hash code for this instance. In accordance with the contract
     * for <code>Object.hashCode()</code>, this method is guaranteed to return
     * the same hash code for two objects when <code>o1.equals(o2)</code>
     *
     * @return the hash code.
     * @see #equals(Object)
     */
    @Override
    public final int hashCode() {
        int hash = 0;
        for (Object o: getEqArray()) {
            if (o != null) hash += o.hashCode();
        }
        return hash;
    }

    /**
     * Tells whether a given instance is considered equivalent to this one.
     * To be equivalent, the object must be an instance of the same class
     * and must have equivalent field values.
     *
     * @param o the object to compare with this one.
     * @return true if equal, false otherwise.
     */
    @Override
    public final boolean equals(Object o) {
        return o instanceof FedoraDTO && o.toString().equals(toString());
    }

    /**
     * Gets a human-readable description of this instance, which fully
     * describes its state. This isn't intended for normal end-user
     * consumption, but can be useful for logging, testing, and debugging.
     *
     * @return the description.
     */
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName() + "(");
        int i = 0;
        for (Object o: getEqArray()) {
            if (i > 0) sb.append(",");
            if (o instanceof String) {
                sb.append("\"" + o + "\"");
            } else {
                sb.append(o);
            }
            i++;
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Gets a comprehensive array of values that embody this instance's
     * state, for the purpose of equality testing.
     *
     * @return the array of values, never <code>null</code>.
     */
    abstract Object[] getEqArray();

}