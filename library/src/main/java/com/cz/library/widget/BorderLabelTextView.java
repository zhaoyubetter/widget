package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cz.library.R;
import com.cz.library.util.Utils;


/**
 * Created by author on 15/12/28.
 * 一个带边框线,及提示的TextView对象
 * like:
 * --  label --
 * |   TEXT   |
 * ------------
 */
public class BorderLabelTextView extends TextView {
    private int textColor;
    private float textSize;
    private int borderColor;
    private float borderSize;
    private float leftPadding;
    private float borderPadding;
    private float labelPadding;
    private String text;
    private Paint paint;

    public BorderLabelTextView(Context context) {
        this(context, null, 0);
    }

    public BorderLabelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderLabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BorderLabelTextView);
        setTextColor(a.getColor(R.styleable.BorderLabelTextView_bt_textColor, Color.BLACK));
        setTextSize(a.getDimensionPixelSize(R.styleable.BorderLabelTextView_bt_textSize, 12));
        setLeftLabelPadding(a.getDimension(R.styleable.BorderLabelTextView_bt_leftLabelPadding, 0));
        setBorderColor(a.getColor(R.styleable.BorderLabelTextView_bt_borderColor, Color.DKGRAY));
        setBorderSize(a.getDimension(R.styleable.BorderLabelTextView_bt_borderSize, getResources().getDimension(R.dimen.divideSize)));
        setBorderPadding(a.getDimension(R.styleable.BorderLabelTextView_bt_borderPadding, 0));
        setLabelPadding(a.getDimension(R.styleable.BorderLabelTextView_bt_labelPadding, Utils.dip2px(8)));
        setLabel(a.getString(R.styleable.BorderLabelTextView_bt_label));
        a.recycle();
    }

    public void setLabelPadding(float padding) {
        this.labelPadding = padding;
        invalidate();
    }

    /**
     * 设置标签
     *
     * @param text
     */
    public void setLabel(String text) {
        this.text = text;
        invalidate();
    }

    /**
     * 设置标签
     *
     * @param res
     */
    public void setLabel(@StringRes int res) {
        if (NO_ID != res) {
            setLabel(getResources().getString(res));
        }
    }

    public void setBorderColor(int mBorderColor) {
        this.borderColor = mBorderColor;
        invalidate();
    }

    public void setBorderSize(float mBorderSize) {
        this.borderSize = mBorderSize;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setLeftLabelPadding(float leftPadding) {
        this.leftPadding = leftPadding;
        invalidate();
    }

    /**
     * 设置分隔内边距
     *
     * @param padding
     */
    public void setBorderPadding(float padding) {
        this.borderPadding = padding;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘文字
        float textWidth = 0, textHeight = 0;
        if (!TextUtils.isEmpty(text)) {
            paint.setColor(textColor);
            paint.setTextSize(textSize);
            textWidth = paint.measureText(text);//文字宽
            Rect textRect = new Rect();
            paint.getTextBounds(text, 0, text.length(), textRect);
            Paint.FontMetrics fm = paint.getFontMetrics();
            textHeight = textRect.height() / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
            canvas.drawText(text, leftPadding + borderPadding, textHeight + borderPadding, paint);
        }
        //绘分隔线,float类型,会造成分隔线计算大小不一致
        drawDivide(canvas, (int) textWidth, (int) textHeight, (int) leftPadding);
    }

    /**
     * 绘制分隔线
     *
     * @param canvas
     */
    private void drawDivide(Canvas canvas, int textWidth, int textHeight, int leftPadding) {
        paint.reset();
        int width = getWidth();
        int height = getHeight();
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderSize);
        float interval = borderSize / 2;
        float halfTextHeight = (textHeight / 2);
        canvas.drawLine(interval + borderPadding, halfTextHeight + borderPadding, interval + borderPadding, height - borderPadding, paint);//左侧线
        canvas.drawLine(width - interval - borderPadding, borderPadding + halfTextHeight, width - interval - borderPadding, height - borderPadding, paint);//右侧
        canvas.drawLine(borderPadding, height - interval - borderPadding, width - borderPadding, height - interval - borderPadding, paint);//底部

        canvas.drawLine(borderPadding, interval + halfTextHeight + borderPadding, borderPadding + leftPadding - labelPadding, interval + halfTextHeight + borderPadding, paint);//顶部左边线
        canvas.drawLine(borderPadding + leftPadding + textWidth + labelPadding, interval + halfTextHeight + borderPadding, width - borderPadding, interval + halfTextHeight + borderPadding, paint);//顶部右边线
    }
}
