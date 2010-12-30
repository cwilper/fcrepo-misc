package com.github.cwilper.fcrepo.dto.core;

public enum ControlGroup {
    
    EXTERNAL_REFERENCE("E"),
    MANAGED_CONTENT("M"),
    REDIRECT("R"),
    INLINE_XML("X");

    private final String shortName;

    ControlGroup(String shortName) {
        this.shortName = shortName;
    }

    public String shortName() {
        return shortName;
    }

    public static ControlGroup forShortName(String shortName) {
        for (ControlGroup c: ControlGroup.values()) {
            if (c.shortName().equals(shortName)) return c;
        }
        throw new IllegalArgumentException();
    }
}
