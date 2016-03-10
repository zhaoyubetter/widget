package com.cz.library.widget.selector;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * 创建控件状态选择器
 * <p>
 * Created by cz on 16/3/7.
 */
public class SelectorBuilder {

    private final StateListDrawable stateListDrawable;


    public SelectorBuilder() {
        stateListDrawable = new StateListDrawable();
    }

    /**
     * add StateListDrawable state
     *
     * @param state SelectState
     * @param color drawable color
     * @see @link{com.cz.library.widget.selector.SelectState}
     */
    public SelectorBuilder addState(int[] state, int color) {
        stateListDrawable.addState(state, new ColorDrawable(color));
        return this;
    }

    /**
     * add StateListDrawable state
     *
     * @param state    SelectState
     * @param drawable state drawable
     * @see @link{com.cz.library.widget.selector.SelectState}
     */
    public SelectorBuilder addState(int[] state, Drawable drawable) {
        stateListDrawable.addState(state, drawable);
        return this;
    }

    public StateListDrawable build() {
        return stateListDrawable;
    }

}
