package com.cz.library.widget.selector;

import android.graphics.drawable.StateListDrawable;

/**
 * Created by cz on 16/3/7.
 */
public interface Selector {
    /**
     * set View State
     *
     * @return state array
     * @see @link{com.cz.library.widget.selector.SelectState}
     */
    int[][] state();

    /**
     * Create a StateListDrawable
     *
     * @return StateListDrawable
     */
    StateListDrawable create();
}
