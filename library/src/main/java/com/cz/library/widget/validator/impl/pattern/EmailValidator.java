package com.cz.library.widget.validator.impl.pattern;

import android.util.Patterns;

import com.cz.library.widget.validator.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by czz on 2016/9/23.
 */
public class EmailValidator extends PatternValidator {
    @Override
    public Pattern getPattern() {
        return Patterns.EMAIL_ADDRESS;
    }
}
