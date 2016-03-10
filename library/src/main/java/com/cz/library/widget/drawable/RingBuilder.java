package com.cz.library.widget.drawable;

import android.graphics.drawable.GradientDrawable;

import com.cz.library.util.ReflectUtils;

/**
 * Created by cz on 16/3/7.
 * 方法约束
 */
public class RingBuilder extends DrawableBuilder {
    private int innerRadiusRatio;
    private int innerRadius;
    private int thicknessRatio;
    private int thickness;

    public RingBuilder() {
        super(GradientDrawable.RING);
    }

    public RingBuilder setInnerRadiusRatio(int innerRadiusRatio) {
        this.innerRadiusRatio = innerRadiusRatio;
        return this;
    }

    public RingBuilder setThicknessRatio(int thicknessRatio) {
        this.thicknessRatio = thicknessRatio;
        return this;
    }

    public RingBuilder setInnerRadius(int innerRadius) {
        this.innerRadius = innerRadius;
        return this;
    }

    public RingBuilder setThickness(int thickness) {
        this.thickness = thickness;
        return this;
    }

    @Override
    public GradientDrawable build() {
        GradientDrawable drawable = super.build();
        Object gradientState = ReflectUtils.getValue(drawable, GradientDrawable.class, "mGradientState");
        if (null != gradientState) {
            ReflectUtils.setValue(gradientState, "mThickness", thickness);
            ReflectUtils.setValue(gradientState, "mThicknessRatio", thicknessRatio);
            ReflectUtils.setValue(gradientState, "mInnerRadiusRatio", innerRadiusRatio);
            ReflectUtils.setValue(gradientState, "mInnerRadius", innerRadius);
        }
        return drawable;
    }
}
