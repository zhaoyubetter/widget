package com.cz.library.widget.selector;

import android.view.View;

import com.cz.library.util.ReflectUtils;

/**
 * Created by cz on 16/3/7.
 * StateListDrawable all State
 */
public class SelectState {
    public static final int[] EMPTY;
    public static final int[] ENABLED;
    public static final int[] FOCUSED;
    public static final int[] SELECTED;
    public static final int[] PRESSED;
    public static final int[] ENABLED_SELECTED;
    public static final int[] WINDOW_FOCUSED;
    public static final int[] ENABLED_FOCUSED;
    public static final int[] ENABLED_WINDOW_FOCUSED;
    public static final int[] FOCUSED_SELECTED;
    public static final int[] FOCUSED_WINDOW_FOCUSED;
    public static final int[] SELECTED_WINDOW_FOCUSED;
    public static final int[] ENABLED_FOCUSED_SELECTED;
    public static final int[] ENABLED_FOCUSED_WINDOW_FOCUSED;
    public static final int[] ENABLED_SELECTED_WINDOW_FOCUSED;
    public static final int[] FOCUSED_SELECTED_WINDOW_FOCUSED;
    public static final int[] ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED;
    public static final int[] PRESSED_WINDOW_FOCUSED;
    public static final int[] PRESSED_SELECTED;
    public static final int[] PRESSED_SELECTED_WINDOW_FOCUSED;
    public static final int[] PRESSED_FOCUSED;
    public static final int[] PRESSED_FOCUSED_WINDOW_FOCUSED;
    public static final int[] PRESSED_FOCUSED_SELECTED;
    public static final int[] PRESSED_FOCUSED_SELECTED_WINDOW_FOCUSED;
    public static final int[] PRESSED_ENABLED;
    public static final int[] PRESSED_ENABLED_WINDOW_FOCUSED;
    public static final int[] PRESSED_ENABLED_SELECTED;
    public static final int[] PRESSED_ENABLED_SELECTED_WINDOW_FOCUSED;
    public static final int[] PRESSED_ENABLED_FOCUSED_STATE_SET;
    public static final int[] PRESSED_ENABLED_FOCUSED_WINDOW_FOCUSED;
    public static final int[] PRESSED_ENABLED_FOCUSED_SELECTED;
    public static final int[] PRESSED_ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED;


    static {
        EMPTY = (int[]) ReflectUtils.getValue(null, View.class, "EMPTY_STATE_SET");
        ENABLED = (int[]) ReflectUtils.getValue(null, View.class, "ENABLED_STATE_SET");
        FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "FOCUSED_STATE_SET");
        SELECTED = (int[]) ReflectUtils.getValue(null, View.class, "SELECTED_STATE_SET");
        PRESSED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_STATE_SET");
        ENABLED_SELECTED = (int[]) ReflectUtils.getValue(null, View.class, "ENABLED_SELECTED_STATE_SET");
        WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "WINDOW_FOCUSED_STATE_SET");
        ENABLED_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "ENABLED_FOCUSED_STATE_SET");
        ENABLED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "ENABLED_WINDOW_FOCUSED_STATE_SET");
        FOCUSED_SELECTED = (int[]) ReflectUtils.getValue(null, View.class, "FOCUSED_SELECTED_STATE_SET");
        FOCUSED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "FOCUSED_WINDOW_FOCUSED_STATE_SET");
        SELECTED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "SELECTED_WINDOW_FOCUSED_STATE_SET");
        ENABLED_FOCUSED_SELECTED = (int[]) ReflectUtils.getValue(null, View.class, "ENABLED_FOCUSED_SELECTED_STATE_SET");
        ENABLED_FOCUSED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET");
        ENABLED_SELECTED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET");
        FOCUSED_SELECTED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET");
        ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET");
        PRESSED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_WINDOW_FOCUSED_STATE_SET");
        PRESSED_SELECTED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_SELECTED_STATE_SET");
        PRESSED_SELECTED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_SELECTED_WINDOW_FOCUSED_STATE_SET");
        PRESSED_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_FOCUSED_STATE_SET");
        PRESSED_FOCUSED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_FOCUSED_WINDOW_FOCUSED_STATE_SET");
        PRESSED_FOCUSED_SELECTED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_FOCUSED_SELECTED_STATE_SET");
        PRESSED_FOCUSED_SELECTED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET");
        PRESSED_ENABLED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_ENABLED_STATE_SET");
        PRESSED_ENABLED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_ENABLED_WINDOW_FOCUSED_STATE_SET");
        PRESSED_ENABLED_SELECTED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_ENABLED_SELECTED_STATE_SET");
        PRESSED_ENABLED_SELECTED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET");
        PRESSED_ENABLED_FOCUSED_STATE_SET = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_ENABLED_FOCUSED_STATE_SET_STATE_SET");
        PRESSED_ENABLED_FOCUSED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET");
        PRESSED_ENABLED_FOCUSED_SELECTED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_ENABLED_FOCUSED_SELECTED_STATE_SET");
        PRESSED_ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED = (int[]) ReflectUtils.getValue(null, View.class, "PRESSED_ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET");
    }
}
