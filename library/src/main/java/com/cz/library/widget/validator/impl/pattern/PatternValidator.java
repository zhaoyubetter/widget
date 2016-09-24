package com.cz.library.widget.validator.impl.pattern;

import android.text.Editable;
import android.text.TextUtils;

import com.cz.library.widget.validator.Validator;

import java.util.regex.Pattern;

/**
 * Created by czz on 2016/9/23.
 */
public abstract class PatternValidator implements Validator {

    public abstract Pattern getPattern();

    @Override
    public boolean validator(Editable value) {
        Pattern pattern = getPattern();
        return !TextUtils.isEmpty(value)&&pattern.matcher(value).matches();
    }
}
