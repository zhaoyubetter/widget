package com.cz.library.widget.validator.impl.pattern;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/23.
 */
public class CharacterValidator extends PatternValidator {
    private final Pattern pattern;
    public CharacterValidator() {
        pattern=Pattern.compile("[a-zA-Z]+");
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
