package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.cz.library.R;


/**
 * Created by cz on 2015/3/7.
 * 组合各种状态布局,局部空布局,网络error,加载布局体等.
 */
public class TemplateView extends RelativeLayout {
    //各种状态桢标志
    private static final String TAG = "TemplateView";
    public final static int CONTAINER = 0;
    public final static int PROGRESS = 1;
    public final static int DISPLAY = 2;
    public final static int ERROR = 3;
    public final static int CUSTOM = 4;
    private RelativeLayout container;
    private Runnable frameAction;
    private int templateCount;
    private int lastFrame;// 上一次布局显示状态

    @IntDef({CONTAINER, PROGRESS, DISPLAY, ERROR, CUSTOM})
    public @interface Frame {
    }

    public TemplateView(Context context) {
        this(context, null, R.attr.template);
    }

    public TemplateView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.template);
    }

    public TemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        container = new RelativeLayout(context);
        addView(container, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TemplateView, R.attr.template, R.style.Template);
        inflateLayout(a.getResourceId(R.styleable.TemplateView_fv_progressLayout, NO_ID));
        inflateLayout(a.getResourceId(R.styleable.TemplateView_fv_displayLayout, NO_ID));
        inflateLayout(a.getResourceId(R.styleable.TemplateView_fv_errorLayout, NO_ID));
        inflateLayout(a.getResourceId(R.styleable.TemplateView_fv_customLayout, NO_ID));
        a.recycle();
        templateCount = getChildCount();//记录模板个数
        setFrame(CONTAINER, true, 0);//初始显示页面桢
    }

    /**
     * inflate the layout
     *
     * @param layout
     */
    private void inflateLayout(@LayoutRes int layout) {
        if (NO_ID != layout) {
            int index = getChildCount();
            inflate(getContext(), layout, this);
            getChildAt(index).setVisibility(View.GONE);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //这里,自定义的layout才解析完,这时能拿到用户自定义的xml布局体,他的位置在最后
        //这里不能用i++,因为removeView之后,角标自动前移.就能拿到后面的view
        for (int i = templateCount; i < getChildCount(); ) {
            View childView = getChildAt(i);
            removeView(childView);
            container.addView(childView);
        }
    }

    /**
     * 是否为指定frame
     *
     * @param frame
     * @return
     */
    public boolean isFrame(@Frame int frame) {
        return lastFrame == frame;
    }

    /**
     * 设置显示桢
     *
     * @param frame @see #PROGRESS/#CONTAINER...
     */
    public void setFrame(@Frame int frame, final boolean animate, long delayTime) {
        setFrame(frame, animate, false, delayTime);
    }

    /**
     * 设置显示桢
     *
     * @param frame @see #PROGRESS/#CONTAINER...
     */
    public void setFrame(@Frame int frame, long delayTime) {
        setFrame(frame, true, false, delayTime);
    }

    public void setForceFrame(@Frame int frame, long delayTime) {
        setFrame(frame, true, true, delayTime);
    }

    /**
     * 强制设定当前桢
     *
     * @param frame
     * @param animator
     */
    public void setForceFrame(int frame, boolean animator, long delayTime) {
        setFrame(frame, animator, true, delayTime);
    }


    /**
     * 设置展示桢view
     *
     * @param frame   当前展示桢
     * @param animate 渐变出现
     */
    private void setFrame(final int frame, final boolean animate, boolean check, long delayTime) {
        if (null != container && (check || frame != lastFrame)) {
            //防止重复设置
            if (null != frameAction) {
                removeCallbacks(frameAction);
            }
            View showView = getChildAt(frame);
            View closeView = getChildAt(lastFrame);
            closeView.setVisibility(View.GONE);//隐藏
            showView.setVisibility(View.VISIBLE);
            if (Build.VERSION_CODES.HONEYCOMB < Build.VERSION.SDK_INT && animate) {
                ViewCompat.setAlpha(showView, 0f);
                ViewCompat.animate(showView).alpha(1f);
            } else {
                showView.clearAnimation();
                closeView.clearAnimation();
            }
            lastFrame = frame;

        }
    }


}
