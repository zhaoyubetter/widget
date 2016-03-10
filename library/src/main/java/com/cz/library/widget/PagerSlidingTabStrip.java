package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cz.library.R;
import com.cz.library.util.Utils;
import com.nineoldandroids.view.ViewHelper;

/**
 * 更改自github#PagerSlidingTabStrip，配合viewpager使用。
 *
 * Created by cz on 2015/1/20
 * @see RecyclerTabLayout recyclerView的实现,更为合理
 */
public class PagerSlidingTabStrip extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private static final int DEFAULT_ITEM_PADDING = 15;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 14;
    private static final int INDICATOR_HEIGHT = Utils.dip2px(2);
    private Drawable indicatorDrawable;//指示器对象
    private int textColor;// 文字默认颜色
    private int textSelectColor;// 文字选中颜色
    private int dividerColor;// 分隔线颜色
    private int underlineColor;// 底部绘制线颜色
    private int itemHorizontalPadding;// 条目左右边距
    private int itemVerticalPadding;// 条目上下边距
    private DivideLinearLayout tabsContainer;// HorizontalScrollView容器
    private int tabCount;
    private int position;
    private int selectPosition;
    private float scale;
    private float positionOffset;
    private float indicatorPadding;
    private ViewPager viewPager;
    private int mScrollOffset;// 移动偏移量
    private int lastScrollX;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private boolean isExpand;
    private float textSize;//字体大小
    private Paint paint;

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        paint = new Paint();
        tabsContainer = new DivideLinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        initAttrs(context, attrs);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context) {
        this(context, null, 0);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        setIndicatorColor(a.getColor(R.styleable.PagerSlidingTabStrip_ps_indicatorImage, Color.GRAY));
        setIndicatorImage(a.getDrawable(R.styleable.PagerSlidingTabStrip_ps_indicatorImage));
        setTextColor(a.getColor(R.styleable.PagerSlidingTabStrip_ps_textColor, Color.DKGRAY));
        setTextSelectColor(a.getColor(R.styleable.PagerSlidingTabStrip_ps_textSelectColor, Color.GRAY));
        setItemHorizontalPadding(a.getDimension(R.styleable.PagerSlidingTabStrip_ps_itemHorizontalPadding, Utils.dip2px(DEFAULT_ITEM_PADDING)));
        setItemVerticalPadding(a.getDimension(R.styleable.PagerSlidingTabStrip_ps_itemVerticalPadding, Utils.dip2px(DEFAULT_ITEM_PADDING)));
        setIndicatorPadding(a.getDimension(R.styleable.PagerSlidingTabStrip_ps_indicatorPadding, 0));
        setItemDivideColor(a.getColor(R.styleable.PagerSlidingTabStrip_ps_itemDivideColor, Color.TRANSPARENT));
        setUnderlineColor(a.getColor(R.styleable.PagerSlidingTabStrip_ps_underlineColor, Color.TRANSPARENT));
        setTabScale(a.getFloat(R.styleable.PagerSlidingTabStrip_ps_tabScale, 0f));
        setExpand(a.getBoolean(R.styleable.PagerSlidingTabStrip_ps_expand, false));
        setTextSize((int) a.getDimension(R.styleable.PagerSlidingTabStrip_ps_textSize, Utils.sp2px(TAB_VIEW_TEXT_SIZE_SP)));
        a.recycle();
    }


    /**
     * 设置指示器边距
     *
     * @param padding
     */
    private void setIndicatorPadding(float padding) {
        this.indicatorPadding = padding;
        invalidate();
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
        updateTabs();
    }

    /**
     * 更新标题
     */
    private void updateTabs() {
        LinearLayout tabsContainer = this.tabsContainer;
        int childCount = tabsContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = tabsContainer.getChildAt(i);
            if (childView instanceof TextView) {
                ((TextView) childView).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        }
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public void setTextSelectColor(int color) {
        this.textSelectColor = color;
    }

    public void setItemDivideColor(int color) {
        this.dividerColor = color;
        invalidate();
    }

    public void setUnderlineColor(int color) {
        this.underlineColor = color;
        invalidate();
    }

    public void setItemHorizontalPadding(float padding) {
        this.itemHorizontalPadding = (int) padding;
    }

    public void setItemVerticalPadding(float padding) {
        this.itemVerticalPadding = (int) padding;
    }

    /**
     * 设置底部滑块颜色
     *
     * @param color
     */
    public void setIndicatorColor(int color) {
        setIndicatorImage(new ColorDrawable(color));
    }

    public void setIndicatorImage(Drawable drawable) {
        indicatorDrawable = drawable;
        invalidate();
    }


    public void setTabScale(float scale) {
        this.scale = scale;
    }

    /**
     * 是否展开条目
     *
     * @param isExpand
     */
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && null != viewPager) {
            PagerAdapter adapter = viewPager.getAdapter();
            if (null != adapter) {
                tabsContainer.removeAllViews();
                viewPager.addOnPageChangeListener(this);
                addPagerTabs(viewPager);
            }
        }
    }

    private void addPagerTabs(final ViewPager viewPager) {
        PagerAdapter adapter = viewPager.getAdapter();
        tabCount = adapter.getCount();
        int width = getWidth() / tabCount;
        for (int i = 0; i < tabCount; i++) {
            TextView tab = new TextView(getContext());
            tab.setGravity(Gravity.CENTER);
            tab.setText(adapter.getPageTitle(i));
            tab.setSingleLine();
            tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tab.setTextColor(0 != i ? textColor : textSelectColor);
            tab.setPadding(itemHorizontalPadding, itemVerticalPadding, itemHorizontalPadding, itemVerticalPadding);
            final int position = i;
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(position);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(isExpand ? width : LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            if (0 == i) {
                ViewHelper.setScaleX(tab, 1f + scale);
                ViewHelper.setScaleY(tab, 1f + scale);
            }
            tabsContainer.addView(tab, params);
        }

    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.pageChangeListener = listener;
    }

    /**
     * 设置标题选中状态
     *
     * @param position
     */
    public void setTabTextColor(int position, boolean selected) {
        TextView textView = (TextView) tabsContainer.getChildAt(position);
        if (null != textView) {
            textView.setTextColor(selected ? textSelectColor : textColor);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画移动标记
        final int height = getHeight();
        View view = tabsContainer.getChildAt(position);
        View nextView = tabsContainer.getChildAt(position + 1);
        float divideHeight = getResources().getDimension(R.dimen.divideSize);
        if (null != view) {
            int left = view.getLeft();
            int right = view.getRight();
            if (null != nextView) {
                left = (int) (positionOffset * nextView.getLeft() + (1.0f - positionOffset) * left);
                right = (int) (positionOffset * nextView.getRight() + (1.0f - positionOffset) * right);
            }
            paint.setColor(underlineColor);
            // 绘底部线
            paint.setStrokeWidth(divideHeight);
            canvas.drawLine(0, height, getWidth(), height, paint);
            // 画矩阵
            indicatorDrawable.setBounds(left + (int) indicatorPadding, height - INDICATOR_HEIGHT,
                    right - (int) indicatorPadding, height);
            indicatorDrawable.draw(canvas);
        }
        // 画divider
        paint.setColor(dividerColor);
        paint.setStrokeWidth(divideHeight);
        int childCount = tabsContainer.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View childView = tabsContainer.getChildAt(i);
            int right = childView.getRight();
            canvas.drawLine(right, 10, right, height - 10, paint);
        }
    }

    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }
        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0) {
            newScrollX -= mScrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (null != pageChangeListener) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
        this.position = position;
        this.positionOffset = positionOffset;
        if (position < tabCount) {
            TextView lastView = null;
            TextView textView = null;
            if (position + 1 < tabCount) {
                lastView = (TextView) tabsContainer.getChildAt(position + 1);
            }
            textView = (TextView) tabsContainer.getChildAt(position);
            if (null != lastView) {
                // 设置缩小
                ViewHelper.setScaleX(lastView, 1f + positionOffset * scale);
                ViewHelper.setScaleY(lastView, 1f + positionOffset * scale);
                // 颜色回退
                lastView.setTextColor(evaluate(1f - positionOffset, textSelectColor, textColor));
            }
            // 设置控件放大
            ViewHelper.setScaleX(textView, 1f + ((1f - positionOffset) * scale));
            ViewHelper.setScaleY(textView, 1f + ((1f - positionOffset) * scale));
            // 颜色选中
            textView.setTextColor(evaluate(1f - positionOffset, textColor, textSelectColor));
        }
        // 当位置完成移动完成之后,重置各个tab颜色,否则颜色会显示不正常
        if (selectPosition == position && 0 == this.positionOffset) {
            for (int i = 0; i < tabCount; i++) {
                setTabTextColor(i, i == position);
            }
        }
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (null != pageChangeListener) {
            pageChangeListener.onPageSelected(position);
        }
        this.selectPosition = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            scrollToChild(position, 0);
        }
        if (null != pageChangeListener) {
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private int evaluate(float fraction, int startValue, int endValue) {
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

        return ((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) | ((startG + (int) (fraction * (endG - startG))) << 8)
                | ((startB + (int) (fraction * (endB - startB))));
    }

}
