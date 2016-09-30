package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.cz.library.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by czz on 2016/9/26.
 */
public class DialView extends View{
    private static final String TAG = "DialView";
    private final boolean DEBUG=true;
    private final Paint paint;
    private final Paint ringPaint;
    private final Paint textPaint;
    private final Path path;
    private final Path textPath;
    private float dialPadding;

    private float[] pos;
    private float[] tan;
    private PathMeasure pathMeasure;
    private PathMeasure textPathMeasure;
    private Drawable progressDrawable;
    private RectF arcRect;
    private Rect textRect;
    private float innerStrokeWidth1;
    private float innerStrokeWidth2;
    private float dialInnerPadding1;
    private float dialInnerPadding2;
    private float dialBottomPadding;
    private int intervalItemCount;
    private int levelTextSize;
    private int levelInfoTextSize;
    private int levelValueTextSize;
    private String levelText;
    private int currentLevelValue;
    private int levelMinValue;
    private int levelMaxValue;
    private float fraction;
    private int itemIntervalDegrees;
    private int itemCount;


    public DialView(Context context) {
        this(context,null,0);
    }

    public DialView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
        textRect=new Rect();
        path=new Path();
        textPath=new Path();
        pos=new float[2];
        tan=new float[2];

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialView);
        setStrokeWidth(a.getDimension(R.styleable.DialView_dv_strokeWidth,0));
        setInnerStrokeWidth1(a.getDimension(R.styleable.DialView_dv_innerStrokeWidth1,0));
        setInnerStrokeWidth2(a.getDimension(R.styleable.DialView_dv_innerStrokeWidth2,0));
        setPaintColor(a.getColor(R.styleable.DialView_dv_paintColor, Color.WHITE));
        setDialPadding(a.getDimension(R.styleable.DialView_dv_dialPadding,0));
        setDialProgressDrawable(a.getDrawable(R.styleable.DialView_dv_dialProgressDrawable));
        setDialInnerPadding1(a.getDimension(R.styleable.DialView_dv_dialInnerPadding1,0));
        setDialInnerPadding2(a.getDimension(R.styleable.DialView_dv_dialInnerPadding2,0));
        setDialItemIntervalDegrees(a.getInteger(R.styleable.DialView_dv_dialItemIntervalDegrees,2));
        setDialIntervalItemCount(a.getInteger(R.styleable.DialView_dv_dialIntervalItemCount,7));
        setDialItemCount(a.getInteger(R.styleable.DialView_dv_dialItemCount,5));
        setDialBottomPadding(a.getDimension(R.styleable.DialView_dv_dialBottomPadding,0));

        setLevelText(a.getString(R.styleable.DialView_dv_dialLevelText));
        setMinLevelValue(a.getInteger(R.styleable.DialView_dv_minLevelValue,0));
        setMaxLevelValue(a.getInteger(R.styleable.DialView_dv_maxLevelValue,100));
        setLevelTextSize(a.getDimensionPixelSize(R.styleable.DialView_dv_levelTextSize,applyDimension(TypedValue.COMPLEX_UNIT_SP,8)));
        setLevelInfoTextSize(a.getDimensionPixelSize(R.styleable.DialView_dv_levelInfoTextSize,applyDimension(TypedValue.COMPLEX_UNIT_SP,16)));
        setLevelValueTextSize(a.getDimensionPixelSize(R.styleable.DialView_dv_levelValueTextSize,applyDimension(TypedValue.COMPLEX_UNIT_SP,28)));
        a.recycle();
    }

    public int applyDimension(int typedValue,int value){
        return Math.round(TypedValue.applyDimension(typedValue,value,getResources().getDisplayMetrics()));
    }
    public void setDialBottomPadding(float padding) {
        this.dialBottomPadding=padding;
        invalidate();
    }

    public void setDialIntervalItemCount(int itemCount) {
        this.intervalItemCount=itemCount;
        invalidate();
    }

    private void setDialItemCount(int count) {
        this.itemCount=count;
        invalidate();
    }

    public void setDialItemIntervalDegrees(int degrees) {
        this.itemIntervalDegrees=degrees;
        invalidate();
    }

    public void setDialInnerPadding1(float padding) {
        this.dialInnerPadding1=padding;
        invalidate();
    }

    public void setDialInnerPadding2(float padding) {
        this.dialInnerPadding2=padding;
        invalidate();
    }

    public void setDialPadding(float padding) {
        this.dialPadding=padding;
        invalidate();
    }

    public void setStrokeWidth(float width) {
        this.paint.setStrokeWidth(width);
        invalidate();
    }

    public void setInnerStrokeWidth1(float strokeWidth) {
        this.innerStrokeWidth1=strokeWidth;
        invalidate();
    }

    public void setInnerStrokeWidth2(float strokeWidth) {
        this.innerStrokeWidth2=strokeWidth;
        invalidate();
    }

    public void setPaintColor(int color) {
        this.paint.setColor(color);
        this.ringPaint.setColor(color);
        this.textPaint.setColor(color);
        invalidate();
    }

    public void setDialProgressDrawable(Drawable drawable) {
        this.progressDrawable=drawable;
        invalidate();
    }

    public void setLevelTextSize(int textSize) {
        this.levelTextSize=textSize;
        invalidate();
    }
    public void setLevelInfoTextSize(int textSize) {
        this.levelInfoTextSize=textSize;
        invalidate();
    }
    public void setLevelValueTextSize(int textSize) {
        this.levelValueTextSize=textSize;
        invalidate();
    }

    public void setLevelText(String text) {
        this.levelText=text;
        invalidate();
    }

    public void setMinLevelValue(int minValue) {
        this.levelMinValue=minValue;
        this.currentLevelValue=minValue;
        invalidate();
    }

    public void setMaxLevelValue(int maxValue) {
        this.levelMaxValue=maxValue;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paint.setStyle(Paint.Style.STROKE);
        ringPaint.setStyle(Paint.Style.STROKE);
        int width = getWidth();
        int height = getHeight();
        float radius = Math.max(width, height)/2-dialPadding;
        path.addCircle(width/2,height- dialBottomPadding,radius, Path.Direction.CW);
        pathMeasure=new PathMeasure(path, false);

        textPath.addCircle(width/2,height- dialBottomPadding,radius-dialPadding-dialInnerPadding1-dialInnerPadding2,Path.Direction.CW);
        textPathMeasure=new PathMeasure(textPath,false);
        float startY=height-radius+dialInnerPadding1;
        float padding = dialPadding + dialInnerPadding1;
        arcRect=new RectF(padding,startY- dialBottomPadding,width-padding,startY+(radius-dialInnerPadding1)*2- dialBottomPadding);

    }

    public void setProgress(float fraction){
        this.fraction=fraction;
        invalidate();
    }

    public void setRotateDegrees(int degrees){
        this.rotateDegrees=degrees;
        invalidate();
    }

    public void levelValueTo(int value){
        //current value->value
        if(levelMinValue<=value&&levelMaxValue>=value){

        }
    }

    private int rotateDegrees;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long st = System.currentTimeMillis();
        int width = getWidth();
        int height = getHeight();
        paint.setColor(Color.WHITE);
        canvas.drawPath(path,paint);

        float radius = Math.max(width, height)/2-dialPadding;
        float halfLength = pathMeasure.getLength()/2;
        pathMeasure.getPosTan(halfLength+fraction*halfLength,pos,tan);
        float degrees =(float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
        if(null!=progressDrawable){
            canvas.save();
            canvas.rotate(degrees,pos[0],pos[1]);
            int halfDrawableWidth = progressDrawable.getIntrinsicWidth()/2;
            int halfDrawableHeight = progressDrawable.getIntrinsicHeight()/2;
            progressDrawable.setBounds((int)pos[0]-halfDrawableWidth,(int)pos[1]-halfDrawableHeight,(int)pos[0]+halfDrawableWidth,(int)pos[1]+halfDrawableHeight);
            progressDrawable.draw(canvas);
            canvas.restore();
        }
        int startDegrees=180+itemIntervalDegrees/2;
        int itemDegrees=(180-itemIntervalDegrees*(itemCount-1))/(itemCount-1);
        ringPaint.setStrokeWidth(innerStrokeWidth1);
        for(int i=0;i<itemCount-1;i++){
            canvas.drawArc(arcRect,startDegrees,itemDegrees,false,ringPaint);
            startDegrees+=itemDegrees+itemIntervalDegrees;
        }

        int totalItemCount=intervalItemCount*(itemCount-1);
        float itemFraction=1f/totalItemCount;
        float strokeWidth = ringPaint.getStrokeWidth();
        ringPaint.setStrokeWidth(innerStrokeWidth2);
        for(int i=0;i<=totalItemCount;i++){
            pathMeasure.getPosTan(halfLength+i*itemFraction*halfLength,pos,tan);
            degrees =(float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
            canvas.save();
            canvas.rotate(degrees,pos[0],pos[1]);
            canvas.translate(0 ,dialInnerPadding1+dialInnerPadding2+strokeWidth/2);
            canvas.drawLine(pos[0],pos[1]-(0==i%intervalItemCount?10:0),pos[0],pos[1]+10,ringPaint);
            canvas.restore();
        }

        //draw level;
        if(levelMinValue!=levelMaxValue&&levelMaxValue-levelMinValue>itemCount) {
            halfLength = textPathMeasure.getLength() / 2;
            textPaint.setTextSize(levelTextSize);
            int itemValue = (levelMaxValue - levelMinValue) / (itemCount-1);
            for (int i = 0; i < itemCount; i++) {
                String text = String.valueOf(levelMinValue+i * itemValue);
                float textWidth = textPaint.measureText(text, 0, text.length());
                textPathMeasure.getPosTan(textWidth / 2, pos, tan);
                degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
                textPaint.getTextBounds(text.toString(), 0, text.length(), textRect);
                canvas.save();
                canvas.rotate(270 - degrees, width / 2, height - dialBottomPadding);
                canvas.drawTextOnPath(text.toString(), textPath, i * (halfLength / (itemCount - 1)), textRect.height(), textPaint);
                canvas.restore();
            }
        }
        //draw note
//        (height-(textPaint.descent() + textPaint.ascent()))/2
        if(!TextUtils.isEmpty(levelText)){
            textPaint.setTextSize(levelInfoTextSize);
            float textWidth = textPaint.measureText(levelText, 0, levelText.length());
            canvas.drawText(levelText,(width-textWidth)/2,-(textPaint.descent()+ textPaint.ascent())+(height-radius)+radius/2,textPaint);
        }
        //draw value
        String value=String.valueOf(500);
        textPaint.setTextSize(levelValueTextSize);
        float textWidth = textPaint.measureText(value, 0, value.length());
        textPaint.getTextBounds(value, 0, value.length(),textRect);
        canvas.drawText(value,(width-textWidth)/2,(height-textPaint.descent()- textPaint.ascent())-textRect.height()- dialBottomPadding,textPaint);

        if(DEBUG){
            radius = Math.max(width, height)/2;
            canvas.drawLine(0,height-radius,width,height-radius,paint);
            canvas.drawLine(width/2,0,width/2,height,paint);
            canvas.drawLine(0,height-dialBottomPadding,width,height-dialBottomPadding,paint);
        }

        Log.e(TAG,"time:"+(System.currentTimeMillis()-st));
    }

}
