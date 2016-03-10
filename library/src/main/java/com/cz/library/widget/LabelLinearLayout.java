package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cz.library.R;
import com.cz.library.util.Utils;


/**
 * Created by cz on 15/11/30.
 */
public class LabelLinearLayout extends DivideLinearLayout {

    public static final int LEFT = 0x01;
    public static final int TOP = 0x02;
    public static final int RIGHT = 0x04;
    public static final int BOTTOM = 0x08;
    public static final int CENTER = 0x10;

    private float textSize;
    private int textColor;
    private float horizontalPadding;
    private float verticalPadding;
    private int gravity;
    private String text;
    private Paint paint;

    public LabelLinearLayout(Context context) {
        this(context, null);
    }

    public LabelLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabelLinearLayout);
        setTextSize(a.getDimensionPixelSize(R.styleable.LabelLinearLayout_ll_textSize, Utils.sp2px(9)));
        setTextColor(a.getColor(R.styleable.LabelLinearLayout_ll_textColor, Color.DKGRAY));
        setHorizontalPadding(a.getDimension(R.styleable.LabelLinearLayout_ll_horizontalPadding, 0));
        setVerticalPadding(a.getDimension(R.styleable.LabelLinearLayout_ll_verticalPadding, 0));
        setGravity(a.getInt(R.styleable.LabelLinearLayout_ll_gravity, 0));
        a.recycle();
    }


    public void setHorizontalPadding(float padding) {
        this.horizontalPadding = padding;
        invalidate();
    }

    public void setVerticalPadding(float padding) {
        this.verticalPadding = padding;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas,
                LEFT == (LEFT | gravity),
                TOP == (TOP | gravity),
                RIGHT == (RIGHT | gravity),
                BOTTOM == (BOTTOM | gravity),
                CENTER == (CENTER | gravity));
    }

    /**
     * 绘制文字
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void drawText(Canvas canvas, boolean left, boolean top, boolean right, boolean bottom, boolean center) {
        if (TextUtils.isEmpty(text)) return;
        int width = getWidth();
        int height = getHeight();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        float textWidth = paint.measureText(text);//文字宽
        float x = horizontalPadding, y = 0;
        //横向
        if (left) {
            x = horizontalPadding;
        } else if (right) {
            x = width - horizontalPadding;
        } else if (center) {
            x = (width - textWidth) / 2;
        }
        float textHeight = -(paint.descent() + paint.ascent());
        //竖向
        if (top) {
            y = textHeight + verticalPadding;
        } else if (bottom) {
            y = height - verticalPadding;
        } else if (center) {
            y = (height + textHeight) / 2;
        }
        canvas.drawText(text, x, y, paint);
    }

}
