package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cz.library.R;

/**
 * Created by cz on 9/23/16.
 */
public class EditLayout extends ViewGroup {
    private Adapter adapter;
    private float itemPadding;
    private int textColor;
    private int textSize;
    private float divideSize;
    private Drawable divideDrawable;

    public EditLayout(Context context) {
        this(context, null);
    }

    public EditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        <attr name="el_itemPadding" format="dimension"/>
//        <attr name="el_textColor" format="color"/>
//        <attr name="el_textSize" format="dimension"/>
//        <attr name="el_itemDivideSize" format="dimension"/>
//        <attr name="el_itemDivideDrawable" format="reference"/>

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditLayout);
        setItemPadding(a.getDimension(R.styleable.EditLayout_el_itemPadding,0));
        setTextColor(a.getColor(R.styleable.EditLayout_el_textColor, Color.BLACK));
        setTextSize(a.getDimensionPixelSize(R.styleable.EditLayout_el_itemDivideSize, 0));
        setItemDivideSize(a.getDimension(R.styleable.EditLayout_el_itemDivideSize, 0));
        setItemDivideDrawable(a.getDrawable(R.styleable.EditLayout_el_itemDivideDrawable));

        a.recycle();
    }

    public void setItemPadding(float itemPadding) {
        this.itemPadding=itemPadding;
        requestLayout();
    }

    public void setTextColor(int color) {
        this.textColor=color;
    }

    public void setTextSize(int textSize) {
        this.textSize=textSize;
    }

    public void setItemDivideSize(float divideSize) {
        this.divideSize=divideSize;
        invalidate();
    }

    public void setItemDivideDrawable(Drawable drawable) {
        this.divideDrawable=drawable;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            childView.measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int childCount = getChildCount();
        int top=paddingTop;
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            top+=i*itemPadding;
            childView.layout(paddingLeft,top,paddingRight,top+childView.getMeasuredHeight());
        }
    }

    public void setAdapter(Adapter adapter){
        this.adapter=adapter;
        requestLayout();
    }

    public static abstract class Adapter{
        public abstract int getItemCount();
        public abstract String getHint(int position);
        public abstract String getHintDrawableResource(int position);
        public abstract View getExtraView(int position);
//        editor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
//        editor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        editor.setInputType(InputType.TYPE_CLASS_TEXT);
        public abstract int getInputType(int position);

    }
}
