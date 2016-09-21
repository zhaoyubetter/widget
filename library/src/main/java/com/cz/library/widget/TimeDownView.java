package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cz.library.R;
import com.cz.library.util.Utils;
import com.cz.library.widget.translation.ITextTranslation;

import java.text.DecimalFormat;


/**
 * Created by czz on 16/01/10.
 */
public class TimeDownView extends View {
    private final static int NO_COLOR=0x01<<16;
    private final String DEFAULT_VALUE = "00";
    public final int MINUTES = 60 * 1000;// 分毫秒值
    public final int HOUR = 60 * MINUTES;// 小时毫秒值
    public final int DAY = 24 * HOUR;// 天毫秒值

    private final int TYPE_HOUR=1;
    private final int TYPE_MINUTE=2;
    private final int TYPE_SECOND=3;

    //中间分隔模式
    public static final int CIRCLE = 0x00;
    public static final int RECTANGLE = 0x01;

    private float itemPadding;
    private int intervalMode;
    private int timeValue;
    private float intervalFlagSize;//分隔标志大小
    private Drawable itemDrawable;
    private Drawable hourItemDrawable;
    private Drawable minuteItemDrawable;
    private Drawable secondItemDrawable;
    private DecimalFormat decimalFormatter;
    private float horizontalIntervalPadding;
    private float verticalIntervalPadding;
    private float itemIntervalRoundRadius;
    private ITextTranslation translation;
    private CountDownTimer timer;
    private TextPaint textPaint;
    private int textColor;
    private int hourTextColor;
    private int minuteTextColor;
    private int secondTextColor;
    private Paint paint;

    public TimeDownView(Context context) {
        this(context, null, 0);
    }

    public TimeDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint=new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        decimalFormatter = new DecimalFormat("00");
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimeDownView);
        setTextColor(a.getColor(R.styleable.TimeDownView_tv_textColor, Color.WHITE));
        setHourTextColor(a.getColor(R.styleable.TimeDownView_tv_hourTextColor,NO_COLOR));
        setMinuteTextColor(a.getColor(R.styleable.TimeDownView_tv_minuteTextColor, NO_COLOR));
        setSecondTextColor(a.getColor(R.styleable.TimeDownView_tv_secondTextColor, NO_COLOR));

        setTextSize(a.getDimensionPixelSize(R.styleable.TimeDownView_tv_textSize, Utils.sp2px(12)));
        setItemPadding(a.getDimension(R.styleable.TimeDownView_tv_itemPadding, Utils.dip2px(4)));
        setHorizontalItemPadding(a.getDimension(R.styleable.TimeDownView_tv_horizontalItemPadding, 0));
        setVerticalItemPadding(a.getDimension(R.styleable.TimeDownView_tv_verticalItemPadding, 0));
        setItemIntervalColor(a.getColor(R.styleable.TimeDownView_tv_itemIntervalColor, Color.TRANSPARENT));
        setItemIntervalRoundRadius(a.getDimension(R.styleable.TimeDownView_tv_itemIntervalRoundRadius, 0));
        setItemDrawable(a.getDrawable(R.styleable.TimeDownView_tv_itemDrawable));
        setHourItemDrawable(a.getDrawable(R.styleable.TimeDownView_tv_hourItemDrawable));
        setMinuteItemDrawable(a.getDrawable(R.styleable.TimeDownView_tv_minuteItemDrawable));
        setSecondItemDrawable(a.getDrawable(R.styleable.TimeDownView_tv_secondItemDrawable));
        setTimeValue(a.getInteger(R.styleable.TimeDownView_tv_timeValue, DAY));//默认1天
        setIntervalMode(a.getInt(R.styleable.TimeDownView_tv_intervalMode, CIRCLE));
        setIntervalFlagSize(a.getDimension(R.styleable.TimeDownView_tv_intervalFlagSize, Utils.dip2px(1)));
        a.recycle();
    }




    public void setItemIntervalColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    public void setItemIntervalRoundRadius(float radius) {
        this.itemIntervalRoundRadius =radius;
        invalidate();
    }


    public void setItemPadding(float itemPadding) {
        this.itemPadding = itemPadding;
        invalidate();
    }

    public void setHorizontalItemPadding(float padding) {
        this.horizontalIntervalPadding = padding;
        invalidate();
    }

    public void setVerticalItemPadding(float padding) {
        this.verticalIntervalPadding = padding;
        invalidate();
    }


    public void setTextColor(int textColor) {
        this.textColor=textColor;
        invalidate();
    }

    public void setHourTextColor(int color) {
        this.hourTextColor=color;
        invalidate();
    }

    public void setMinuteTextColor(int color) {
        this.minuteTextColor=color;
        invalidate();
    }

    public void setSecondTextColor(int color) {
        this.secondTextColor=color;
        invalidate();
    }

    public void setItemDrawable(Drawable drawable) {
        this.itemDrawable = drawable;
        invalidate();
    }

    public void setHourItemDrawable(Drawable drawable) {
        this.hourItemDrawable = drawable;
        invalidate();
    }

    public void setMinuteItemDrawable(Drawable drawable) {
        this.minuteItemDrawable = drawable;
        invalidate();
    }

    public void setSecondItemDrawable(Drawable drawable) {
        this.secondItemDrawable = drawable;
        invalidate();
    }

    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
        invalidate();
    }

    public void setTimeValue(int time) {
        this.timeValue = time;
        initTimer(timeValue);
        invalidate();
    }

    public void setIntervalMode(int mode) {
        this.intervalMode = mode;
        invalidate();
    }

    public void setIntervalFlagSize(float size) {
        this.intervalFlagSize = size;
        invalidate();
    }

    public void setTextTransaltion(ITextTranslation transaltion){
        this.translation=transaltion;
    }

    /**
     * 初始化定时器对象
     *
     * @param timeValue
     */
    private void initTimer(int timeValue) {
        //动态重新设定时,取缔上一个定时器对象
        if (null != timer) {
            timer.cancel();
        }
        timer = new CountDownTimer(timeValue, 1 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeDownView.this.timeValue = (int) millisUntilFinished;
                invalidate();
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

    @Override
    public void invalidate() {
        if(hasWindowFocus()) super.invalidate();
    }

    @Override
    public void postInvalidate() {
        if(hasWindowFocus()) super.postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //动态计算宽高
        CharSequence text = DEFAULT_VALUE;
        float textWidth = 0, textHeight = 0;
        if (!TextUtils.isEmpty(text)) {
            textWidth = textPaint.measureText(text.toString());//文字宽
            Rect textRect = new Rect();
            textPaint.getTextBounds(text.toString(), 0, text.length(), textRect);
            textHeight = textRect.height();
        }
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //动态设定控件大小
        setMeasuredDimension(resolveSize((int) (paddingLeft + textWidth * 3 + itemPadding * 2 + horizontalIntervalPadding * 6 + paddingRight), widthMeasureSpec),
                resolveSize((int) (paddingTop + textHeight + verticalIntervalPadding * 2 + paddingBottom), heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画各个条目背景
        int timeMillis = timeValue;
        //当前时,分,秒
        int hour = timeMillis / HOUR;
        int minute = (timeMillis - hour * HOUR) / MINUTES;
        int second = timeMillis / 1000 % 60;

        float hourOffset = drawTimeValue(canvas, hour, getPaddingLeft(), TYPE_HOUR);//绘时
        drawInterval(canvas, hourOffset);//时间隔
        float minuteOffset = drawTimeValue(canvas, minute, hourOffset + itemPadding, TYPE_MINUTE);//绘分
        drawInterval(canvas, minuteOffset);//分间隔
        drawTimeValue(canvas, second, minuteOffset + itemPadding, TYPE_SECOND);//绘秒
    }

    /**
     * 绘制时间值
     *
     * @param canvas
     * @param value
     * @param offset
     */
    private float drawTimeValue(Canvas canvas, int value, float offset,int type) {
        int height = getHeight();
        int paddingTop = getPaddingTop();
        String text = decimalFormatter.format(value);
        float textWidth = textPaint.measureText(text);//文字宽
        float right = offset + textWidth + horizontalIntervalPadding * 2;
        //绘条目背景
        Drawable drawable=null;
        if(TYPE_HOUR==type){
            drawable=getItemDrawable(hourItemDrawable,itemDrawable);
        } else if(TYPE_MINUTE==type){
            drawable=getItemDrawable(minuteItemDrawable,itemDrawable);
        } else if(TYPE_SECOND==type){
            drawable=getItemDrawable(secondItemDrawable,itemDrawable);
        }
        if(null!=drawable){
            drawable.setBounds((int) (offset), paddingTop,
                    (int) right, height - getPaddingBottom());
            drawable.draw(canvas);
        }
        //绘文字
        float centerY = (height - (textPaint.descent() + textPaint.ascent())) / 2;//文字居中
//        if(null!=translation){
//            translation.drawFrame(canvas,textPaint,offset+horizontalIntervalPadding,centerY,text);
//        } else {
            int color=textColor;
            if(TYPE_HOUR==type){
                color=getItemTextColor(hourTextColor,textColor);
            } else if(TYPE_MINUTE==type){
                color=getItemTextColor(minuteTextColor,textColor);
            } else if(TYPE_SECOND==type){
                color=getItemTextColor(secondTextColor,textColor);
            }
            textPaint.setColor(color);
            canvas.drawText(text, offset + horizontalIntervalPadding, centerY, textPaint);
//        }
        return right;
    }

    private int getItemTextColor(int itemColor,int textColor) {
        int color=textColor;
        if(itemColor!=NO_COLOR){
            color=itemColor;
        }
        return color;
    }

    private Drawable getItemDrawable(Drawable typeItemDrawable, Drawable itemDrawable) {
        Drawable drawable=null;
        if(null!=typeItemDrawable){
            drawable=typeItemDrawable;
        }else if(null!=itemDrawable){
            drawable=itemDrawable;
        }
        return drawable;
    }

    /**
     * 绘间隔
     *
     * @param offset
     */
    private void drawInterval(Canvas canvas, float offset) {
        paint.setStyle(Paint.Style.FILL);//实心
        int itemHeight = getHeight() / 3;
        float halfPadding = itemPadding / 2;
        if (CIRCLE == intervalMode) {
            //分隔绘圆
            canvas.drawCircle(offset + halfPadding, itemHeight, intervalFlagSize, paint);//上边分隔
            canvas.drawCircle(offset + halfPadding, itemHeight * 2, intervalFlagSize, paint);//下边分隔
        } else {
            //分隔绘方
            canvas.drawRoundRect(new RectF(offset + halfPadding - intervalFlagSize / 2,
                            itemHeight - intervalFlagSize,
                            offset + halfPadding + intervalFlagSize / 2,
                            itemHeight + intervalFlagSize), itemIntervalRoundRadius,
                    itemIntervalRoundRadius, paint);//上边

            canvas.drawRoundRect(new RectF(offset + halfPadding - intervalFlagSize / 2,
                            itemHeight * 2 - intervalFlagSize,
                            offset + halfPadding + intervalFlagSize / 2,
                            itemHeight * 2 + intervalFlagSize), itemIntervalRoundRadius,
                    itemIntervalRoundRadius, paint);//下边
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable data = super.onSaveInstanceState();
        bundle.putParcelable("data", data);
        bundle.putInt("time", timeValue);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable("data");
        timeValue=bundle.getInt("time");
        super.onRestoreInstanceState(superData);
    }
}
