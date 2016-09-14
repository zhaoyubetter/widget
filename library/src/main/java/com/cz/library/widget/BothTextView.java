package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cz.library.R;


/**
 * 显示两端文字textView,并能设置上下左右分隔线
 *
 * 改进方面,
 * 1:可动态设定绘制文字方向,分左,上,右,下
 * 2:可选依附元素.边距,drawable对象,以及另一边文字
 * 3:增加多行绘制,以及行限定
 * @author czz
 * @date 2014/9/3
 * @update 2016/9/14
 */
public class BothTextView extends DivideTextView {
	public static final int LEFT=0x01;
	public static final int TOP=0x02;
	public static final int RIGHT=0x04;
	public static final int BOTTOM=0x08;

	public static final int BORDER=0x00;
	public static final int TEXT=0x01;
	public static final int DRAWABLE=0x02;

	private TextPaint textPaint;
	private String text;
	private Rect textBounds;
	private int textColor;
	private int textSize;
	private boolean defaultSet;
	private int attachmentMode;
	private int textGravity;
	private float textPadding;
	private int maxLines;
	private boolean ellipsize;
	private boolean remeasure;
	private float finalTextWidth,finalTextHeight;


	public BothTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BothTextView(Context context) {
		this(context, null, 0);
	}

	public BothTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.remeasure=true;
		textBounds=new Rect();
		textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BothTextView);
		setHandText(a.getString(R.styleable.BothTextView_bv_handText));
		setHandTextSize(a.getDimensionPixelSize(R.styleable.BothTextView_bv_handTextSize, 0));
		setHandTextColor(a.getColor(R.styleable.BothTextView_bv_handTextColor, Color.BLACK));
		setHandTextPadding(a.getDimension(R.styleable.BothTextView_bv_handTextPadding, 0));
		setHandLines(a.getInteger(R.styleable.BothTextView_bv_handLines, 1));
		setHandEndEllipsize(a.getBoolean(R.styleable.BothTextView_bv_handEndEllipsize, true));
		setHandTextGravity(a.getInt(R.styleable.BothTextView_bv_handTextGravity,LEFT));
		setAttachmentModeInner(a.getInt(R.styleable.BothTextView_bv_attachmentMode,BORDER));
		setDefaultSet(a.getBoolean(R.styleable.BothTextView_bv_handDefaultSet, true));
		a.recycle();
	}



	public void setHandText(String text) {
		this.text=text;
		requestLayout();
	}

	public void setHandTextSize(int textSize) {
		this.textSize=textSize;
		requestLayout();
	}

	public void setHandTextColor(int color) {
		this.textColor=color;
		invalidate();
	}

	public void setHandTextPadding(float padding) {
		this.textPadding=padding;
		invalidate();
	}

	public void setHandLines(int lines) {
		this.maxLines=lines;
		requestLayout();
	}

	public void setHandEndEllipsize(boolean ellipsize) {
		this.ellipsize=ellipsize;
		requestLayout();
	}

	public void setHandTextGravity(int gravity) {
		this.textGravity=gravity;
		invalidate();
	}

	public void setAttachmentModeInner(int mode) {
		this.attachmentMode=mode;
		requestLayout();
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		this.remeasure=true;
		super.setText(text, type);
	}

	public void setDefaultSet(boolean defaultSet) {
		TextPaint defaultPaint = getPaint();
		textPaint.reset();
		if (null != defaultPaint && defaultSet) {
			textPaint.setColor(defaultPaint.getColor());
			textPaint.setTextSize(defaultPaint.getTextSize());
		} else {
			textPaint.setColor(textColor);
			textPaint.setTextSize(textSize);
		}
	}

	@Override
	public void invalidate() {
		if(hasWindowFocus()) super.invalidate();
	}

	@Override
	public void postInvalidate() {
		if(hasWindowFocus()) super.postInvalidate();
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measuredWidth = getMeasuredWidth();
		int measuredHeight = getMeasuredHeight();
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();
		Drawable[] drawables = getCompoundDrawables();

		float finalTextWidth = measuredWidth-paddingLeft-paddingRight;
		float finalTextHeight=measuredHeight-paddingTop-paddingBottom;
		if(remeasure){
			this.remeasure=false;
			this.finalTextWidth=finalTextWidth;
			this.finalTextHeight=finalTextHeight;
		}


		float textWidth = textPaint.measureText(text, 0, text.length());
		textPaint.getTextBounds(text,0, text.length(),textBounds);
		float textHeight=textBounds.height();

		float width=0,height=0;
		if(MeasureSpec.AT_MOST==widthMode){
			if(LEFT==textGravity||RIGHT==textGravity){
				if(TEXT==attachmentMode){
					width=paddingLeft+finalTextWidth+textPadding+textWidth+paddingRight;
				} else if(DRAWABLE==attachmentMode&&null!=drawables[0]){
					width=paddingLeft+drawables[0].getIntrinsicWidth()+textPadding+paddingRight;
				} else {
					width+=paddingLeft+textWidth+paddingRight;
				}
			}
		}
		if(MeasureSpec.AT_MOST==heightMode){
			if(TOP==textGravity||BOTTOM==textGravity){
				if(TEXT==attachmentMode){
					height=paddingTop+finalTextHeight+textPadding+textHeight+paddingBottom;
				} else if(DRAWABLE==attachmentMode&&null!=drawables[1]){
					height=paddingTop+drawables[1].getIntrinsicHeight()+textPadding+paddingBottom;
				} else {
					height+=paddingTop+textHeight+paddingBottom;
				}
			}
		}
		setMeasuredDimension((int)Math.max(measuredWidth,width),(int)Math.max(measuredHeight,height));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(!TextUtils.isEmpty(text)){
			int width = getWidth();
			int height = getHeight();
			int paddingLeft = getPaddingLeft();
			int paddingTop = getPaddingTop();
			int paddingRight = getPaddingRight();
			int paddingBottom = getPaddingBottom();
			Drawable[] drawables = getCompoundDrawables();
			float textWidth = textPaint.measureText(this.text, 0, this.text.length());
			textPaint.getTextBounds(this.text,0, this.text.length(),textBounds);
			float textHeight=textBounds.height();
			switch (attachmentMode){
				case TEXT:
					switch (textGravity){
						case TOP:
							drawText(canvas,paddingLeft,paddingTop+finalTextHeight+textPadding);
							break;
						case RIGHT:
							drawText(canvas,width-paddingRight-textPadding-finalTextWidth-textWidth,paddingTop);
							break;
						case BOTTOM:
							drawText(canvas,paddingLeft,height-paddingBottom+finalTextHeight-textPadding-textHeight);
							break;
						case LEFT:
							default:
								drawText(canvas,paddingLeft+finalTextWidth+textPadding,paddingTop);
							break;
					}
					break;
				case DRAWABLE:
				case BORDER:
					default:
						switch (textGravity){
							case TOP:
								if(DRAWABLE==attachmentMode&&null!=drawables[1]){
									drawText(canvas,paddingLeft,paddingTop+drawables[1].getIntrinsicHeight()+textPadding);
								} else {
									drawText(canvas,paddingLeft,paddingTop+textPadding);
								}
								break;
							case RIGHT:
								if(DRAWABLE==attachmentMode&&null!=drawables[2]){
									drawText(canvas,width-paddingRight-textPadding-drawables[2].getIntrinsicWidth()-textWidth,paddingTop);
								} else {
									drawText(canvas,width-paddingRight-textPadding-textWidth,paddingTop);
								}
								break;
							case BOTTOM:
								if(DRAWABLE==attachmentMode&&null!=drawables[3]){
									drawText(canvas,paddingLeft,height-paddingBottom+drawables[3].getIntrinsicHeight()-textPadding-textHeight);
								} else {
									drawText(canvas,paddingLeft,height-paddingBottom-textPadding-textHeight);
								}
								break;
							case LEFT:
							default:
								if(DRAWABLE==attachmentMode&&null!=drawables[0]){
									drawText(canvas,paddingLeft+drawables[0].getIntrinsicWidth()+textPadding,paddingTop);
								} else {
									drawText(canvas,paddingLeft+textPadding,paddingTop);
								}
								break;
						}
					break;
			}
		}
	}



	private void drawText(Canvas canvas,float x,float y) {
		canvas.drawText(text,x,y,textPaint);
	}

}
