package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
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
	public static final int LEFT=0x01;
	public static final int RIGHT=0x02;
	private static final String TAG = "BothTextView";

	@IntDef({LEFT,RIGHT})
	private @interface ImageGravity{
	}
	private int imageGravity;
	private Drawable imageDrawable;
	private int imageDrawableWidth;
	private int imageDrawableHeight;
	private int drawablePadding;
	private TextPaint textPaint;
	private String subText;
	private int textColor;
	private int textSize;
	private int rightPadding;
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
		setSubImageDrawablePadding((int) a.getDimension(R.styleable.BothTextView_bv_subImageDrawablePadding, 0));
		setSubImageDrawable(a.getDrawable(R.styleable.BothTextView_bv_subImageDrawable));
		setImageDrawableWidth((int) a.getDimension(R.styleable.BothTextView_bv_subImageDrawableWidth, 0f));
		setImageDrawableHeight((int) a.getDimension(R.styleable.BothTextView_bv_subImageDrawableHeight, 0f));
		setImageGravityInner(a.getInt(R.styleable.BothTextView_bv_subImageGravity, LEFT));

		setSubText(a.getString(R.styleable.BothTextView_bv_subText));
		setSubTextSize(a.getDimensionPixelSize(R.styleable.BothTextView_bv_subTextSize, 0));
		setSubTextColor(a.getColor(R.styleable.BothTextView_bv_subTextColor, Color.BLACK));
		setSubTextRightPadding((int) a.getDimension(R.styleable.BothTextView_bv_subTextRightPadding, 0));
		setDefaultSet(a.getBoolean(R.styleable.BothTextView_bv_subDefaultSet, true));

		a.recycle();
	}


	public void setSubImageDrawablePadding(int padding) {
		this.drawablePadding=padding;
		requestLayout();
	}

	public void setSubImageDrawable(Drawable drawable) {
		this.imageDrawable=drawable;
		invalidate();
	}

	public void setImageDrawableWidth(int width) {
		this.imageDrawableWidth =width;
		invalidate();
	}

	public void setImageDrawableHeight(int height) {
		this.imageDrawableHeight =height;
		invalidate();
	}

	public void setImageGravity(@ImageGravity int gravity) {
		setImageGravityInner(gravity);
	}

	private void setImageGravityInner(int gravity) {
		this.imageGravity=gravity;
		invalidate();
	}

	/**
	 * 设置右边文字边距
	 *
	 * @param dimension
	 */
	public void setSubTextRightPadding(int dimension) {
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
		int width = getWidth();
		int height = getHeight();
		Drawable[] compoundDrawables = getCompoundDrawables();
		int textWidth=0;
		if(!TextUtils.isEmpty(subText)){
			setDefaultSetPaint();
			textWidth=Math.round(textPaint.measureText(subText, 0, subText.length()));
		}
		int compoundDrawablesWidth=null==compoundDrawables[2]?0:compoundDrawables[2].getIntrinsicWidth();

		int textRight=width-getPaddingRight() - rightPadding -compoundDrawablesWidth-textWidth;
		if(null!= imageDrawable){
			int intrinsicWidth = imageDrawable.getIntrinsicWidth();
			int intrinsicHeight = imageDrawable.getIntrinsicHeight();
			int drawableWidth= (0==imageDrawableWidth)?intrinsicWidth: imageDrawableWidth;
			int drawableHeight= (0==imageDrawableHeight)?intrinsicHeight: imageDrawableHeight;
			int startX=(height-drawableHeight)/2;
			if(LEFT==imageGravity){
				imageDrawable.setBounds(textRight-drawablePadding-drawableWidth,startX,textRight-drawablePadding,startX+drawableHeight);
			} else if(RIGHT==imageGravity){
				int rightDrawableStart=width-getPaddingRight() -compoundDrawablesWidth;
				textRight-=(drawableWidth+drawablePadding);
				imageDrawable.setBounds(rightDrawableStart-drawablePadding-drawableWidth,startX,rightDrawableStart-drawablePadding,startX+drawableHeight);
			}
			imageDrawable.draw(canvas);
		}

		if (!TextUtils.isEmpty(subText)) {
			canvas.drawText(subText,  textRight, (height-(textPaint.descent() + textPaint.ascent()))/2, textPaint);
		}

	}
}