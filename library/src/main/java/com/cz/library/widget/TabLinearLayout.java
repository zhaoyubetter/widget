package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.cz.library.R;
import com.cz.library.util.Utils;

import xyqb.library.AnimatorCompat;
import xyqb.library.AnimatorListenerAdapter;
import xyqb.library.AnimatorUpdateListener;

/**
 * Created by cz on 15/8/21.
 * 一个自定义LinearLayout,增加tab控制切换
 * tabWidth为0时,依靠verticalPadding设定大小,否则依靠tagWidth为主
 */
public class TabLinearLayout extends DivideLinearLayout {
    private static final int STRIP = 0x00;
    private static final int CLIP = 0x01;
    private static final int FULL = 0x02;
    private static final String TAG = "TabLinearLayout";
    private Drawable tabDrawable;
    private int tabWidth;
    private int tabHeight;
    private int horizontalPadding;
    private int verticalPadding;
    private float fraction;//执行进度
    private int tabMode;
    private AnimatorCompat.Animator animator;
    private OnSelectListener listener;
    private int selectPosition;//选中位置
    private int lastPosition;//上一次选中位置

    public TabLinearLayout(Context context) {
        this(context, null);
    }

    public TabLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        setClipChildren(false);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLinearLayout);
        setTabImage(a.getDrawable(R.styleable.TabLinearLayout_tll_tabImage));
        setTabWidth((int) a.getDimension(R.styleable.TabLinearLayout_tll_tabWidth, 0));
        setTabHeight((int) a.getDimension(R.styleable.TabLinearLayout_tll_tabHeight, Utils.dip2px(2)));
        setTabMode(a.getInt(R.styleable.TabLinearLayout_tll_tabMode, STRIP));
        setHorizontalPadding((int) a.getDimension(R.styleable.TabLinearLayout_tll_horizontalPadding, 0));
        setVerticalPadding((int) a.getDimension(R.styleable.TabLinearLayout_tll_verticalPadding, 0));
        a.recycle();

    }


    public void setHorizontalPadding(int padding) {
        this.horizontalPadding = padding;
        invalidate();
    }

    public void setVerticalPadding(int padding) {
        this.verticalPadding = padding;
        invalidate();
    }

    public void setTabWidth(int width) {
        this.tabWidth = width;
        invalidate();
    }

    public void setTabHeight(int height) {
        this.tabHeight = height;
        invalidate();
    }

    public void setTabImage(Drawable drawable) {
        tabDrawable = drawable;
        invalidate();
    }

    public void setTabMode(int mode) {
        this.tabMode = mode;
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup viewParent = (ViewGroup) parent;
            viewParent.setClipChildren(CLIP != mode);
        }
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup viewParent = (ViewGroup) parent;
            viewParent.setClipChildren(CLIP != tabMode);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final int index = i;
            View childView = getChildAt(i);
            childView.setSelected(i == selectPosition);
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectPosition(index);
                }
            });
        }
    }

    /**
     * 选中其中一个条目
     *
     * @param index
     */
    public void setSelectPosition(int index) {
        if (index != selectPosition) {
            if (null != animator) {
                animator.cancel();
            }
            selectPosition = index;
            animator = AnimatorCompat.ofFloat(1f);

            animator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(AnimatorCompat.Animator animation, float fraction) {
                    TabLinearLayout.this.fraction = animation.getAnimatedFraction();
                    invalidate();
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(AnimatorCompat.Animator animation) {
                    lastPosition = selectPosition;
                }
            });
            this.animator.start();
            View selectView = getChildAt(selectPosition);
            View lastView = getChildAt(lastPosition);
            if (null != selectView) {
                selectView.setSelected(true);
            }
            if (null != lastView) {
                lastView.setSelected(false);
            }
            //回调监听
            if (null != listener) {
                listener.onSelectTab(selectView, selectPosition, lastPosition);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int childCount = getChildCount();
        if (0 >= childCount || null == tabDrawable) return;
        //在控件之上
        View lastView = getChildAt(lastPosition);
        View selectView = getChildAt(selectPosition);
        Rect outRect1 = new Rect();
        Rect outRect2 = new Rect();
        lastView.getHitRect(outRect1);
        selectView.getHitRect(outRect2);
        tabDrawable.setBounds(getBoundLeft(outRect1, outRect2),
                getBoundTop(outRect1, outRect2),
                getBoundRight(outRect1, outRect2),
                getBoundBottom(outRect1, outRect2));
        tabDrawable.draw(canvas);
    }


    private int getBoundLeft(Rect rect1, Rect rect2) {
        int orientation = getOrientation();
        int start = 0, end = 0;
        if (HORIZONTAL == orientation) {
            if (0 < tabWidth) {
                //width决定
                start = rect1.centerX() - tabWidth / 2;
                end = rect2.centerX() - tabWidth / 2;
            } else {
                //内padding决定
                start = rect1.left + horizontalPadding;
                end = rect2.left + horizontalPadding;
            }
        } else if (VERTICAL == orientation) {
            //纵向左边距
            switch (tabMode) {
                case STRIP:
                    //width决定,靠底
                    start = rect1.right - verticalPadding - tabHeight;
                    end = rect2.right - verticalPadding - tabHeight;
                    break;
                case CLIP:
                    start = rect1.right + verticalPadding;
                    end = rect2.right + verticalPadding;
                    break;
                case FULL:
                    start = rect1.left + verticalPadding;
                    end = rect2.left + verticalPadding;
                    break;
            }
        }
        return start + (int) ((end - start) * fraction);
    }

    /**
     * 获得绘制顶点
     * 获得绘制顶点
     *
     * @param rect1
     * @param rect2
     * @return
     */
    private int getBoundTop(Rect rect1, Rect rect2) {
        int start = 0, end = 0;
        int orientation = getOrientation();
        if (HORIZONTAL == orientation) {
            switch (tabMode) {
                case STRIP:
                    //width决定,靠底
                    start = rect1.bottom - verticalPadding - tabHeight;
                    end = rect2.bottom - verticalPadding - tabHeight;
                    break;
                case CLIP:
                    start = rect1.bottom + verticalPadding;
                    end = rect2.bottom + verticalPadding;
                    break;
                case FULL:
                    start = rect1.top + verticalPadding;
                    end = rect2.top + verticalPadding;
                    break;
            }
        } else if (VERTICAL == orientation) {
            //width决定,靠底
            if (0 < tabWidth) {
                start = rect1.centerY() - tabWidth / 2;
                end = rect2.centerY() - tabWidth / 2;
            } else {
                start = rect1.top + horizontalPadding;
                end = rect2.top + horizontalPadding;
            }
        }
        return start + (int) ((end - start) * fraction);
    }

    private int getBoundRight(Rect rect1, Rect rect2) {
        int orientation = getOrientation();
        int start = 0, end = 0;
        if (HORIZONTAL == orientation) {
            if (0 < tabWidth) {
                //width决定
                start = rect1.centerX() + tabWidth / 2;
                end = rect2.centerX() + tabWidth / 2;
            } else {
                //内padding决定
                start = rect1.right - horizontalPadding;
                end = rect2.right - horizontalPadding;
            }
        } else if (VERTICAL == orientation) {
            //纵向左边距
            switch (tabMode) {
                case STRIP:
                    //width决定,靠底
                    start = rect1.right - verticalPadding;
                    end = rect2.right - verticalPadding;
                    break;
                case CLIP:
                    start = rect1.right + verticalPadding + tabHeight;
                    end = rect2.right + verticalPadding + tabHeight;
                    break;
                case FULL:
                    start = rect1.right - verticalPadding;
                    end = rect2.right - verticalPadding;
                    break;
            }
        }
        return start + (int) ((end - start) * fraction);
    }

    private int getBoundBottom(Rect rect1, Rect rect2) {
        int start = 0, end = 0;
        int orientation = getOrientation();
        if (HORIZONTAL == orientation) {
            switch (tabMode) {
                case STRIP:
                    //width决定,靠底
                    start = rect1.bottom - verticalPadding;
                    end = rect2.bottom - verticalPadding;
                    break;
                case CLIP:
                    start = rect1.bottom + verticalPadding + tabHeight;
                    end = rect2.bottom + verticalPadding + tabHeight;
                    break;
                case FULL:
                    start = rect1.bottom - verticalPadding;
                    end = rect2.bottom - verticalPadding;
                    break;
            }
        } else if (VERTICAL == orientation) {
            //width决定,靠底
            if (0 < tabWidth) {
                start = rect1.centerY() + tabWidth / 2;
                end = rect2.centerY() + tabWidth / 2;
            } else {
                start = rect1.bottom - horizontalPadding;
                end = rect2.bottom - horizontalPadding;
            }
        }
        return start + (int) ((end - start) * fraction);
    }


    public void setOnSelectListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public interface OnSelectListener {
        void onSelectTab(View v, int position, int lastPosition);
    }
}
