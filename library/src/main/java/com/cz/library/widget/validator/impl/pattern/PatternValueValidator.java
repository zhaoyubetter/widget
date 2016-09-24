package com.cz.library.widget.validator.impl.pattern;

import android.text.Editable;
import android.text.TextUtils;

import com.cz.library.widget.validator.Validator;

import java.util.regex.Pattern;

/**
 * Created by czz on 2016/9/23.
 */
public  class PatternValueValidator extends PatternValidator {
    private final Pattern pattern;

    public PatternValueValidator(String patternValue) {
        this.pattern = Pattern.compile(patternValue);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
