/**
 * Copyright (C) 2015 nshmura
 * Copyright (C) 2015 The Android Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cz.library.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.cz.library.R;


public class RecyclerTabLayout extends RecyclerView {
    protected static final long DEFAULT_SCROLL_DURATION = 200;
    protected static final float DEFAULT_POSITION_THRESHOLD = 1f;
    protected Paint mIndicatorPaint;
    protected int mTabMinWidth;
    protected int mTabMaxWidth;
    protected int mIndicatorHeight;
    protected float mIndicatorPadding;
    protected float mTabScale;

    protected LinearLayoutManager mLinearLayoutManager;
    protected RecyclerOnScrollListener mRecyclerOnScrollListener;
    private ViewPager.OnPageChangeListener mListener;
    protected ViewPager mViewPager;
    protected Adapter<?> mAdapter;

    protected int mIndicatorPositoin;
    protected int mIndicatorOffset;
    protected int mScrollOffset;
    protected float mOldPositionOffset;
    protected float mPositionThreshold;
    protected boolean mRequestScrollToTab;
    private int mTabSelectColor;
    private int mTabDefaultColor;

    public RecyclerTabLayout(Context context) {
        this(context, null);
    }

    public RecyclerTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mIndicatorPaint = new Paint();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(mLinearLayoutManager);
        setItemAnimator(null);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerTabLayout);
        setTabIndicatorColor(a.getColor(R.styleable.RecyclerTabLayout_rtl_tabIndicatorColor, 0));
        setTabIndicatorHeight(a.getDimensionPixelSize(R.styleable.RecyclerTabLayout_rtl_tabIndicatorHeight, 0));
        setTabDefaultColor(a.getColor(R.styleable.RecyclerTabLayout_rtl_tabDefaultColor, Color.WHITE));
        setTabSelectColor(a.getColor(R.styleable.RecyclerTabLayout_rtl_tabSelectColor, Color.GREEN));
        setTabIndicatorPadding(a.getDimension(R.styleable.RecyclerTabLayout_rtl_tabIndicatorPadding, 0));

        setTabScale(a.getFloat(R.styleable.RecyclerTabLayout_rtl_tabScale, 0));
        a.recycle();

        mPositionThreshold = DEFAULT_POSITION_THRESHOLD;
    }

    /**
     * 设置tab条目的缩放值
     *
     * @param scale
     */
    public void setTabScale(float scale) {
        this.mTabScale = scale;
    }


    @Override
    protected void onDetachedFromWindow() {
        if (mRecyclerOnScrollListener != null) {
            removeOnScrollListener(mRecyclerOnScrollListener);
            mRecyclerOnScrollListener = null;
        }
        super.onDetachedFromWindow();
    }


    public void setTabIndicatorColor(int color) {
        mIndicatorPaint.setColor(color);
        invalidate();
    }

    public void setTabIndicatorHeight(int indicatorHeight) {
        mIndicatorHeight = indicatorHeight;
    }

    /**
     * 设置指示器内边距
     *
     * @param padding
     */
    public void setTabIndicatorPadding(float padding) {
        this.mIndicatorPadding = padding;
    }

    public void setAutoSelectionMode(boolean autoSelect) {
        if (mRecyclerOnScrollListener != null) {
            removeOnScrollListener(mRecyclerOnScrollListener);
            mRecyclerOnScrollListener = null;
        }
        if (autoSelect) {
            mRecyclerOnScrollListener = new RecyclerOnScrollListener(this, mLinearLayoutManager);
            addOnScrollListener(mRecyclerOnScrollListener);
        }
    }

    public void setPositionThreshold(float positionThreshold) {
        mPositionThreshold = positionThreshold;
    }

    public void setUpWithAdapter(Adapter<?> adapter) {
        mAdapter = adapter;
        mAdapter.setTabScale(mTabScale);
        mAdapter.setSelectColor(mTabSelectColor);
        mAdapter.setDefaultColor(mTabDefaultColor);
        mViewPager = adapter.getViewPager();
        if (mViewPager.getAdapter() == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        }
        mViewPager.setOnPageChangeListener(new ViewPagerOnPageChangeListener(this));
        setAdapter(adapter);
        scrollToTab(mViewPager.getCurrentItem());
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, smoothScroll);
            scrollToTab(mViewPager.getCurrentItem());
            return;
        }

        if (smoothScroll && position != mIndicatorPositoin) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                startAnimation(position);
            } else {
                scrollToTab(position); //FIXME add animation
            }
        } else {
            scrollToTab(position);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void startAnimation(final int position) {

        float distance = 1;

        View view = mLinearLayoutManager.findViewByPosition(position);
        if (view != null) {
            float currentX = view.getX() + view.getMeasuredWidth() / 2.f;
            float centerX = getMeasuredWidth() / 2.f;
            distance = Math.abs(centerX - currentX) / view.getMeasuredWidth();
        }

        ValueAnimator animator;
        if (position < mIndicatorPositoin) {
            animator = ValueAnimator.ofFloat(distance, 0);
        } else {
            animator = ValueAnimator.ofFloat(-distance, 0);
        }
        animator.setDuration(DEFAULT_SCROLL_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollToTab(position, (float) animation.getAnimatedValue(), true);
            }
        });
        animator.start();
    }

    protected void scrollToTab(int position) {
        scrollToTab(position, 0, false);
        mAdapter.setCurrentIndicatorPosition(position);
        mAdapter.notifyDataSetChanged();
    }

    protected void scrollToTab(int position, float positionOffset, boolean fitIndicator) {
        int scrollOffset = 0;

        View selectedView = mLinearLayoutManager.findViewByPosition(position);
        View nextView = mLinearLayoutManager.findViewByPosition(position + 1);

        if (selectedView != null) {
            int width = getMeasuredWidth();
            float scroll1 = width / 2.f - selectedView.getMeasuredWidth() / 2.f;

            if (nextView != null) {
                float scroll2 = width / 2.f - nextView.getMeasuredWidth() / 2.f;

                float scroll = scroll1 + (selectedView.getMeasuredWidth() - scroll2);
                float dx = scroll * positionOffset;
                scrollOffset = (int) (scroll1 - dx);

                mScrollOffset = (int) dx;
                mIndicatorOffset = (int) ((scroll1 - scroll2) * positionOffset);

            } else {
                scrollOffset = (int) scroll1;
                mScrollOffset = 0;
                mIndicatorOffset = 0;
            }
            if (fitIndicator) {
                mScrollOffset = 0;
                mIndicatorOffset = 0;
            }

            if (mAdapter != null && mIndicatorPositoin == position) {
                updateCurrentIndicatorPosition(position, positionOffset - mOldPositionOffset,
                        positionOffset);
            }

            mIndicatorPositoin = position;

        } else {
            if (getMeasuredWidth() > 0 && mTabMinWidth == mTabMaxWidth) { //fixed size
                int width = mTabMinWidth;
                int offset = (int) (positionOffset * -width);
                int leftOffset = (int) ((getMeasuredWidth() - width) / 2.f);
                scrollOffset = offset + leftOffset;
            }
            mRequestScrollToTab = true;
        }

        if (null != mAdapter) {
            mAdapter.onTabScroll(selectedView, nextView, positionOffset, mTabScale, mTabSelectColor, mTabDefaultColor);
        }

        mLinearLayoutManager.scrollToPositionWithOffset(position, scrollOffset);

        if (mIndicatorHeight > 0) {
            invalidate();
        }

        if (null != mAdapter) {
            mAdapter.setAdapterPosition(position);
            int adapterPosition = mAdapter.getAdapterPosition();
            if (position == adapterPosition && 0 == positionOffset) {
                mAdapter.notifyDataSetChanged();
            }
        }

        mOldPositionOffset = positionOffset;
    }

    protected void updateCurrentIndicatorPosition(int position, float dx, float positionOffset) {
        int indicatorPosition = -1;
        if (dx > 0 && positionOffset >= mPositionThreshold) {
            indicatorPosition = position + 1;

        } else if (dx < 0 && positionOffset <= 1 - mPositionThreshold) {
            indicatorPosition = position;
        }
        if (indicatorPosition >= 0 && indicatorPosition != mAdapter.getCurrentIndicatorPosition()) {
            mAdapter.setCurrentIndicatorPosition(indicatorPosition);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        View view = mLinearLayoutManager.findViewByPosition(mIndicatorPositoin);
        if (view == null) {
            if (mRequestScrollToTab) {
                mRequestScrollToTab = false;
                scrollToTab(mViewPager.getCurrentItem());
            }
            return;
        }
        mRequestScrollToTab = false;

        int left = view.getLeft() + mScrollOffset - mIndicatorOffset;
        int right = view.getRight() + mScrollOffset + mIndicatorOffset;
        int top = getHeight() - mIndicatorHeight;
        int bottom = getHeight();
        canvas.drawRect(left + mIndicatorPadding, top, right - mIndicatorPadding, bottom, mIndicatorPaint);
    }

    public void setTabSelectColor(int color) {
        this.mTabSelectColor = color;
        if (null != mAdapter) {
            mAdapter.setSelectColor(color);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setTabDefaultColor(int color) {
        this.mTabDefaultColor = color;
        if (null != mAdapter) {
            mAdapter.setDefaultColor(color);
        }
    }

    protected static class RecyclerOnScrollListener extends OnScrollListener {

        protected RecyclerTabLayout mRecyclerTabLayout;
        protected LinearLayoutManager mLinearLayoutManager;

        public RecyclerOnScrollListener(RecyclerTabLayout recyclerTabLayout,
                                        LinearLayoutManager linearLayoutManager) {
            mRecyclerTabLayout = recyclerTabLayout;
            mLinearLayoutManager = linearLayoutManager;
        }

        public int mDx;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            mDx += dx;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case SCROLL_STATE_IDLE:
                    if (mDx > 0) {
                        selectCenterTabForRightScroll();
                    } else {
                        selectCenterTabForLeftScroll();
                    }
                    mDx = 0;
                    break;
                case SCROLL_STATE_DRAGGING:
                case SCROLL_STATE_SETTLING:
            }
        }

        protected void selectCenterTabForRightScroll() {
            int first = mLinearLayoutManager.findFirstVisibleItemPosition();
            int last = mLinearLayoutManager.findLastVisibleItemPosition();
            int center = mRecyclerTabLayout.getWidth() / 2;
            for (int position = first; position <= last; position++) {
                View view = mLinearLayoutManager.findViewByPosition(position);
                if (view.getLeft() + view.getWidth() >= center) {
                    mRecyclerTabLayout.setCurrentItem(position, false);
                    break;
                }
            }
        }

        protected void selectCenterTabForLeftScroll() {
            int first = mLinearLayoutManager.findFirstVisibleItemPosition();
            int last = mLinearLayoutManager.findLastVisibleItemPosition();
            int center = mRecyclerTabLayout.getWidth() / 2;
            for (int position = last; position >= first; position--) {
                View view = mLinearLayoutManager.findViewByPosition(position);
                if (view.getLeft() <= center) {
                    mRecyclerTabLayout.setCurrentItem(position, false);
                    break;
                }
            }
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mListener = listener;
    }

    protected class ViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final RecyclerTabLayout mRecyclerTabLayout;
        private int mScrollState;

        public ViewPagerOnPageChangeListener(RecyclerTabLayout recyclerTabLayout) {
            mRecyclerTabLayout = recyclerTabLayout;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mRecyclerTabLayout.scrollToTab(position, positionOffset, false);
            if (null != mListener) {
                mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
            if (null != mListener) {
                mListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                if (mRecyclerTabLayout.mIndicatorPositoin != position) {
                    mRecyclerTabLayout.scrollToTab(position);
                }
            }
            if (null != mListener) {
                mListener.onPageSelected(position);
            }
        }


    }


    public static abstract class Adapter<T extends ViewHolder>
            extends RecyclerView.Adapter<T> {
        protected ViewPager mViewPager;
        protected int mIndicatorPosition;
        protected int mAdapterPosition;
        protected float mTabScale;
        protected int selectColor;
        protected int defaultColor;

        public Adapter(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        public ViewPager getViewPager() {
            return mViewPager;
        }

        public void setCurrentIndicatorPosition(int indicatorPosition) {
            mIndicatorPosition = indicatorPosition;
        }

        public int getCurrentIndicatorPosition() {
            return mIndicatorPosition;
        }

        public void setSelectColor(int color) {
            this.selectColor = color;
        }


        public void setDefaultColor(int color) {
            this.defaultColor = color;
        }

        private void setTabScale(float scale) {
            this.mTabScale = scale;
        }

        public float getTabScale() {
            return mTabScale;
        }

        public void setAdapterPosition(int position) {
            mAdapterPosition = position;
        }

        public int getAdapterPosition() {
            return mAdapterPosition;
        }

        @Override
        public void onBindViewHolder(T holder, int position) {
            onBindViewHolder(holder, position, selectColor, defaultColor);
        }

        public abstract void onBindViewHolder(T holder, int position, int selectColor, int defaultColor);

        /**
         * 当页滑动时
         *
         * @param offset
         */
        public abstract void onTabScroll(View lastView, View selectView, float offset, float scale, int selectColor, int defaultColor);
    }
}