package com.cz.library.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    public static final int NONE = 0x00;
    public static final int LEFT = 0x01;
    public static final int TOP = 0x02;
    public static final int RIGHT = 0x04;
    public static final int BOTTOM = 0x08;

    private float strokeWidth;
    private int divideColor;
    private int dividePadding;
    private int leftPadding;//左下单独边距,项目内这块需要比较多大
    private int gravity;
    private Paint paint;

    @IntDef({NONE, LEFT, TOP, RIGHT, BOTTOM})
    public @interface DivideGravity {
    }

    public DivideRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DivideLinearLayout);
        gravity = (a.getInt(R.styleable.DivideLinearLayout_dl_divideGravity, NONE));
        setStrokeWidth(a.getDimension(R.styleable.DivideLinearLayout_dl_divideSize, resources.getDimension(R.dimen.divideSize)));
        setDivideColor(a.getColor(R.styleable.DivideLinearLayout_dl_divideColor, resources.getColor(R.color.divide)));
        setDividePadding((int) a.getDimension(R.styleable.DivideLinearLayout_dl_dividePadding, 0f));
        setLeftPadding((int) a.getDimension(R.styleable.DivideLinearLayout_dl_leftPadding, 0f));
        a.recycle();
    }


    public void setDivideGravity(@DivideGravity int gravity) {
        this.gravity = gravity;
        invalidate();
    }

    /**
     * 设置分隔线宽
     *
     * @param strokeWidth
     */
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
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
    public void setDividePadding(int padding) {
        this.dividePadding = padding;
        invalidate();
    }

    /**
     * 设置左边内边距
     *
     * @param padding
     */
    private void setLeftPadding(int padding) {
        this.leftPadding = padding;
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
        paint.reset();
        paint.setColor(divideColor);
        int width = getWidth();
        int height = getHeight();
        float interval = strokeWidth / 2;
        paint.setStrokeWidth(strokeWidth);
        if (drawLeft) {
            canvas.drawLine(interval, dividePadding, interval, height - dividePadding, paint);
        }
        if (drawTop) {
            canvas.drawLine(dividePadding, interval, width - dividePadding, interval, paint);
        }
        if (drawRight) {
            canvas.drawLine(width - interval, dividePadding, width - interval, height - dividePadding, paint);
        }
        if (drawBottom) {
            canvas.drawLine(dividePadding + leftPadding, height - interval, width - dividePadding, height - interval, paint);
        }
    }
}
