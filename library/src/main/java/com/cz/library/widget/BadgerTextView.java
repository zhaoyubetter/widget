package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cz.library.R;
import com.cz.library.util.Utils;


/**
 * Created by author on 16/02/25.
 */
public class BadgerTextView extends CenterTextView {
    private static final boolean DEBUG = false;
    public static final int L_T = 0x00;
    public static final int R_T = 0x01;
    public static final int L_B = 0x02;
    public static final int R_B = 0x03;

    private Drawable labelDrawable;
    private int textColor;
    private float textSize;
    private int horizontalPadding;
    private int verticalPadding;
    private float leftPadding;
    private float topPadding;
    private float rightPadding;
    private float bottomPadding;
    private int drawableWidth;
    private int drawableHeight;
    private int gravity;
    private String text;
    private Paint paint;
    private boolean enable;

    public BadgerTextView(Context context) {
        this(context, null, 0);
    }

    public BadgerTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        enable = true;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BadgerTextView);
        setBadgerDrawable(a.getResourceId(R.styleable.BadgerTextView_lt_badgerDrawable, NO_ID));
        setBadgerWidth((int) a.getDimension(R.styleable.BadgerTextView_lt_badgerWidth, 0));
        setBadgerHeight((int) a.getDimension(R.styleable.BadgerTextView_lt_badgerHeight, 0));
        setTextColor(a.getColor(R.styleable.BadgerTextView_lt_textColor, Color.DKGRAY));
        setTextSize(a.getDimensionPixelSize(R.styleable.BadgerTextView_lt_textSize, Utils.sp2px(12)));
        setHorizontalPadding((int) a.getDimension(R.styleable.BadgerTextView_lt_horizontalPadding, 0));
        setVerticalPadding((int) a.getDimension(R.styleable.BadgerTextView_lt_verticalPadding, 0));
        setTextPadding(a.getDimension(R.styleable.BadgerTextView_lt_textPadding, 0));
        setTextLeftPadding(a.getDimension(R.styleable.BadgerTextView_lt_textLeftPadding, 0));
        setTextTopPadding(a.getDimension(R.styleable.BadgerTextView_lt_textTopPadding, 0));
        setTextRightPadding(a.getDimension(R.styleable.BadgerTextView_lt_textRightPadding, 0));
        setTextBottomPadding(a.getDimension(R.styleable.BadgerTextView_lt_textBottomPadding, 0));
        setBadgerGravity(a.getInt(R.styleable.BadgerTextView_lt_badgerGravity, L_T));
        setBadgerText(a.getString(R.styleable.BadgerTextView_lt_badgerText));
        a.recycle();
    }

    /**
     * 设置文字四个方向边距
     *
     * @param padding
     */
    public void setTextPadding(float padding) {
        this.leftPadding = padding;
        this.topPadding = padding;
        this.rightPadding = padding;
        this.bottomPadding = padding;
        invalidate();
    }


    /**
     * 设置纵向内边距
     *
     * @param verticalPadding
     */
    public void setVerticalPadding(int verticalPadding) {
        this.verticalPadding = verticalPadding;
        invalidate();
    }

    public void setBadgerDrawable(@DrawableRes int labelDrawable) {
        this.labelDrawable = getResources().getDrawable(labelDrawable);
        invalidate();
    }

    public void setBadgerDrawable(Drawable drawable) {
        this.labelDrawable = drawable;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setBadgerText(String text) {
        this.text = text;
        invalidate();
    }

    /**
     * 设置横向内边距
     *
     * @param horizontalPadding
     */
    public void setHorizontalPadding(int horizontalPadding) {
        this.horizontalPadding = horizontalPadding;
        invalidate();
    }

    /**
     * 设置是否启用badger
     *
     * @param enable
     */
    public void setBadgerEnable(boolean enable) {
        this.enable = enable;
        invalidate();
    }

    /**
     * 设置绘制方向
     *
     * @param gravity
     */
    public void setBadgerGravity(int gravity) {
        this.gravity = gravity;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setTextLeftPadding(float padding) {
        this.leftPadding = padding;
        invalidate();
    }

    public void setTextTopPadding(float padding) {
        this.topPadding = padding;
        invalidate();
    }

    public void setTextRightPadding(float padding) {
        this.rightPadding = padding;
        invalidate();
    }

    public void setTextBottomPadding(float padding) {
        this.bottomPadding = padding;
        invalidate();
    }

    public void setBadgerWidth(int width) {
        this.drawableWidth = width;
        invalidate();
    }

    public void setBadgerHeight(int height) {
        this.drawableHeight = height;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!enable) return;
        if (TextUtils.isEmpty(text)) {
            //如果没有文字,只画背景
            drawDrawable(canvas, drawableWidth, drawableHeight, 0, 0);
        } else {
            //如果有文字,是背景附带文字,加文字内边距
            paint.setTextSize(textSize);
            float textWidth = paint.measureText(text);
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int textHeight = bounds.height() * 2;
            int drawableWidth = (int) (textWidth + leftPadding + rightPadding);
            int drawableHeight = (int) (textHeight + topPadding + bottomPadding);
            if (textWidth < textHeight) {
                drawableWidth = textHeight;
            }
            drawDrawable(canvas, drawableWidth, drawableHeight, textWidth, bounds.height());
        }

    }

    /**
     * 绘制一个drawable
     *
     * @param canvas
     */
    private void drawDrawable(Canvas canvas, int drawableWidth, int drawableHeight, float textWidth, float textHeight) {
        if (null != labelDrawable) {
            int width = getWidth();
            int height = getHeight();
            Rect rect;
            switch (gravity) {
                case L_B:
                    rect = new Rect(horizontalPadding, height - drawableHeight - verticalPadding, drawableWidth + horizontalPadding, height - verticalPadding);
                    break;
                case R_T:
                    rect = new Rect(width - horizontalPadding - drawableWidth, verticalPadding, width - horizontalPadding, drawableHeight + verticalPadding);
                    break;
                case R_B:
                    rect = new Rect(width - horizontalPadding - drawableWidth, height - drawableHeight - verticalPadding, width - horizontalPadding, height - verticalPadding);
                    break;
                case L_T:
                default:
                    rect = new Rect(horizontalPadding, verticalPadding, drawableWidth + horizontalPadding, drawableHeight + verticalPadding);
                    break;
            }
            labelDrawable.setBounds(rect);
            labelDrawable.draw(canvas);
            if (!TextUtils.isEmpty(text)) {
                paint.setColor(textColor);
                Paint.FontMetrics fm = paint.getFontMetrics();
                float centerVerticalBaselineY = rect.height() / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
                canvas.drawText(text, rect.left + (drawableWidth - textWidth) / 2, rect.top + centerVerticalBaselineY, paint);
                if (DEBUG) {
                    paint.setColor(Color.BLACK);
                    float rectWidth = rect.width();
                    float rectHeight = rect.height();
                    canvas.drawLine(rect.left, rect.top + rectHeight / 2, rect.right, rect.top + rectHeight / 2, paint);
                    canvas.drawLine(rect.left + rectWidth / 2, rect.top, rect.left + rectWidth / 2, rect.bottom, paint);
                }
            }
        }
    }
}
