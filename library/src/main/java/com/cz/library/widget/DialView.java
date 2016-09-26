package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cz.library.R;

/**
 * Created by czz on 2016/9/26.
 */
public class DialView extends View{
    private static final String TAG = "DialView";
    private final boolean DEBUG=true;
    private final Paint paint;
    private final Path path;
    private float dialPadding;

    private float[] pos;
    private float[] tan;
    private PathMeasure pathMeasure;
    private Drawable progressDrawable;
    private RectF arcRect;
    private float dialInnerPadding;
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
        path=new Path();
        pos=new float[2];
        tan=new float[2];

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialView);
        setStrokeWidth(a.getDimension(R.styleable.DialView_dv_strokeWidth,0));
        setPaintColor(a.getColor(R.styleable.DialView_dv_paintColor, Color.WHITE));
        setDialPadding(a.getDimension(R.styleable.DialView_dv_dialPadding,0));
        setDialProgressDrawable(a.getDrawable(R.styleable.DialView_dv_dialProgressDrawable));
        setDialInnerPadding(a.getDimension(R.styleable.DialView_dv_dialInnerPadding,0));
        setDialItemIntervalDegrees(a.getInteger(R.styleable.DialView_dv_dialItemIntervalDegrees,4));
        setDialItemCount(a.getInteger(R.styleable.DialView_dv_dialItemCount,5));
        a.recycle();
    }

    private void setDialItemCount(int count) {
        this.itemCount=count;
        invalidate();
    }

    public void setDialItemIntervalDegrees(int degrees) {
        this.itemIntervalDegrees=degrees;
        invalidate();
    }

    public void setDialInnerPadding(float padding) {
        this.dialInnerPadding=padding;
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

    public void setPaintColor(int color) {
        this.paint.setColor(color);
        invalidate();
    }

    public void setDialProgressDrawable(Drawable drawable) {
        this.progressDrawable=drawable;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paint.setStyle(Paint.Style.STROKE);
        int width = getWidth();
        int height = getHeight();
        float radius = Math.max(width, height)/2-dialPadding;
        path.addCircle(width/2,height,radius, Path.Direction.CW);
        pathMeasure=new PathMeasure(path, false);

        float padding = dialPadding + dialInnerPadding;
        float startY=height-radius+padding;
        arcRect=new RectF(padding,startY,width-padding,startY+radius*2);
    }

    public void setProgress(float fraction){
        this.fraction=fraction;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawPath(path,paint);
        float length = pathMeasure.getLength();
        pathMeasure.getPosTan(length/2+fraction*length/2,pos,tan);
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

        int startDegrees=90;
        int itemDegrees=(180-itemIntervalDegrees*itemCount)/itemCount;
        for(int i=0;i<itemCount;i++){
            canvas.drawArc(arcRect,startDegrees+itemIntervalDegrees,itemDegrees,false,paint);
            startDegrees+=itemDegrees;
        }

        if(DEBUG){
            int radius = Math.max(width, height)/2;
            canvas.drawLine(0,height-radius,width,height-radius,paint);
            canvas.drawLine(width/2,0,width/2,height,paint);
        }
    }
}
