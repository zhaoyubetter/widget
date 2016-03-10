package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cz.library.R;
import com.cz.library.util.Utils;


/**
 * Created by cz on 2015/3/8.
 */
public class TabHost extends DivideLinearLayout {
    private int color;//默认颜色
    private int selectColor;//选中颜色
    private int padding;//spec对象内边距
    private int textSize;//标签文字大小
    private int indicateColor;//指示文字颜色
    private float indicateSize;//指示文字大小
    private int indicateBackGround;//指示器背景颜色
    private int position;//选中位置
    private int lastPosition;//上一次点中位置
    private boolean[] showIndicates;
    private float[] indicateFractions;
    private ViewPager viewPager;
    private OnTabItemClickListener listener;
    private Paint paint;

    public TabHost(Context context) {
        this(context, null);
    }

    public TabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabHost);
        setColor(a.getColor(R.styleable.TabHost_th_color, Color.WHITE));
        setSelectColor(a.getColor(R.styleable.TabHost_th_selectColor, Color.BLUE));
        setVerticalPadding((int) a.getDimension(R.styleable.TabHost_th_verticalPadding, Utils.dip2px(8)));
        setTextSize((int) a.getDimension(R.styleable.TabHost_th_textSize, 10));
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            setOnSpecClickListener(getChildAt(i));
        }
    }

    /**
     * 设置文字选中颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
        notifyDataChange();
    }

    /**
     * 设置选中颜色
     *
     * @param selectColor
     */
    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
        notifyDataChange();
    }

    /**
     * 设置spec条目内边距
     *
     * @param padding 内边距
     */
    public void setVerticalPadding(int padding) {
        this.padding = padding;
        notifyDataChange();
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
        notifyDataChange();
    }


    /**
     * 刷新spec数据样式
     */
    private void notifyDataChange() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof TextView) {
                TextView textSpec = ((TextView) childView);
                //重置text Spec的颜色取值
                textSpec.setTextColor(i == lastPosition ? selectColor : color);
                textSpec.setTextSize(textSize);
            } else if (childView instanceof ImageView) {
                //重置image Spec的内边距值
                childView.setPadding(padding, padding, padding, padding);
            }
        }
    }

    /**
     * 添加文字与图片spec
     *
     * @param text          提示文字
     * @param drawableResId 图片引用
     */
    public TextView addTextSpec(@IdRes int id, String text, int drawableResId) {
        Context context = getContext();
        CenterTextView tabSpec = new CenterTextView(context);
        tabSpec.setId(id);
        tabSpec.setCompoundDrawablePadding(Utils.dip2px(4));
        tabSpec.setDrawableMode(CenterTextView.DRAWABLE_TOP);
        tabSpec.setCompoundDrawablesWithIntrinsicBounds(0, drawableResId, 0, 0);
        tabSpec.setTextSize(textSize);
        boolean isSelect = 0 == getChildCount();
        tabSpec.setTextColor(isSelect ? selectColor : color);
        tabSpec.setSelected(isSelect);
        tabSpec.setText(text);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
        layoutParams.gravity = Gravity.CENTER;
        addView(tabSpec, layoutParams);
        setOnSpecClickListener(tabSpec);
        return tabSpec;
    }


    /**
     * 添加图片spec
     *
     * @param drawableResId
     */
    public void addImageSpec(int drawableResId) {
        Context context = getContext();
        ImageView tabSpec = new ImageView(context);
        tabSpec.setPadding(padding, padding, padding, padding);
        tabSpec.setScaleType(ImageView.ScaleType.FIT_CENTER);
        tabSpec.setImageResource(drawableResId);
        tabSpec.setSelected(0 == getChildCount());
        addView(tabSpec, new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
        setOnSpecClickListener(tabSpec);
    }

    /**
     * 获得spec总数
     *
     * @return 总数
     */
    public int getCount() {
        return getChildCount();
    }

    /**
     * 设置spec点击事件
     *
     * @param tabSpec
     */
    private void setOnSpecClickListener(View tabSpec) {
        if (null != tabSpec) {
            tabSpec.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = indexOfChild(v);
                    if (null != listener) {
                        if (position != lastPosition) {
                            setSelectPosition(position, true);
                            setSelectPosition(lastPosition, false);
                        }
                        listener.onTabItemClick(v, position, lastPosition);
                        lastPosition = position;
                    }
                    if (null != viewPager) {
                        viewPager.setCurrentItem(position);
                    }
                }
            });
        }
    }


    /**
     * 设置指定tab选中tab
     *
     * @param position 指定tab
     * @param isSelect 当前view是否选中
     */
    public void setSelectPosition(int position, boolean isSelect) {
        if (position >= getChildCount()) return;
        View selectView = getChildAt(position);
        if (selectView instanceof TextView) {
            //之所以不用colorStateDrawable,是因为用颜色偏移,可以实现颜色渐变,
            selectView.setSelected(isSelect);
            ((TextView) selectView).setTextColor(isSelect ? selectColor : color);
        } else if (selectView instanceof ImageView) {
            selectView.setSelected(isSelect);
            selectView.setSelected(isSelect);
        }
    }

    /**
     * 设置tab条目点击事件
     *
     * @param listener 点击回调对象
     */
    public void setOnTabItemClickListener(OnTabItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (null == showIndicates) return;
        paint.setColor(indicateBackGround);
        int width = getWidth();
        int length = showIndicates.length;
        int itemSize = width / length;
        Rect bounds = new Rect();
        for (int i = 0; i < length; i++) {
            View childView = getChildAt(i);
            if (showIndicates[i] && childView instanceof TextView) {
                TextView textView = (TextView) childView;
                TextPaint paint = textView.getPaint();
                String text = textView.getText().toString();
                paint.getTextBounds(text, 0, text.length(), bounds);
                int textWidth = bounds.width();
                canvas.drawCircle(i * itemSize + itemSize / 2 + textWidth / 2 + indicateSize, indicateSize * 2, indicateSize, this.paint);
            }
        }
    }


    /**
     * tab条目点击监听回调接口
     */
    public interface OnTabItemClickListener {
        /**
         * tab条目点击事件
         *
         * @param v        点击view对象
         * @param position 点击位置
         */
        void onTabItemClick(View v, int position, int lastPosition);
    }
}
