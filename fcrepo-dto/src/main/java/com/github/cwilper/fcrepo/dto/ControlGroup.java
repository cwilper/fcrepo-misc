package com.github.cwilper.fcrepo.dto;

public enum ControlGroup {
    
    EXTERNAL_REFERENCE("E"),
    INLINE_XML("X"),
    MANAGED_CONTENT("M"),
    REDIRECT("R");

    private final String shortName;

    ControlGroup(String shortName) {
        this.shortName = shortName;
    }

    public String shortName() {
        return shortName;
    }

    public static State forShortName(String shortName) {
        for (State s: State.values()) {
            if (s.shortName().equals(shortName)) return s;
        }
        throw new IllegalArgumentException();
    }
}
