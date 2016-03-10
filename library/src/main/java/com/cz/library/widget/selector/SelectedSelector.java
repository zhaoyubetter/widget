package com.cz.library.widget.selector;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorRes;

import com.cz.library.util.Utils;

/**
 * 常规选择状态选择器
 * <p>
 * Created by cz on 16/3/6.
 */
public class SelectedSelector implements Selector {

    private final Drawable emptyDrawable;
    private final Drawable selectDrawable;

    public SelectedSelector(@ColorRes int color, @ColorRes int selectColor) {
        this(new ColorDrawable(Utils.getColor(color)), new ColorDrawable(Utils.getColor(selectColor)));
    }

    public SelectedSelector(Drawable emptyDrawable, Drawable selectDrawable) {
        this.emptyDrawable = emptyDrawable;
        this.selectDrawable = selectDrawable;
    }

    @Override
    public int[][] state() {
        return new int[][]{SelectState.EMPTY, SelectState.ENABLED_SELECTED};
    }

    @Override
    public StateListDrawable create() {
        int[][] state = state();
        Drawable[] drawables = {emptyDrawable, emptyDrawable};
        SelectorBuilder builder = new SelectorBuilder();
        for (int i = 0; i < state.length; i++) {
            builder.addState(state[i], drawables[i]);
        }
        return builder.build();
    }
}
