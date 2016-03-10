package com.cz.library.widget.drawable;

import android.graphics.drawable.GradientDrawable;

/**
 * Created by cz on 16/3/6.
 */
public class RectDrawableBuilder extends DrawableBuilder {
    private float ltRadius;
    private float rtRadius;
    private float lbRadius;
    private float rbRadius;

    public RectDrawableBuilder() {
        super(GradientDrawable.RECTANGLE);
    }

    public RectDrawableBuilder setCornersRadius(float cornersRadius) {
        this.ltRadius = cornersRadius;
        this.rtRadius = cornersRadius;
        this.lbRadius = cornersRadius;
        this.rbRadius = cornersRadius;
        return this;
    }

    public RectDrawableBuilder setLeftTopRadius(float radius) {
        this.ltRadius = radius;
        return this;
    }

    public RectDrawableBuilder setRightTopRadius(float radius) {
        this.rtRadius = radius;
        return this;
    }

    public RectDrawableBuilder setLeftBottomRadius(float radius) {
        this.lbRadius = radius;
        return this;
    }

    public RectDrawableBuilder setRightBottomRadius(float radius) {
        this.rbRadius = radius;
        return this;
    }

    @Override
    public GradientDrawable build() {
        GradientDrawable drawable = super.build();
        drawable.setCornerRadii(new float[]{ltRadius, ltRadius,
                rtRadius, rtRadius,
                rbRadius, rbRadius,
                lbRadius, lbRadius});
        return drawable;
    }
}
