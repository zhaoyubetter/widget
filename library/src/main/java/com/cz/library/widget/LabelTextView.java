package com.cz.library.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import com.cz.library.R;
import com.cz.library.util.Utils;


/**
 * 显示增加一端显示文字的textView
 * <p/>
 * Created by cz on 14/9/3.
 * like:
 * ------------------
 * |TEXT   otherText|
 * ------------------
 * update 2016/2/15,增加方向设定
 */
public class LabelTextView extends DivideTextView {
    private static final int DEFAULT_TEXT_SIZE = Utils.sp2px(15);
    private int labelSize;
    private int labelColor;
    private float labelPadding;
    private int labelGravity;
    private boolean defaultSet;
    private String label;
    private Paint paint;

    public LabelTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);
        setLabelSize(a.getDimensionPixelSize(R.styleable.LabelTextView_bv_labelSize, DEFAULT_TEXT_SIZE));
        setLabelColor(a.getColorStateList(R.styleable.LabelTextView_bv_labelColor));
        setLabelColor(a.getColor(R.styleable.LabelTextView_bv_labelColor, Color.BLACK));
        setLabelPadding(a.getDimension(R.styleable.LabelTextView_bv_labelPadding, 0));
        setLabel(a.getString(R.styleable.LabelTextView_bv_label));
        setBothDefault(a.getBoolean(R.styleable.LabelTextView_bv_labelDefault, true));
        setLabelGravity(a.getInt(R.styleable.LabelTextView_bv_labelGravity, Gravity.RIGHT));
        a.recycle();
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelTextView(Context context) {
        this(context, null, 0);
    }

    /**
     * 设置标题展示方法
     *
     * @param gravity
     * @see android.view.Gravity#LEFT,android.view.Gravity#RIGHT,android.view.Gravity#TOP,android.view.Gravity#BOTTOM,android.view.Gravity#CENTER
     */
    public void setLabelGravity(int gravity) {
        this.labelGravity = gravity;
        invalidate();
    }


    /**
     * 设置右边文字边距
     *
     * @param padding
     */
    public void setLabelPadding(float padding) {
        this.labelPadding = padding;
    }

    /**
     * 设置左边文字颜色
     *
     * @param color
     */
    public void setLabelColor(int color) {
        this.labelColor = color;
        invalidate();
    }

    public void setLabelColor(ColorStateList colorStateList) {
        if (null != colorStateList) {
        }
    }

    /**
     * 设置右边文字字体大小
     *
     * @param textSize
     */
    public void setLabelSize(int textSize) {
        this.labelSize = textSize;
        invalidate();
    }

    /**
     * 设置标题
     *
     * @param res
     */
    public void setLabel(@StringRes int res) {
        this.label = getContext().getString(res);
        invalidate();
    }

    /**
     * 设置标签
     *
     * @param text
     */
    public void setLabel(String text) {
        this.label = text;
        invalidate();
    }

    /**
     * 设置应用默认属性
     */
    private void setBothDefault(boolean isDefault) {
        this.defaultSet = isDefault;
    }

    /**
     * 获取文字
     *
     * @return
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * 设置画笔样式
     *
     * @param isDefault
     */
    private void initPaint(boolean isDefault) {
        TextPaint textPaint = getPaint();
        paint.reset();
        if (null != textPaint && isDefault) {
            paint.setColor(textPaint.getColor());
            paint.setTextSize(textPaint.getTextSize());
        } else {
            paint.setColor(labelColor);
            paint.setTextSize(labelSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(label)) return;
        int width = getWidth();
        int height = getHeight();
        initPaint(defaultSet);
        float textWidth = 0, textHeight = 0;
        CharSequence text = getText();
        if (!TextUtils.isEmpty(text)) {
            TextPaint paint = getPaint();
            textWidth = paint.measureText(text.toString());
            Rect bounds = new Rect();
            paint.getTextBounds(text.toString(), 0, text.length(), bounds);
            textHeight = bounds.height();
        }

        //标签宽
        float labelTextWidth = paint.measureText(label);//文字宽
        Rect textRect = new Rect();
        paint.getTextBounds(label, 0, label.length(), textRect);
        float centerX = (width - labelTextWidth) / 2;
        float centerY = (height - (paint.descent() + paint.ascent())) / 2;

        int drawableSize = 0;
        int gravity = getGravity();
        int drawablePadding = getCompoundDrawablePadding();
        Drawable[] compoundDrawables = getCompoundDrawables();
        switch (labelGravity) {
            case Gravity.LEFT:
                int paddingLeft = getPaddingLeft();
                Drawable leftDrawable = compoundDrawables[0];
                if (null != leftDrawable) {
                    drawableSize = leftDrawable.getIntrinsicWidth() + drawablePadding;
                }
                canvas.drawText(label, gravity == labelGravity ? textWidth : 0 + paddingLeft + drawableSize, centerY, paint);
                break;
            case Gravity.TOP:
                int paddingTop = getPaddingLeft();
                Drawable topDrawable = compoundDrawables[0];
                if (null != topDrawable) {
                    drawableSize = topDrawable.getIntrinsicHeight() + drawablePadding;
                }
                canvas.drawText(label, centerX, gravity == labelGravity ? textHeight : 0 + paddingTop + drawableSize, paint);
                break;
            case Gravity.RIGHT:
                int paddingRight = getPaddingLeft();
                Drawable rightDrawable = compoundDrawables[0];
                if (null != rightDrawable) {
                    drawableSize = rightDrawable.getIntrinsicWidth() + drawablePadding;
                }
                canvas.drawText(label, gravity == labelGravity ? textWidth : 0 + paddingRight + drawableSize, centerY, paint);
                break;
            case Gravity.BOTTOM:
                int paddingBottom = getPaddingLeft();
                Drawable bottomDrawable = compoundDrawables[0];
                if (null != bottomDrawable) {
                    drawableSize = bottomDrawable.getIntrinsicHeight() + drawablePadding;
                }
                canvas.drawText(label, centerX, gravity == labelGravity ? textHeight : 0 + paddingBottom + drawableSize, paint);
                break;
            case Gravity.CENTER:
                break;
            default:

                break;
        }
    }
}
