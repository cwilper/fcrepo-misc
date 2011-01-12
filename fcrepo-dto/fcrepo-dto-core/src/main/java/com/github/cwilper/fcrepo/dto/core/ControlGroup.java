package com.github.cwilper.fcrepo.dto.core;

/**
 * The Control Group of a <code>Datastream</code>.
 *
 * @see <a href="package-summary.html#working">Working With DTO Classes</a>
 */
public enum ControlGroup {

    /** E: Externally managed, but Fedora can dereference. */
    EXTERNAL("E"),

    /** M: Internally managed, can be stored outside of object XML. */
    MANAGED("M"),

    /** R: Externally managed, and Fedora should only redirect. */
    REDIRECT("R"),

    /** X: Internally managed, always stored inside object XML. */
    INLINE_XML("X");

    private final String shortName;

    /**
     * Creates an instance.
     *
     * @param shortName the abbreviated name often used in serializations.
     */
    ControlGroup(String shortName) {
        this.shortName = shortName;
    }

    /**
     * Gets the abbreviated name often used in serializations.
     *
     * @return the short name, never <code>null</code>.
     */
    public String shortName() {
        return shortName;
    }

    /**
     * Gets an instance from a short name.
     *
     * @param shortName the short name.
     * @return the corresponding instance.
     * @throws IllegalArgumentException if the short name is not recognized.
     */
    public static ControlGroup forShortName(String shortName) {
        for (ControlGroup c: ControlGroup.values()) {
            if (c.shortName().equals(shortName)) return c;
        }
        throw new IllegalArgumentException();
    }
}
