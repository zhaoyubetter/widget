package com.cz.library.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.cz.library.R;


/**
 * 分隔线布局
 *
 * Created by cz on 2014/8/8
 */
public class DivideRelativeLayout extends RelativeLayout {
    public static final int NONE = 0x01;
    public static final int LEFT = 0x02;
    public static final int TOP = 0x04;
    public static final int RIGHT = 0x08;
    public static final int BOTTOM = 0x10;

    private Drawable divideDrawable;
    private int strokeWidth;
    private int dividePadding;
    private int leftPadding;//左下单独边距,项目内这块需要比较多大
    private int gravity;

    @IntDef({NONE, LEFT, TOP, RIGHT, BOTTOM})
    public @interface DivideGravity {
    }

    public DivideRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        initAttribute(context, attrs);
    }

    public DivideRelativeLayout(Context context) {
        this(context, null);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        Resources resources = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DivideRelativeLayout);
        setDivideGravityInner(a.getInt(R.styleable.DivideRelativeLayout_dr_divideGravity, NONE));
        setStrokeWidth(a.getDimension(R.styleable.DivideRelativeLayout_dr_divideSize, resources.getDimension(R.dimen.divideSize)));
        setDivideDrawable(a.getDrawable(R.styleable.DivideRelativeLayout_dr_divideDrawable));
        setDividePadding(a.getDimension(R.styleable.DivideRelativeLayout_dr_dividePadding, 0f));
        setLeftPadding(a.getDimension(R.styleable.DivideRelativeLayout_dr_leftPadding, 0f));
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


    public void setDivideDrawable(Drawable drawable) {
        this.divideDrawable = drawable;
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
