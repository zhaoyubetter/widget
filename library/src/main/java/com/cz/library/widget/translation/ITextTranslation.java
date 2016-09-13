package com.cz.library.widget.translation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by czz on 2016/9/13.
 */
public abstract class ITextTranslation {
    protected View target;

    public ITextTranslation(View target) {
        this.target = target;
    }
    public abstract void preTranslation(String newValue, String oldValue);
    public abstract void drawFrame(Canvas canvas, Paint paint, float x, float y, String newValue, String oldValue);
}
