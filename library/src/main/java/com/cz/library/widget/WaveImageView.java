package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.cz.library.R;
import com.cz.library.util.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by cz on 15/11/11.
 * 一个带标签的imageView的波浪显示控件
 */
public class WaveImageView extends ImageView {
    private int padding;
    private int borderColor;
    private float borderWidth;
    private float waveRadius;
    private int waveColor;
    private int textColor;
    private float textSize;
    private int maskColor;
    private String text;
    private Paint paint;
    private ShapeDrawable shapeDrawable;
    private ValueAnimator valueAnimator;
    private PorterDuffXfermode xfermode;
    private Path path;
    private RectF rectF;
    int w = 0;
    int waveAmplitude;
    int waveRange;
    int highLevel;


    public WaveImageView(Context context) {
        this(context, null, 0);
    }

    public WaveImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        rectF = new RectF();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaveImageView);
        setPadding((int) a.getDimension(R.styleable.WaveImageView_wv_padding, 0));
        setBorderColor(a.getColor(R.styleable.WaveImageView_wv_borderColor, Color.RED));
        setBorderWidth(a.getDimension(R.styleable.WaveImageView_wv_borderWidth, Utils.dip2px(0.8f)));
        setWaveRadius(a.getFloat(R.styleable.WaveImageView_wv_waveRadius, 0f));
        setWaveColor(a.getColor(R.styleable.WaveImageView_wv_waveColor, Color.WHITE));
        setTextColor(a.getColor(R.styleable.WaveImageView_wv_textColor, Color.WHITE));
        setTextSize(a.getDimensionPixelSize(R.styleable.WaveImageView_wv_textSize, Utils.sp2px(12)));
        setMaskColor(a.getColor(R.styleable.WaveImageView_wv_maskColor, Color.WHITE));
        setText(a.getResourceId(R.styleable.WaveImageView_wv_label, NO_ID));
        a.recycle();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = getWidth();
                int height = getHeight();
                if (0 != width && 0 != height) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    width = getMeasuredWidth();
                    waveRange = width;
                    waveAmplitude = 12;
                    highLevel = (int) (height * (0.4f) + waveAmplitude);
                    invalidate();
                }
            }
        });
    }

    /**
     * 设置水波纹的高度0-1f
     *
     * @param fraction
     */
    public void setWaveLevel(float fraction) {
        this.highLevel = (int) (getHeight() * (fraction) + waveAmplitude);
        setTextColor((0.4f < fraction) ? Color.WHITE : borderColor);//设置字体颜色
        invalidate();
    }

    public void setWaveLevelAnim(float fraction) {
        setWaveLevelAnim(fraction, null);
    }

    /**
     * 设置水波纹的高度0-1f
     *
     * @param fraction
     */
    public void setWaveLevelAnim(float fraction, final Runnable action) {
        final int lastLevel = highLevel;
        final int newLevel = (int) (getHeight() * (fraction) + waveAmplitude);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(lastLevel, newLevel);
        valueAnimator.setDuration(600);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                highLevel = Integer.valueOf(animation.getAnimatedValue().toString());
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (null != action) {
                    action.run();
                }
            }
        });
        valueAnimator.start();
        setTextColor((0.4f < fraction) ? Color.WHITE : borderColor);//设置字体颜色
    }

    public void setPadding(int padding) {
        this.padding = padding;
        setScaleType(ScaleType.FIT_CENTER);
        setPadding(padding, padding, padding, padding);
        invalidate();
    }

    public void setBorderColor(int outLineColor) {
        this.borderColor = outLineColor;
        invalidate();
    }

    public void setWaveRadius(float waveRadius) {
        this.waveRadius = waveRadius;
        invalidate();
    }

    public void setWaveColor(int waveColor) {
        this.waveColor = waveColor;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setText(@StringRes int res) {
        if (NO_ID != res) {
            setText(getResources().getString(res));
        }
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
        invalidate();
    }

    public void setBorderWidth(float outLineWidth) {
        this.borderWidth = outLineWidth;
        invalidate();
    }

    public void startAnim() {
        if (null == valueAnimator) {
            valueAnimator = ValueAnimator.ofFloat(1f);
            valueAnimator.setDuration(Short.MAX_VALUE);
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.setRepeatCount(-1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    path.reset();
                    int width = getWidth();
                    int height = getHeight();
                    int padding = WaveImageView.this.padding;
                    rectF.left = padding;
                    rectF.top = padding;
                    rectF.right = width - padding;
                    rectF.bottom = height - padding;
                    path.addArc(rectF, 0, 180f);
                    w += 3;
                    if (w >= (width - padding) * 2) w = 0;
                    for (int x = padding; x < width - padding; x++) {
                        float y = (float) (highLevel + waveAmplitude * Math.cos((float) (x + w) / (float) (width - 5) * Math.PI));
                        path.lineTo(x, y);
                    }
                    path.close();
                    invalidate();
                }
            });
        } else {
            valueAnimator.cancel();
        }
        valueAnimator.start();
    }

    public void stopAnim() {
        if (null != valueAnimator) {
            valueAnimator.cancel();
        }
    }

    public void toggleWave() {
        if (null == valueAnimator) {
            startAnim();
        } else if (valueAnimator.isRunning()) {
            setWaveLevelAnim(0f, new Runnable() {
                @Override
                public void run() {
                    valueAnimator.cancel();
                }
            });
        } else {
            valueAnimator.start();
            setWaveLevelAnim(0.4f);
        }

    }

    /**
     * 自动移动
     */
    public void autoMoveAnim() {

    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(0);
        setImageDrawable(getResources().getDrawable(resId));
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(null);
        if (null != drawable) {
            //记录drawable位图对象
            Bitmap bitmap = getBitmapFromDrawable(drawable);
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            if (null == shapeDrawable) {
                shapeDrawable = new ShapeDrawable(new OvalShape());
            }
            shapeDrawable.getPaint().setShader(bitmapShader);
        } else {
            shapeDrawable = null;
        }
        invalidate();
    }

    /**
     * drawable获取bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        Bitmap bitmap = null;
        if (null != drawable) {
            try {
                if (drawable instanceof BitmapDrawable) {
                    return ((BitmapDrawable) drawable).getBitmap();
                } else if (drawable instanceof ColorDrawable) {
                    bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);
                } else if (0 < drawable.getIntrinsicWidth()
                        && 0 < drawable.getIntrinsicHeight()) {
                    bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_4444);
                }
                if (null != bitmap) {
                    Canvas canvas = new Canvas(bitmap);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);
                }
            } catch (OutOfMemoryError e) {
            }
        }
        return bitmap;
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int padding = this.padding;
        if (null != shapeDrawable) {
            //设置显示区域
            shapeDrawable.setBounds(padding, padding, width - padding, height - padding);
            //绘制shapeDrawable
            shapeDrawable.draw(canvas);
        }

        //动画不执行时,不进行绘制
        if (null != valueAnimator && valueAnimator.isRunning()) {
            int sc = canvas.saveLayer(0, 0, width, height, null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                    | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
            canvas.rotate(180, width / 2, height / 2);
            //绘底部波浪
            paint.setColor(waveColor);
            paint.setStyle(Paint.Style.FILL);

            //遮罩图层
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2 - borderWidth / 2 - padding, paint);
            paint.setXfermode(xfermode);

            canvas.drawPath(path, paint);

            paint.setXfermode(null);
            canvas.restoreToCount(sc);
        }

        //绘显示圆环
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2 - borderWidth / 2 - padding, paint);
        //绘显示文字
        String text = this.text;
        if (!TextUtils.isEmpty(text)) {
            paint.setColor(textColor);
            paint.setTextSize(textSize);
            paint.setStyle(Paint.Style.FILL);
            float textWidth = paint.measureText(text);
            float centerX = (width - textWidth) / 2;
            float centerY = (height - (paint.descent() + paint.ascent())) / 2;
            canvas.drawText(text, centerX, centerY, paint);
        }
    }

}
