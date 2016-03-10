package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;

import com.cz.library.R;
import com.cz.library.util.Utils;

/**
 * Created by cz on 15/6/17.
 * 带指示器的线性布局
 * like
 * HORIZONTAL
 * ---------------------------
 * |  1  2  3  4  5  6  7  8  |
 * |  V1 V2 V3 V4 V5 V6 V7 V8 |
 * ---------------------------
 */
public class IndicateView extends DivideLinearLayout {
    public static final int TOP = 0;
    public static final int CENTER = 1;
    public static final int BOTTOM = 2;
    private Paint paint;
    private float textSize;//文字大小
    private int textColor;//文字颜色
    private float indicatePadding;//内边距
    private float dividerSize;//分隔线宽
    private int dividerColor;//分隔线颜色
    private float indicateSize;//指示器大小
    private int indicateBackground;
    private Drawable indicateDrawable;//指示器drawable
    private int gravity;

    @IntDef(value = {TOP, CENTER, BOTTOM})
    private @interface IndicateGravity {
    }

    public IndicateView(Context context) {
        this(context, null);
    }

    public IndicateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicateView);
        setTextColor(a.getColor(R.styleable.IndicateView_iv_textColor, Color.WHITE));
        setTextSize(a.getDimensionPixelSize(R.styleable.IndicateView_iv_textSize, Utils.sp2px(12)));
        setIndicatePadding(a.getDimension(R.styleable.IndicateView_iv_indicatePadding, 0));
        setDividerColor(a.getColor(R.styleable.IndicateView_iv_dividerColor, Color.DKGRAY));
        setDividerSize(a.getDimension(R.styleable.IndicateView_iv_dividerSize, Utils.dip2px(1)));
        setIndicateImage(a.getDrawable(R.styleable.IndicateView_iv_indicateImage));
        setIndicateSize(a.getDimension(R.styleable.IndicateView_iv_indicateSize, Utils.dip2px(40)));
        setIndicateBackground(a.getColor(R.styleable.IndicateView_iv_indicateBackground, Color.TRANSPARENT));
        this.gravity = a.getInt(R.styleable.IndicateView_iv_indicateGravity, TOP);
        a.recycle();
    }


    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setTextColor(int color) {
        this.textColor = color;
        invalidate();
    }

    public void setIndicatePadding(float padding) {
        this.indicatePadding = padding;
        invalidate();
    }

    public void setDividerSize(float size) {
        this.dividerSize = size;
        invalidate();
    }

    public void setDividerColor(int color) {
        this.dividerColor = color;
        invalidate();
    }

    /**
     * 设置指示器颜色
     *
     * @param color
     */
    public void setIndicateColor(int color) {
        setIndicateImage(new ColorDrawable(color));
    }

    public void setIndicateImage(Drawable drawable) {
        if (null == drawable) return;
        this.indicateDrawable = drawable;
        invalidate();
    }

    private void setIndicateSize(float size) {
        this.indicateSize = size;
        invalidate();
    }

    public void setIndicateBackground(int color) {
        this.indicateBackground = color;
        invalidate();
    }

    /**
     * 设置指示器方向
     *
     * @param gravity
     */
    public void setIndicateGravity(@IndicateGravity int gravity) {
        this.gravity = gravity;
        invalidate();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //根据方向移动整体控件位置
        int childCount = getChildCount();
        int orientation = getOrientation();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
            if (VERTICAL == orientation) {
                layoutParams.leftMargin = (int) indicateSize;
            } else if (HORIZONTAL == orientation) {
                layoutParams.topMargin = (int) indicateSize;
            }
        }
        requestLayout();
    }

    private float getStartX() {
        float startX = 0;
        if (VERTICAL == getOrientation()) {
            startX = indicateSize / 2;
        }
        return startX;
    }

    private float getStartY() {
        float startY = 0;
        if (HORIZONTAL == getOrientation()) {
            startY = indicateSize / 2;
        }
        return startY;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘分隔线
        float x = getStartX(), y = getStartY();//绘制线的起点,x,y
        drawDivide(canvas, x, y);
        for (int i = 0; i < getChildCount(); i++) {
            drawText(canvas, x, y, i);
        }

    }

    /**
     * 绘分隔线,背景
     *
     * @param canvas
     * @param x
     * @param y
     */
    private void drawDivide(Canvas canvas, float x, float y) {
        int width = getWidth();
        int height = getHeight();
        int orientation = getOrientation();
        paint.setColor(indicateBackground);
        paint.setStrokeWidth(dividerSize);
        if (VERTICAL == orientation) {
            canvas.drawRect(0, 0, indicateSize, height, paint);
            paint.setColor(dividerColor);
            canvas.drawLine(x, y, x, height, paint);
        } else if (HORIZONTAL == orientation) {
            canvas.drawRect(0, 0, width, indicateSize, paint);
            paint.setColor(dividerColor);
            canvas.drawLine(x, y, width, y, paint);
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas
     * @param x
     * @param y
     * @param index
     */
    private void drawText(Canvas canvas, float x, float y, int index) {
        //绘背景
        Rect textRect = new Rect();//文字绘制矩阵
        Rect outRect = new Rect();
        View childView = getChildAt(index);
        childView.getHitRect(outRect);
        int orientation = getOrientation();
        String text = String.valueOf(index + 1);
        paint.setTextSize(textSize);//在这里设置,使计算不出错
        paint.getTextBounds(text, 0, text.length(), textRect);
        float textWidth = paint.measureText(text);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float centerBaselineY = textRect.height() / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
        float radius = (indicateSize - indicatePadding - paint.getStrokeWidth() / 2) / 2;
        float textLeft = 0, textTop = 0, circleX = 0, circleY = 0;
        if (VERTICAL == orientation) {
            switch (gravity) {
                case TOP:
                    textTop = outRect.top + radius + centerBaselineY / 2;
                    circleY = outRect.top + radius;
                    break;
                case CENTER:
                    textTop = outRect.centerY() + centerBaselineY / 2;
                    circleY = outRect.centerY();
                    break;
                case BOTTOM:
                    textTop = outRect.bottom - radius + centerBaselineY / 2;
                    circleY = outRect.bottom - radius;
                    break;
            }
            textLeft = x - textWidth / 2;
            circleX = x;
        } else if (HORIZONTAL == orientation) {
            switch (gravity) {
                case TOP:
                    textLeft = outRect.left + radius - textWidth / 2;
                    circleX = outRect.left + radius;
                    break;
                case CENTER:
                    textLeft = outRect.centerX() - textWidth / 2;
                    circleX = outRect.centerX();
                    break;
                case BOTTOM:
                    textLeft = outRect.right - radius - textWidth / 2;
                    circleX = outRect.right - radius;
                    break;
            }
            textTop = y + textRect.height() / 2;
            circleY = y;
        }
        if (null != indicateDrawable) {
            indicateDrawable.setBounds((int) (circleX - radius),
                    (int) (circleY - radius),
                    (int) (circleX + radius),
                    (int) (circleY + radius));
            indicateDrawable.draw(canvas);
        }
        paint.setColor(textColor);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText(text, textLeft, textTop, paint);
    }


}
