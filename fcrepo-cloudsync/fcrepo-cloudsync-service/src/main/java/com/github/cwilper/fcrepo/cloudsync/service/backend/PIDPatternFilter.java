package com.github.cwilper.fcrepo.cloudsync.service.backend;

import com.github.cwilper.ttff.AbstractFilter;

import java.util.regex.Pattern;

public class PIDPatternFilter extends AbstractFilter<String> {

    private final String pattern;

    public PIDPatternFilter(String pattern) {
        this.pattern = pattern;
    }

    public String accept(String value) {
        if (pattern == null
                || pattern.equals("*")
                || Pattern.matches(pattern, value) ) {
            return value;
        } else {
            return null;
        }
    }
}
