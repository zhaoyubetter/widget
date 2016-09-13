package com.cz.library.widget.translation;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by czz on 2016/9/13.
 */
public class TranslationTextTranslation extends ITextTranslation {
    private Rect textBounds;
    private float fraction;

    public TranslationTextTranslation(View target) {
        super(target);
        this.textBounds=new Rect();
    }

    @Override
    public void preTranslation(String newValue, String oldValue) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TranslationTextTranslation.this.fraction=valueAnimator.getAnimatedFraction();
                target.invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    public void drawFrame(Canvas canvas, Paint paint, float x, float y, String newValue, String oldValue) {
        paint.getTextBounds(newValue,0,newValue.length(),textBounds);
        int newTextHeight=textBounds.height();
        paint.getTextBounds(oldValue,0,oldValue.length(),textBounds);
        int oldTextHeight=textBounds.height();
        canvas.drawText(newValue,x,y-newTextHeight*(1f-fraction),paint);
        canvas.drawText(oldValue,x,y+oldTextHeight*fraction,paint);
    }

}
