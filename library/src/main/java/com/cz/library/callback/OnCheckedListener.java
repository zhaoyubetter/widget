package com.cz.library.callback;

import android.view.View;

/**
 * Created by cz on 16/3/7.
 */
public interface OnCheckedListener {

    /**
     * 选择监听
     *
     * @param v        当前点击控件
     * @param position 当前点击位置
     * @param value    当前控件值
     */
    void onChecked(View v, int position, CharSequence value);
}
