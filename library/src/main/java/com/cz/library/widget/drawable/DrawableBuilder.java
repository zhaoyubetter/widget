package com.cz.library.widget.drawable;

import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntDef;

import com.cz.library.annotation.UnitDp;
import com.cz.library.util.ReflectUtils;
import com.cz.library.util.Utils;

/**
 * 创建drawable
 * <p/>
 * Created by cz on 16/3/7.
 */
public class DrawableBuilder {
    private static final String TAG = "DrawableBuilder";
    private int shape;//当前创建图形
    private int solidColor;//填充颜色
    private int strokeWidth;//边线大小
    private int strokeColor;//边线颜色
    private int dashWidth;
    private int dashGap;
    private int width;
    private int height;
    private int leftPadding, topPadding, rightPadding, bottomPadding;
    private GradientBuilder gradientBuilder;

    @IntDef(value = {GradientDrawable.RECTANGLE, GradientDrawable.OVAL, GradientDrawable.LINE, GradientDrawable.RING})
    public @interface Shape {
    }

    protected DrawableBuilder(@Shape int shape) {
        this.shape = shape;
    }

    public static OvalBuilder oval() {
        return new OvalBuilder();
    }

    public static LineBuilder line() {
        return new LineBuilder();
    }

    public static RectDrawableBuilder rect() {
        return new RectDrawableBuilder();
    }

    public static RingBuilder ring() {
        return new RingBuilder();
    }

    public GradientBuilder setGradientType(@GradientBuilder.GradientType int gradientType) {
        gradientBuilder = new GradientBuilder(this, gradientType);
        return gradientBuilder;
    }

    public DrawableBuilder setSolidColor(int solidColor) {
        this.solidColor = solidColor;
        return this;
    }

    public DrawableBuilder setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public DrawableBuilder setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public DrawableBuilder setDashGap(@UnitDp int dashGap) {
        this.dashGap = Utils.dip2px(dashGap);
        return this;
    }

    public DrawableBuilder setDashWidth(@UnitDp int dashWidth) {
        this.dashWidth = Utils.dip2px(dashWidth);
        return this;
    }

    public DrawableBuilder setHeight(@UnitDp int height) {
        this.height = Utils.dip2px(height);
        return this;
    }

    public DrawableBuilder setWidth(@UnitDp int width) {
        this.width = Utils.dip2px(width);
        return this;
    }

    public DrawableBuilder setLeftPadding(int padding) {
        this.leftPadding = padding;
        return this;
    }

    public DrawableBuilder setTopPadding(int padding) {
        this.topPadding = padding;
        return this;
    }

    public DrawableBuilder setRightPadding(int padding) {
        this.rightPadding = padding;
        return this;
    }

    public DrawableBuilder setBottomPadding(int padding) {
        this.bottomPadding = padding;
        return this;
    }

    public DrawableBuilder setPadding(int padding) {
        this.leftPadding = padding;
        this.topPadding = padding;
        this.rightPadding = padding;
        this.bottomPadding = padding;
        return this;
    }

    public GradientDrawable build() {
        GradientDrawable drawable;
        if (null == gradientBuilder) {
            drawable = new GradientDrawable();
            drawable.setColor(solidColor);
        } else {
            drawable = gradientBuilder.create();
        }
        drawable.setShape(shape);
        drawable.setStroke(strokeWidth, strokeColor, dashGap, dashWidth);
        drawable.setSize(width, height);
        ReflectUtils.setValue(drawable, "mPadding", new Rect(leftPadding, topPadding, rightPadding, bottomPadding));
        return drawable;
    }
}
