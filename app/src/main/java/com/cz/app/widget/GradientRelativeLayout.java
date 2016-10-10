package com.cz.app.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by cz on 10/8/16.
 */
public class GradientRelativeLayout extends RelativeLayout {
    private int[][] colors;

    public GradientRelativeLayout(Context context) {
        this(context, null, 0);
    }

    public GradientRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setAnimColors(int[]...colors){
        if(null==colors||2>=colors.length){
            throw new IllegalArgumentException("color length must greater then two!");
        } else {
            for(int i=0;i<colors.length;i++){
                if(2!=colors[i].length){
                    throw new IllegalArgumentException("color item length must two!");
                }
            }
            this.colors=colors;
            setBackGround(colors[0][0],colors[0][1]);
        }
    }

    private void setBackGround(int topColor,int bottomColor) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{topColor, bottomColor}));
        } else {
            setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{topColor, bottomColor}));
        }
    }


    public void setAnimFraction(int level,float fraction) {
        setBackGround(evaluate(fraction,colors[level][0],colors[level+1][0]),evaluate(fraction,colors[level][1],colors[level+1][1]));
    }


    private  int evaluate(float fraction, int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24);
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24);
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) | ((startG + (int) (fraction * (endG - startG))) << 8)
                | ((startB + (int) (fraction * (endB - startB))));
    }


}
