package com.cz.library.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cz.library.R;


/**
 * 分隔线布局
 *
 * Created by cz on 2014/8/8
 */
public class DivideTextView extends TextView {
    public static final int NONE = 0x01;
    public static final int LEFT = 0x02;
    public static final int TOP = 0x04;
    public static final int RIGHT = 0x08;
    public static final int BOTTOM = 0x10;

    private Drawable divideDrawable;
    private int strokeWidth;
    private int divideColor;
    private int dividePadding;
    private int leftPadding;//左下单独边距,项目内这块需要比较多大
    private int gravity;

    @IntDef({NONE, LEFT, TOP, RIGHT, BOTTOM})
    public @interface DivideGravity {
    }



    public DivideTextView(Context context) {
        this(context, null, 0);
    }

    public DivideTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DivideTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        initAttribute(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        Resources resources = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DivideTextView);
        setDivideGravityInner(a.getInt(R.styleable.DivideTextView_dt_divideGravity, NONE));
        setStrokeWidth(a.getDimension(R.styleable.DivideTextView_dt_divideSize, resources.getDimension(R.dimen.divideSize)));
        setDivideColor(a.getColor(R.styleable.DivideTextView_dt_divideDrawable, resources.getColor(R.color.divide)));
        setDividePadding(a.getDimension(R.styleable.DivideTextView_dt_dividePadding, 0f));
        setLeftPadding(a.getDimension(R.styleable.DivideTextView_dt_leftPadding, 0f));
        a.recycle();
    }



    public void setDivideGravity(@DivideGravity int gravity) {
        setDivideGravityInner(gravity);
    }

    public void setDivideGravityInner(int gravity) {
        this.gravity = gravity;
        invalidate();
    }

    /**
     * 设置分隔线宽
     *
     * @param strokeWidth
     */
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = Math.round(strokeWidth);
        invalidate();
    }


    public void setDivideColor(int color) {
        this.divideColor = color;
        invalidate();
    }

    /**
     * 设置绘制线边距
     *
     * @param padding
     */
    public void setDividePadding(float padding) {
        this.dividePadding = Math.round(padding);
        invalidate();
    }

    /**
     * 设置左边内边距
     *
     * @param padding
     */
    private void setLeftPadding(float padding) {
        this.leftPadding = Math.round(padding);
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // 绘制周边分隔线
        drawDivide(canvas);
    }

    private void drawDivide(Canvas canvas) {
        drawDivide(canvas, gravity == (gravity | LEFT),
                gravity == (gravity | TOP),
                gravity == (gravity | RIGHT),
                gravity == (gravity | BOTTOM));
    }

    private void drawDivide(Canvas canvas, boolean drawLeft, boolean drawTop, boolean drawRight, boolean drawBottom) {
        if(null==divideDrawable)return;
        int width = getWidth();
        int height = getHeight();

        if (drawLeft) {
            divideDrawable.setBounds(0, dividePadding, strokeWidth, height - dividePadding);
            divideDrawable.draw(canvas);
        }
        if (drawTop) {
            divideDrawable.setBounds(dividePadding, 0, width - dividePadding, strokeWidth);
            divideDrawable.draw(canvas);
        }
        if (drawRight) {
            divideDrawable.setBounds(width - strokeWidth, dividePadding, width, height - dividePadding);
            divideDrawable.draw(canvas);
        }
        if (drawBottom) {
            divideDrawable.setBounds(dividePadding + leftPadding, height - strokeWidth, width - dividePadding, height);
            divideDrawable.draw(canvas);
        }
    }
}
