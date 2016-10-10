package com.cz.library.widget.card;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;


/**
 * a oval shadow
 */
public class OvalShadowDrawable extends ShapeDrawable implements IShadowDrawable{
    private static final int FILL_SHADOW_COLOR = 0x3D000000;
    private static final int SHADOW_COLOR = 0x1E000000;
    private int shadowRadius;
    private int diameter;

    public OvalShadowDrawable() {
        super();
    }

    @Override
    public void setBackgroundColor(int color){
        getPaint().setColor(color);
        invalidateSelf();
    }
    @Override
    public void setShadowRadius(int radius){
        this.shadowRadius=radius;
        setShape(new OvalShadow(radius, diameter));
    }
    @Override
    public void setElevation(float elevation){
        getPaint().setShadowLayer(shadowRadius, 0, elevation, SHADOW_COLOR);
    }

    static class OvalShadow extends OvalShape {
        private RadialGradient mRadialGradient;
        private int mShadowRadius;
        private Paint mShadowPaint;
        private int mCircleDiameter;

        public OvalShadow(int shadowRadius, int circleDiameter) {
            super();
            mShadowPaint = new Paint();
            mShadowRadius = shadowRadius;
            mCircleDiameter = circleDiameter;
            mRadialGradient = new RadialGradient(mCircleDiameter / 2, mCircleDiameter / 2,
                    mShadowRadius, new int[]{
                    FILL_SHADOW_COLOR, Color.TRANSPARENT
            }, null, Shader.TileMode.CLAMP);
            mShadowPaint.setShader(mRadialGradient);
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            final float viewWidth = getWidth();
            final float viewHeight = getHeight();
            canvas.drawCircle(viewWidth / 2, viewHeight / 2, (mCircleDiameter / 2 + mShadowRadius), mShadowPaint);
            canvas.drawCircle(viewWidth / 2, viewHeight / 2, (mCircleDiameter / 2), paint);
        }
    }
}