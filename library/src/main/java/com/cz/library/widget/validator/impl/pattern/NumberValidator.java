package com.cz.library.widget.validator.impl.pattern;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by czz on 2016/9/23.
 */
public class NumberValidator extends PatternValidator {
    private final Pattern pattern;
    public NumberValidator() {
        pattern=Pattern.compile("[0-9]+");
    }
    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
