package com.cz.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cz.app.R;

/**
 * Created by cz on 16/3/9.
 */
public class ColorItem extends LinearLayout {

    public ColorItem(Context context) {
        this(context, null);
    }

    public ColorItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.color_item, this);
        setGravity(Gravity.CENTER_VERTICAL);
        TextView textView = (TextView) findViewById(R.id.text);
        View colorView = findViewById(R.id.view);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorItem);
        textView.setText(a.getString(R.styleable.ColorItem_ci_text));
        colorView.setBackgroundColor(a.getColor(R.styleable.ColorItem_ci_color, Color.DKGRAY));
        a.recycle();
    }

    public void setColor(int color) {
        findViewById(R.id.view).setBackgroundColor(color);
    }
}
