package com.cz.library.widget.drawable;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntDef;

/**
 * Created by cz on 16/3/7.
 */
public class GradientBuilder {
    private static final String TAG = "GradientBuilder";
    private final int gradientType;
    private final DrawableBuilder gradientDrawable;
    private int gradientAngle;
    private float gradientRadius;
    private boolean useLevel;
    private float gradientCenterX;
    private float gradientCenterY;
    private int gradientStartColor;
    private int gradientCenterColor;
    private int gradientEndColor;

    @IntDef(value = {GradientDrawable.LINEAR_GRADIENT, GradientDrawable.RADIAL_GRADIENT, GradientDrawable.SWEEP_GRADIENT})
    public @interface GradientType {
    }

    GradientBuilder(DrawableBuilder drawableBuilder, @GradientType int gradientType) {
        this.gradientDrawable = drawableBuilder;
        this.gradientType = gradientType;
    }

    public GradientBuilder setGradientAngle(int gradientAngle) {
        this.gradientAngle = gradientAngle;
        return this;
    }

    public GradientBuilder setGradientCenterColor(int gradientCenterColor) {
        this.gradientCenterColor = gradientCenterColor;
        return this;
    }

    public GradientBuilder setCenterX(float gradientCenterX) {
        this.gradientCenterX = gradientCenterX;
        return this;
    }

    public GradientBuilder setCenterY(float gradientCenterY) {
        this.gradientCenterY = gradientCenterY;
        return this;
    }

    public GradientBuilder setGradientEndColor(int gradientEndColor) {
        this.gradientEndColor = gradientEndColor;
        return this;
    }

    public GradientBuilder setGradientRadius(float gradientRadius) {
        this.gradientRadius = gradientRadius;
        return this;
    }

    public GradientBuilder setGradientStartColor(int gradientStartColor) {
        this.gradientStartColor = gradientStartColor;
        return this;
    }


    public GradientBuilder setUseLevel(boolean useLevel) {
        this.useLevel = useLevel;
        return this;
    }

    private GradientDrawable.Orientation getOrientation(int angle) {
        angle %= 360;
        GradientDrawable.Orientation orientation;
        switch (angle) {
            default:
            case 0:
                orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                break;
            case 45:
                orientation = GradientDrawable.Orientation.BL_TR;
                break;
            case 90:
                orientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case 135:
                orientation = GradientDrawable.Orientation.BR_TL;
                break;
            case 180:
                orientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
            case 225:
                orientation = GradientDrawable.Orientation.TR_BL;
                break;
            case 270:
                orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
            case 315:
                orientation = GradientDrawable.Orientation.TL_BR;
                break;
        }
        return orientation;
    }

    GradientDrawable create() {
        GradientDrawable drawable = new GradientDrawable(getOrientation(gradientAngle), new int[]{gradientStartColor, gradientCenterColor, gradientEndColor});
        drawable.setGradientType(gradientType);
        drawable.setGradientRadius(gradientRadius);
        drawable.setUseLevel(useLevel);
        drawable.setGradientCenter(gradientCenterX, gradientCenterY);
        return drawable;
    }

    public GradientDrawable build() {
        return gradientDrawable.build();
    }
}
