package com.cz.library.annotation;

/**
 * Created by cz on 16/3/7.
 */

import android.support.annotation.IntDef;
import android.util.TypedValue;

@IntDef(value = {TypedValue.COMPLEX_UNIT_DIP, TypedValue.COMPLEX_UNIT_SP, TypedValue.COMPLEX_UNIT_PX})
public @interface UnitType {
}
