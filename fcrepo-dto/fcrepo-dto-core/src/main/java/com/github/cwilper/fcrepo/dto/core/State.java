package com.github.cwilper.fcrepo.dto.core;

public enum State {

    ACTIVE("Active"),
    INACTIVE("Inactive"),
    DELETED("Deleted");

    private final String shortName;
    private final String longName;

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
