package com.github.cwilper.fcrepo.dto.core;

/**
 * The State of a {@link FedoraObject} or {@link Datastream}.
 *
 * @see <a href="package-summary.html#working">Working With DTO Classes</a>
 */
public enum State {

    /** [A]ctive. */
    ACTIVE("Active"),

    /** [I]nactive. */
    INACTIVE("Inactive"),

    /** [D]eleted. */
    DELETED("Deleted");

    private final String shortName;
    private final String longName;

    /**
     * Creates an instance.
     *
     * @param longName the full name sometimes used in serializations.
     */
    State(String longName) {
        this.shortName = longName.substring(0, 1);
        this.longName = longName;
    }

    /**
     * Gets the abbreviated name (A, I, or D) often used in serializations.
     *
     * @return the short name, never <code>null</code>.
     */
    public String shortName() {
        return shortName;
    }

    /**
     * Gets the long name (Active, Inactive, or Deleted) sometimes used in
     * serializations.
     *
     * @return the short name, never <code>null</code>.
     */
    public String longName() {
        return longName;
    }

    /**
     * Gets an instance from a short name.
     *
     * @param shortName the short name.
     * @return the corresponding instance.
     * @throws IllegalArgumentException if the short name is not recognized.
     */
    public static State forShortName(String shortName) {
        for (State s: State.values()) {
            if (s.shortName().equals(shortName)) return s;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Gets an instance from a long name.
     *
     * @param longName the long name.
     * @return the corresponding instance.
     * @throws IllegalArgumentException if the long name is not recognized.
     */
    public static State forLongName(String longName) {
        for (State s: State.values()) {
            if (s.longName().equals(longName)) return s;
        }
        throw new IllegalArgumentException();
    }
}
