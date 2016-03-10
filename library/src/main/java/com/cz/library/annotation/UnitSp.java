package com.cz.library.annotation;

import android.util.TypedValue;

/**
 * Created by cz on 16/3/7.
 */
public @interface UnitSp {
    int value() default TypedValue.COMPLEX_UNIT_SP;
}
