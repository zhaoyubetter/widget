package com.cz.library.widget.selector;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorRes;

import com.cz.library.util.Utils;

/**
 * 常规点击状态选择器
 * <p>
 * Created by cz on 16/3/6.
 */
public class PressSelector implements Selector {

    private final Drawable emptyDrawable;
    private final Drawable pressDrawable;

    public PressSelector(@ColorRes int color, @ColorRes int pressColor) {
        this(new ColorDrawable(Utils.getColor(color)), new ColorDrawable(Utils.getColor(pressColor)));
    }

    public PressSelector(Drawable emptyDrawable, Drawable pressDrawable) {
        this.emptyDrawable = emptyDrawable;
        this.pressDrawable = pressDrawable;
    }

    @Override
    public int[][] state() {
        return new int[][]{SelectState.EMPTY, SelectState.PRESSED_ENABLED};
    }

    @Override
    public StateListDrawable create() {
        int[][] state = state();
        Drawable[] drawables = {emptyDrawable, pressDrawable};
        SelectorBuilder builder = new SelectorBuilder();
        for (int i = 0; i < state.length; i++) {
            builder.addState(state[i], drawables[i]);
        }
        return builder.build();
    }
}
