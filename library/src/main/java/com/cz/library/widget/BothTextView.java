package com.cz.library.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cz.library.R;

/**
 * 显示两端文字textView,并能设置上下左右分隔线
 *
 * @author czz
 * @date 2014/9/3
 * @update 2016/9/14
 */
/**
 * 显示两端文字textView,并能设置上下左右分隔线
 *
 * @author momo
 * @Date 2014/9/3
 */
public class BothTextView extends DivideTextView {
	private TextPaint textPaint;
	private String subText;
	private int textColor;
	private int textSize;
	private float rightPadding;
	private boolean defaultSet;

	public BothTextView(Context context) {
		this(context, null, 0);
	}

	public BothTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}


	public BothTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BothTextView);
		setSubText(a.getString(R.styleable.BothTextView_bv_subText));
		setSubTextSize(a.getDimensionPixelSize(R.styleable.BothTextView_bv_subTextSize,0));
		setSubTextColor(a.getColor(R.styleable.BothTextView_bv_subTextColor,Color.BLACK));
		setSubTextRightPadding(a.getDimension(R.styleable.BothTextView_bv_subTextRightPadding, 0));
		setDefaultSet(a.getBoolean(R.styleable.BothTextView_bv_subDefaultSet, true));
		a.recycle();
	}

	/**
	 * 设置右边文字边距
	 *
	 * @param dimension
	 */
	public void setSubTextRightPadding(float dimension) {
		this.rightPadding = dimension;
		invalidate();
	}

	/**
	 * 设置右边文字颜色
	 *
	 * @param color
	 */
	public void setSubTextColor(int color) {
		this.textColor = color;
		invalidate();
	}

	/**
	 * 设置右边文字字体大小
	 *
	 * @param textSize
	 */
	public void setSubTextSize(int textSize) {
		this.textSize = textSize;
	}

	/**
	 * 设置文字
	 *
	 * @param resId
	 */
	public void setSubText(@StringRes int resId) {
		this.subText = getContext().getString(resId);
		invalidate();
	}

	public void setSubText(String text) {
		this.subText = text;
		invalidate();
	}

	/**
	 * 获取文字
	 *
	 * @return
	 */
	public String getSubText() {
		return this.subText;
	}

	/**
	 * 设置两边默认属性
	 */
	public void setDefaultSet(boolean defaultSet) {
		this.defaultSet=defaultSet;
		invalidate();
	}

	public void setDefaultSetPaint(){
		TextPaint textPaint = getPaint();
		if (null != textPaint && defaultSet) {
			this.textPaint.setColor(textPaint.getColor());
			this.textPaint.setTextSize(textPaint.getTextSize());
		} else {
			this.textPaint.setColor(textColor);
			this.textPaint.setTextSize(textSize);
		}
	}



	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!TextUtils.isEmpty(subText)) {
			setDefaultSetPaint();
			int width = getWidth();
			int height = getHeight();
			Drawable[] compoundDrawables = getCompoundDrawables();
			int drawableWidth=null==compoundDrawables[2]?0:compoundDrawables[2].getIntrinsicWidth()+getCompoundDrawablePadding();
			canvas.drawText(subText, width-getPaddingRight() - rightPadding -drawableWidth- textPaint.measureText(subText,0,subText.length()),
					(height-(textPaint.descent() + textPaint.ascent()))/2, textPaint);
		}
	}
}