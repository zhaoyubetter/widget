package com.cz.library.widget.validator.impl.pattern;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/23.
 */
public class WebUrlValidator extends PatternValidator {
    @Override
    public Pattern getPattern() {
        return Patterns.WEB_URL;
    }
}
