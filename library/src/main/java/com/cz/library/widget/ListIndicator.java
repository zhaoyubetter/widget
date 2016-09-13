package com.cz.library.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cz.library.R;
import com.cz.library.util.Utils;



/**
 * Created by cz on 2015/3/19.
 * ListView 侧边指示器
 * 1:指示图形. 圆,方,放大圆心效果
 * 2:指示选择颜色
 * 3:指示器字体大小
 * 4:指示器字体颜色
 */
public class ListIndicator extends View {
    private static final boolean DEBUG = false;

    private static final int PRESS_INDEX = 0;
    private static final int SCALE_COUNT = 5;//缩放条目个数
    private static final int CIRCLE = 0x00;
    private static final int RECTANGLE = 0x01;
    private static final int SCALE_CIRCLE = 0x02;
    private String[] datum;
    private int type;
    private int color;
    private int textColor;
    private float textSize;
    private float textScale;//
    private int textSelectColor;
    private Paint paint;
    private int position;//选中位置
    private OnIndicatorListener listener;
    private float[] fractions;
    private int angle;

    public ListIndicator(Context context) {
        this(context, null, 0);
    }

    public ListIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        fractions = new float[3];
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListIndicator);
        setType(a.getInt(R.styleable.ListIndicator_li_type, CIRCLE));
        setColor(a.getColor(R.styleable.ListIndicator_li_color, Color.BLUE));
        setTextColor(a.getColor(R.styleable.ListIndicator_li_text_color, Color.DKGRAY));
        setTextSelectColor(a.getColor(R.styleable.ListIndicator_li_select_color, Color.BLUE));
        setTextSize(a.getDimensionPixelSize(R.styleable.ListIndicator_li_text_size, Utils.sp2px(12)));
        setTextScale(a.getDimensionPixelSize(R.styleable.ListIndicator_li_textScale, Utils.sp2px(20)));
        a.recycle();
    }


    /**
     * 设置绘制类型
     *
     * @param type 绘制类型
     * @see {@link ListIndicator#RECTANGLE#CIRCLE}
     */
    public void setType(int type) {
        this.type = type;
        invalidate();
    }

    /**
     * 设置绘制边框颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    /**
     * 设置字体颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        this.textColor = color;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    /**
     * 设置字体选中颜色
     *
     * @param color
     */
    public void setTextSelectColor(int color) {
        this.textSelectColor = color;
        invalidate();
    }

    public void setSelectPosition(int position) {
        if (0 > position || position >= datum.length) return;
        this.position = position;
        invalidate();
    }

    /**
     * 文字放大值
     *
     * @param scale
     */
    public void setTextScale(float scale) {
        this.textScale = scale;
        invalidate();
    }

    /**
     * 设置角度
     *
     * @param angle
     */
    public void setAngle(int angle) {
        this.angle = angle;
        invalidate();
    }

    /**
     * 设置绘制数据列表
     *
     * @param data
     */
    public void setData(String[] data) {
        this.datum = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == datum || 0 == datum.length) return;
        int width = getWidth();
        int height = getHeight();
        int length = datum.length;
        float itemHeight = height * 1.0f / length;
        Rect bounds = new Rect();
        int position = this.position;
        int scaleCount = SCALE_COUNT;
        float scaleWidth = (scaleCount - 1) * 2 * itemHeight;
        //在上下,五个值产生影响,每个大小控件scale/5值,形成半圆,放大,width/scale+1;倍
        //先绘半圆
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < length; i++) {
            //绘图形
            if (SCALE_CIRCLE == type) {
                float x, y;
                float angle;
                float radius = scaleWidth / 2;
                float textSize;
                int textColor;
                String data = datum[i];
                paint.setTextSize(this.textSize);
                paint.getTextBounds(data, 0, data.length(), bounds);
                //放大半圆
                if (i > position - scaleCount && i < position) {
                    //上边
                    angle = 180 - (position - i) * (90f / (scaleCount));
                    x = (int) (width / 2 + Math.cos(angle / 360f * 2 * Math.PI) * radius);
                    y = (int) (position * itemHeight + itemHeight - Math.sin(angle / 360f * 2 * Math.PI) * radius);
                    textSize = this.textSize + (scaleCount - (position - i)) * (textScale / scaleCount);
                    textColor = evaluate((scaleCount - (position - i)) * 1.0f / scaleCount, this.textColor, textSelectColor);
                } else if (i < position + scaleCount && i > position) {
                    //下边
                    angle = 180 + (i - position) * (90f / (scaleCount));
                    x = (int) (width / 2 + Math.cos(angle / 360f * 2 * Math.PI) * radius);
                    y = (int) (position * itemHeight + itemHeight - Math.sin(angle / 360f * 2 * Math.PI) * radius);
                    textSize = this.textSize + (scaleCount - (i - position)) * (textScale / scaleCount);
                    textColor = evaluate((scaleCount - (i - position)) * 1.0f / scaleCount, this.textColor, textSelectColor);
                } else if (i == position) {
                    //正中
                    x = (int) (width / 2 + Math.cos((float) 180 / 360f * 2 * Math.PI) * radius);
                    y = (int) (position * itemHeight + itemHeight - Math.sin((float) (180) / 360f * 2 * Math.PI) * radius);
                    textSize = this.textSize + textScale;
                    textColor = textSelectColor;
                } else {
                    textSize = this.textSize;
                    x = width / 2 - bounds.centerX();
                    y = itemHeight * i + itemHeight / 2 - bounds.centerY();
                    textColor = this.textColor;
                }

                if (i == position) {
                    //绘图形
                    paint.setColor(color);
                    RectF rect = getBlockRect(width, itemHeight, i);
                    canvas.drawCircle(rect.centerX(), rect.centerY(), Math.min(rect.width(), rect.height()) / 2 * (1 - fractions[PRESS_INDEX]), paint);
                }
                paint.setTextSize(this.textSize + (textSize - this.textSize) * fractions[PRESS_INDEX]);
                //指定位置
                float targetX = width / 2 - bounds.centerX();
                float targetY = itemHeight * i + itemHeight / 2 - bounds.centerY();
                x = targetX + (x - targetX) * fractions[PRESS_INDEX];
                y = targetY + (y - targetY) * fractions[PRESS_INDEX];
                paint.setColor(evaluate(fractions[PRESS_INDEX], this.textColor, textColor));
                canvas.drawText(data, x, y, paint);
            } else if (i == this.position) {
                //绘图形
                paint.setColor(color);
                RectF rect = getBlockRect(width, itemHeight, i);
                switch (type) {
                    case RECTANGLE:
                        canvas.drawRect(rect, paint);
                        break;
                    case CIRCLE:
                        canvas.drawCircle(rect.centerX(), rect.centerY(), Math.min(rect.width(), rect.height()) / 2, paint);
                        break;
                }
            }
            if (SCALE_CIRCLE != type) {
                //绘文字
                paint.setAlpha(0xFF);
                paint.setColor(textColor);
                paint.setTextSize(textSize);
                String data = datum[i];
                paint.getTextBounds(data, 0, data.length(), bounds);
                float x = width / 2 - bounds.centerX();
                float y = itemHeight * i + itemHeight / 2 - bounds.centerY();
                canvas.drawText(datum[i], x, y, paint);
            }

            //测试代码
            if (DEBUG) {
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(3);
                canvas.drawLine(-200, itemHeight * i, width + 200, itemHeight * i, paint);
            }
        }
        //测试代码
        if (DEBUG) {
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            canvas.drawCircle(width / 2, position * itemHeight + itemHeight / 2, scaleWidth / 2, paint);
        }
    }

    @NonNull
    private RectF getBlockRect(int width, float itemHeight, int i) {
        RectF rect;
        if (width >= itemHeight) {
            rect = new RectF((width - itemHeight) / 2, itemHeight * i, width - (width - itemHeight) / 2, itemHeight * (i + 1));
        } else {
            rect = new RectF(0, itemHeight * i + (itemHeight - width) / 2, width, itemHeight * (i + 1) - (itemHeight - width) / 2);
        }
        return rect;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null == datum) return true;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (null != listener) {
                    listener.onPress();
                    startAnim(PRESS_INDEX, false);
                }
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int position = getPosition(y);
                if (0 > position) {
                    position = 0;
                } else if (position >= datum.length) {
                    position = datum.length - 1;
                }
                if (position != this.position) {
                    if (null != listener) {
                        listener.onSelect(datum[position], position);
                    }
                    //这里位置变化
                    this.position = position;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (null != listener) {
                    listener.onCancel();
                    startAnim(PRESS_INDEX, true);
                }
                break;
        }
        return true;
    }

    /**
     * 执行动画
     *
     * @param index
     */
    private void startAnim(final int index, final boolean reversal) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                fractions[index] = reversal ? 1 - fraction : fraction;
                invalidate();
            }
        });
        valueAnimator.start();
    }

    /**
     * 转换颜色
     *
     * @param fraction
     * @param startColor
     * @param endColor
     * @return
     */
    public int evaluate(float fraction, int startColor, int endColor) {
        int startInt = (Integer) startColor;
        int startA = (startInt >> 24);
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endColor;
        int endA = (endInt >> 24);
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) | (int) ((startR + (int) (fraction * (endR - startR))) << 16) | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

    private int getPosition(float y) {
        return (int) y / (getHeight() / datum.length);
    }

    public void setOnIndicatorListener(OnIndicatorListener listener) {
        this.listener = listener;
    }

    public interface OnIndicatorListener {
        /**
         * 开始按下
         */
        void onPress();

        /**
         * 选中位置
         *
         * @param word
         * @param position
         */
        void onSelect(String word, int position);

        /**
         * 取消
         */
        void onCancel();
    }
}
