package com.github.cwilper.fcrepo.dto.core;

/**
 * The State of a <code>FedoraObject</code> or <code>Datastream</code>.
 */
public enum State {

    ACTIVE("Active"),
    INACTIVE("Inactive"),
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

    public String shortName() {
        return shortName;
    }

    public String longName() {
        return longName;
    }

    public static State forShortName(String shortName) {
        for (State s: State.values()) {
            if (s.shortName().equals(shortName)) return s;
        }
        throw new IllegalArgumentException();
    }

    public static State forLongName(String longName) {
        for (State s: State.values()) {
            if (s.longName().equals(longName)) return s;
        }
        throw new IllegalArgumentException();
    }
}
