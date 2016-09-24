package com.cz.library.widget.validator.impl;

import android.text.Editable;
import android.text.TextUtils;

import com.cz.library.widget.validator.Validator;

import java.util.regex.Matcher;

/**
 * Created by Administrator on 2016/9/23.
 */
public class NotEmptyValidator implements Validator {
    @Override
    public boolean validator(Editable value) {
        return !TextUtils.isEmpty(value)&&!TextUtils.isEmpty(value.toString().trim());
    }
}
