package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cz.library.R;

import java.util.List;

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
            childView.layout(paddingLeft, top, paddingRight, top + childView.getMeasuredHeight());
        }
    }

    public void setAdapter(Adapter adapter){
        removeAllViews();
        int itemCount = adapter.getItemCount();
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        for(int i=0;i<itemCount;i++){
            LinearLayout itemLayout= (LinearLayout) layoutInflater.inflate(R.layout.edit_layout_item, this, false);
            SearchView searchView= (SearchView) itemLayout.findViewById(R.id.search_view);
            searchView.setHintDrawableResource(adapter.getHintDrawableResource(i));
            searchView.setSearchHint(adapter.getHint(i));
            searchView.setSearchInputType(adapter.getInputType(i));
            searchView.setSearchTextColor(textColor);
            searchView.setSearchTextSize(textSize);
            View extraView = adapter.getExtraView(i);
            if(null!=extraView){
                itemLayout.addView(extraView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }
        requestLayout();
    }

    public Editable getItemValue(int position){
        Editable text=null;
        int childCount = getChildCount();
        if(position<childCount){
            View childView = getChildAt(position);
            SearchView searchView= (SearchView) childView.findViewById(R.id.search_view);
            text=searchView.getText();
        }
        return text;
    }

    public static abstract class Adapter{
        public abstract int getItemCount();
        public abstract String getHint(int position);
        public abstract int getHintDrawableResource(int position);
        public abstract View getExtraView(int position);
//        editor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
//        editor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//        editor.setInputType(InputType.TYPE_CLASS_TEXT);
        public abstract int getInputType(int position);
    }

    public interface OnResultMatchListener{
        void onMatch(List<Editable> items);
    }

    public interface OnExtraItemClickListener{
        void onItemClickListener(View v,int index);
    }
}
