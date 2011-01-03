package com.github.cwilper.fcrepo.dto.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for <code>State</code>.
 */
public class StateTest {

    @Test
    public void threePossibleValues() {
        assertEquals(3, State.values().length);
    }

    @Test
    public void expectedShortNames() {
        assertEquals("A", State.ACTIVE.shortName());
        assertEquals("I", State.INACTIVE.shortName());
        assertEquals("D", State.DELETED.shortName());
    }

    @Test
    public void expectedLongNames() {
        assertEquals("Active", State.ACTIVE.longName());
        assertEquals("Inactive", State.INACTIVE.longName());
        assertEquals("Deleted", State.DELETED.longName());
    }

    @Test
    public void getByShortNames() {
        assertEquals(State.ACTIVE, State.forShortName("A"));
        assertEquals(State.INACTIVE, State.forShortName("I"));
        assertEquals(State.DELETED, State.forShortName("D"));
    }
    
    @Test
    public void getByLongNames() {
        assertEquals(State.ACTIVE, State.forLongName("Active"));
        assertEquals(State.INACTIVE, State.forLongName("Inactive"));
        assertEquals(State.DELETED, State.forLongName("Deleted"));
    }

    @Test (expected=IllegalArgumentException.class)
    public void bogusShortName() {
        State.forShortName("Bogus");
    }

    @Test (expected=IllegalArgumentException.class)
    public void bogusLongName() {
        State.forLongName("Bogus");
    }

}
