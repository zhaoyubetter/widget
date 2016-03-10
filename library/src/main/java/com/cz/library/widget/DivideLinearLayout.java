package com.cz.library.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.cz.library.R;


/**
 * 分隔线布局
 * <p>
 * Created by cz on 2014/8/8
 */
public class DivideLinearLayout extends LinearLayout {
    public final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    public static final int NONE = 0x00;
    public static final int LEFT = 0x01;
    public static final int TOP = 0x02;
    public static final int RIGHT = 0x04;
    public static final int BOTTOM = 0x08;
    private boolean isShowItemDivide;// 显示子条目分隔线

    private float strokeSize;
    private float itemStrokeSize;

    private int divideColor;
    private int itemDivideColor;

    private int dividePadding;
    private int itemDividePadding;
    private int leftPadding;//左下单独边距,项目内这块需要比较多大
    private int gravity;
    private Paint paint;

    public DivideLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setWillNotDraw(false);
        initAttribute(context, attrs);
    }

    public DivideLinearLayout(Context context) {
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
        setDivideGravity(a.getInt(R.styleable.DivideLinearLayout_dl_divideGravity, NONE));
        setItemDivide(a.getBoolean(R.styleable.DivideLinearLayout_dl_itemDivide, false));
        setDivideSize(a.getDimension(R.styleable.DivideLinearLayout_dl_divideSize, getResources().getDimension(R.dimen.divideSize)));
        setItemDivideSize(a.getDimension(R.styleable.DivideLinearLayout_dl_itemDivideSize, getResources().getDimension(R.dimen.divideSize)));
        setDivideColor(a.getColor(R.styleable.DivideLinearLayout_dl_divideColor, resources.getColor(R.color.divide)));
        setItemDivideColor(a.getColor(R.styleable.DivideLinearLayout_dl_itemDivideColor, resources.getColor(R.color.divide)));
        setDividePadding((int) a.getDimension(R.styleable.DivideLinearLayout_dl_dividePadding, 0f));
        setLeftPadding((int) a.getDimension(R.styleable.DivideLinearLayout_dl_leftPadding, 0f));
        setItemDividePadding((int) a.getDimension(R.styleable.DivideLinearLayout_dl_itemDividePadding, 0f));
        a.recycle();
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

    private int getResourceColor(AttributeSet attrs, String attrName, int defaultColor) {
        int color = attrs.getAttributeResourceValue(MATERIALDESIGNXML, attrName, defaultColor);
        if (defaultColor == color) {
            color = attrs.getAttributeIntValue(MATERIALDESIGNXML, attrName, defaultColor);
        } else {
            color = getResources().getColor(color);
        }
        return color;
    }

    /**
     * 显示条目分隔线
     *
     * @param showItemDivide
     */
    public void setItemDivide(boolean showItemDivide) {
        this.isShowItemDivide = showItemDivide;
        invalidate();
    }

    public void setDivideGravity(int gravity) {
        this.gravity = gravity;
        invalidate();
    }

    /**
     * 设置分隔线宽
     *
     * @param size
     */
    public void setDivideSize(float size) {
        this.strokeSize = size;
        invalidate();
    }

    /**
     * 设置条目分隔线宽
     *
     * @param size
     */
    public void setItemDivideSize(float size) {
        this.itemStrokeSize = size;
        invalidate();
    }

    public void setDivideColor(int color) {
        this.divideColor = color;
        invalidate();
    }

    public void setItemDivideColor(int color) {
        this.itemDivideColor = color;
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
     * 设置条目分隔线边距
     *
     * @param padding
     */
    public void setItemDividePadding(int padding) {
        this.itemDividePadding = padding;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // 绘制周边分隔线
        drawDivide(canvas);
        // 画divider
        drawItemDivide(canvas);
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
        float interval = strokeSize / 2;
        paint.setStrokeWidth(strokeSize);
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

    /**
     * 绘制条目分隔线
     *
     * @param canvas
     */
    private void drawItemDivide(Canvas canvas) {
        if (isShowItemDivide) {
            paint.reset();
            paint.setColor(itemDivideColor);
            paint.setStrokeWidth((int) (itemStrokeSize / 2 + 0.5f));
            int width = getWidth();
            int height = getHeight();
            int orientation = getOrientation();
            int childCount = getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View childView = getChildAt(i);
                if (View.VISIBLE == childView.getVisibility()) {
                    int paddingLeft = getPaddingLeft();
                    int paddingTop = getPaddingTop();
                    int paddingRight = getPaddingRight();
                    int paddingBottom = getPaddingBottom();
                    if (HORIZONTAL == orientation) {
                        int right = childView.getRight();
                        canvas.drawLine(right, itemDividePadding + paddingTop, right, height - itemDividePadding - paddingBottom, paint);
                    } else if (VERTICAL == orientation) {
                        int bottom = childView.getBottom();
                        canvas.drawLine(itemDividePadding + paddingLeft, bottom, width - itemDividePadding - paddingRight, bottom, paint);
                    }
                }
            }
        }
    }


}
