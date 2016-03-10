package com.cz.library.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cz.library.R;
import com.cz.library.callback.OnCheckedListener;
import com.cz.library.util.Utils;
import com.cz.library.widget.drawable.DrawableBuilder;
import com.cz.library.widget.drawable.RectDrawableBuilder;
import com.cz.library.widget.selector.SelectState;
import com.cz.library.widget.selector.SelectorBuilder;


/**
 * 控件组,可自定义外圈选中样式
 * update 2014/11/5 重构代码,
 * 1:将外观改为用颜色值用代码生成,非drawalbe目录下shspe生成.
 * 2:新增滑块移动动画,难点距边缘时自动圆角化
 * <p/>
 * Created by cz on 2014/5/16
 */
public class OptionGroup extends RadioGroup {
    public static final int GROUP_EDGE = 0;//边缘
    public static final int ITEM_EDGE = 1;//条目

    private static final int TEXT_SIZE = 14;
    private static final int DEFAULT_TEXT_PADDING = 0;
    private static final int DEFAULT_BUTTON_ROUND = 4;
    private final int DEFAULT_COLOR = Color.GREEN;//默认颜色
    private int dividerSize;// 获取分隔线宽度,以dip为单位
    private int dividerColor;// 获取分隔线颜色,默认黑色
    private int backgroundColor;// 获取背景颜色,默认透明
    private ColorStateList colorStateList;
    private int selectColor;// 按钮选中颜色
    private int pressColor;// 按钮按下去颜色
    private int defaultColor;// 默认选中颜色
    private float roundRadius;// 图景圆角
    private int itemHorizontalPadding;// 文字左右边距
    private int itemVerticalPadding;// 文字上下边距
    private float itemTextSize;//子条目文字大小
    private int itemMargin;//设置button的Margin
    private int selectPosition;// 选中位置
    private int edgeMode;//条目边缘模式
    private OnCheckedListener listener;// 选中监听
    private Paint paint;

    @IntDef(value = {GROUP_EDGE, ITEM_EDGE})
    public @interface EdgeMode {
    }

    public OptionGroup(Context context) {
        this(context, null);
    }

    public OptionGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        setOrientation(HORIZONTAL);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OptionGroup);
        setBackGroundColor(a.getColor(R.styleable.OptionGroup_og_backGroundColor, Color.TRANSPARENT));
        setDividerSize((int) a.getDimension(R.styleable.OptionGroup_og_divideSize, Utils.dip2px(1)));
        setDivideColor(a.getColor(R.styleable.OptionGroup_og_divideColor, DEFAULT_COLOR));
        setRoundRadius(a.getDimension(R.styleable.OptionGroup_og_roundRadius, Utils.dip2px(DEFAULT_BUTTON_ROUND)));
        setItemTextSize(a.getDimensionPixelSize(R.styleable.OptionGroup_og_itemTextSize, TEXT_SIZE));
        setItemMargin((int) a.getDimension(R.styleable.OptionGroup_og_itemMargin, 0));
        setItemHorizontalPadding(a.getDimension(R.styleable.OptionGroup_og_itemHorizontalPadding, Utils.dip2px(DEFAULT_TEXT_PADDING)));
        setItemVerticalPadding(a.getDimension(R.styleable.OptionGroup_og_itemVerticalPadding, Utils.dip2px(DEFAULT_TEXT_PADDING)));
        setItemTextSelector(a.getColorStateList(R.styleable.OptionGroup_og_itemTextSelector));
        this.edgeMode = a.getInt(R.styleable.OptionGroup_og_itemEdgeMode, GROUP_EDGE);
        setSelectColor(a.getColor(R.styleable.OptionGroup_og_selectColor, DEFAULT_COLOR));
        setPressColor(a.getColor(R.styleable.OptionGroup_og_pressColor, DEFAULT_COLOR));
        setDefaultColor(a.getColor(R.styleable.OptionGroup_og_defaultColor, Color.TRANSPARENT));
        setItems(a.getTextArray(R.styleable.OptionGroup_og_items));
        a.recycle();
    }

    /**
     * 设置边缘模式
     *
     * @param mode
     */
    public void setItemEdgeMode(@EdgeMode int mode) {
        this.edgeMode = mode;
        refreshItems();
    }

    /**
     * 设置控件组内边距
     *
     * @param radius
     */
    public void setRoundRadius(float radius) {
        this.roundRadius = radius;
        refreshItems();
    }

    /**
     * 设置按钮默认显示颜色
     *
     * @param color
     */
    public void setDefaultColor(int color) {
        defaultColor = color;
        refreshItems();
    }

    /**
     * 设置选中颜色
     *
     * @param color
     */
    public void setSelectColor(int color) {
        selectColor = color;
        refreshItems();
    }

    /**
     * 设置控件按下颜色
     *
     * @param color
     */
    public void setPressColor(int color) {
        pressColor = color;
        refreshItems();
    }

    /**
     * 设置展示数据
     *
     * @param res
     */
    public void setItems(@ArrayRes int res) {
        if (-1 != res) {
            setItems(getResources().getStringArray(res));
        }
    }

    public void setItems(CharSequence[] items) {
        if (null != items) {
            initItems(items);
        }
    }

    /**
     * 设置显示文字大小
     *
     * @param textSize
     */
    public void setItemTextSize(float textSize) {
        this.itemTextSize = textSize;
        refreshItems();
    }

    /**
     * 设置文字左右边距
     *
     * @param padding
     */
    public void setItemHorizontalPadding(float padding) {
        this.itemHorizontalPadding = (int) padding;
        refreshItems();
    }

    /**
     * 设置文字上下边距
     *
     * @param padding
     */
    public void setItemVerticalPadding(float padding) {
        this.itemVerticalPadding = (int) padding;
        refreshItems();
    }

    /**
     * 设置条目文字状态选择器
     *
     * @param stateList
     */
    public void setItemTextSelector(ColorStateList stateList) {
        this.colorStateList = stateList;
        refreshItems();
    }


    public void setItemMargin(int margin) {
        this.itemMargin = margin;
        refreshItems();
    }

    /**
     * 初始化数据
     *
     * @param titles
     */
    private void initItems(final CharSequence[] titles) {
        removeAllViews();
        RadioButton button;
        RadioGroup.LayoutParams layoutParams;
        for (int i = 0, length = titles.length; i < length; i++) {
            layoutParams = new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.MATCH_PARENT, 1f);
            layoutParams.setMargins(itemMargin, itemMargin, itemMargin, itemMargin);
            button = new RadioButton(getContext());
            button.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            button.setGravity(Gravity.CENTER);
            button.setText(titles[i]);
            if (null != colorStateList) {
                button.setTextColor(colorStateList);
            }
            button.setTextSize(itemTextSize);
            setButtonBackground(button, i, length);
            button.setPadding(itemHorizontalPadding, itemVerticalPadding, itemHorizontalPadding, itemVerticalPadding);
            final int index = i;
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != titles) {
                        setChecked(index);
                        if (null != listener) {
                            listener.onChecked(v, index, titles[index]);
                        }
                    }
                }
            });
            addView(button, layoutParams);
        }
        // 默认选中第一个
        setChecked(selectPosition);
    }

    /**
     * 刷新view设置
     */
    private void refreshItems() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof TextView) {
                TextView itemView = (TextView) childView;
                itemView.setTextSize(itemTextSize);
                setButtonBackground(childView, i, childCount);//设置背景
                if (null != colorStateList) {
                    itemView.setTextColor(colorStateList);
                }
                itemView.setPadding(itemHorizontalPadding, itemVerticalPadding, itemHorizontalPadding, itemVerticalPadding);
            }
        }

        RectDrawableBuilder rect = DrawableBuilder.rect();
        if (GROUP_EDGE == edgeMode) {
            rect.setCornersRadius(roundRadius);
        }
        setBackgroundDrawable(rect.setStrokeWidth(dividerSize).
                setStrokeColor(dividerColor).
                setSolidColor(backgroundColor).build());
    }

    /**
     * 根据位置设置字体颜色
     *
     * @param index
     */
    private void setTextColorByPosition(int index, boolean isSelect) {
        final View childView = getChildAt(index);
        if (null != childView && childView instanceof RadioButton) {
            childView.setSelected(isSelect);
        }
    }

    /**
     * 设置按钮背景
     *
     * @param view
     * @param i
     * @param length
     */
    private void setButtonBackground(View view, int i, int length) {
        RectDrawableBuilder pressDrawableBuilder = DrawableBuilder.rect();
        RectDrawableBuilder selectDrawableBuilder = DrawableBuilder.rect();
        RectDrawableBuilder emptyDrawableBuilder = DrawableBuilder.rect();
        if (GROUP_EDGE == edgeMode) {
            if (0 == i) {
                pressDrawableBuilder.setLeftTopRadius(roundRadius).setLeftBottomRadius(roundRadius).setPadding(dividerSize);
                selectDrawableBuilder.setLeftTopRadius(roundRadius).setLeftBottomRadius(roundRadius).setPadding(dividerSize);
                emptyDrawableBuilder.setLeftTopRadius(roundRadius).setLeftBottomRadius(roundRadius).setPadding(dividerSize);
            } else if (i == length - 1) {
                pressDrawableBuilder.setRightTopRadius(roundRadius).setRightBottomRadius(roundRadius).setPadding(dividerSize);
                selectDrawableBuilder.setRightTopRadius(roundRadius).setRightBottomRadius(roundRadius).setPadding(dividerSize);
                emptyDrawableBuilder.setRightTopRadius(roundRadius).setRightBottomRadius(roundRadius).setPadding(dividerSize);
            }
        } else {
            pressDrawableBuilder.setCornersRadius(roundRadius).setPadding(dividerSize);
            selectDrawableBuilder.setCornersRadius(roundRadius).setPadding(dividerSize);
            emptyDrawableBuilder.setCornersRadius(roundRadius).setPadding(dividerSize);
        }
        view.setBackgroundDrawable(new SelectorBuilder().addState(SelectState.PRESSED_ENABLED, pressDrawableBuilder.setSolidColor(pressColor).build()).
                addState(SelectState.ENABLED_SELECTED, selectDrawableBuilder.setSolidColor(selectColor).build())
                .addState(SelectState.EMPTY, emptyDrawableBuilder.setSolidColor(defaultColor).build()).build());
    }

    /**
     * 设置分隔线宽度
     *
     * @param size
     */
    public void setDividerSize(int size) {
        this.dividerSize = size;
        invalidate();
    }

    public void setBackGroundColor(int color) {
        this.backgroundColor = color;
        refreshItems();
    }

    /**
     * 设置分隔线颜色
     *
     * @param color
     */
    public void setDivideColor(int color) {
        this.dividerColor = color;
        RectDrawableBuilder rect = DrawableBuilder.rect();
        if (GROUP_EDGE == edgeMode) {
            rect.setCornersRadius(roundRadius);
        }
        setBackgroundDrawable(rect.setStrokeWidth(dividerSize).
                setStrokeColor(dividerColor).
                setSolidColor(backgroundColor).build());
    }

    /**
     * 设置选中指定位置
     *
     * @param position
     */
    public void setChecked(final int position) {
        setTextColorByPosition(selectPosition, false);
        setTextColorByPosition(position, true);
        selectPosition = position;
    }

    /**
     * 设置选中监听
     */
    public void setOnCheckedListener(OnCheckedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDivide(canvas);// 绘制分隔线
    }

    /**
     * 绘制分隔线
     *
     * @param canvas
     */
    private void drawDivide(Canvas canvas) {
        int length = getChildCount();
        if (0 != length) {
            paint.setColor(dividerColor);
            paint.setStrokeWidth(dividerSize);
            View childView;
            for (int i = 0; i < length - 1; i++) {
                childView = getChildAt(i);
                canvas.drawLine(childView.getRight(), 0, childView.getRight(), getHeight(), paint);
            }
        }
    }

}
