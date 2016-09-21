package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.cz.library.R;


/**
 * Created by cz on 9/12/16.
 */
public class DeleteEditText extends EditText {

    private int drawableWidth;
    private int drawableHeight;
    private Drawable deleteDrawable;
    private TextChangeListener listener;
    private boolean showDrawable;
    private boolean touchInRect;
    private OnDeleteTextListener deleteListener;

    public DeleteEditText(Context context) {
        this(context, null, 0);
    }

    public DeleteEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeleteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DeleteEditText);
        setDeleteDrawable(a.getDrawable(R.styleable.DeleteEditText_dt_deleteDrawable));
        setDeleteDrawableWidth((int) a.getDimension(R.styleable.DeleteEditText_dt_deleteDrawableWidth, 0));
        setDeleteDrawableHeight((int) a.getDimension(R.styleable.DeleteEditText_dt_deleteDrawableHeight, 0));
        a.recycle();


        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showDrawable = !TextUtils.isEmpty(s);
                if (null != listener) {
                    listener.onTextChanged(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void setDeleteDrawable(Drawable drawable) {
        this.deleteDrawable=drawable;
        invalidate();
    }

    public void setDeleteDrawableWidth(int width) {
        this.drawableWidth=width;
        int paddingRight = getPaddingRight();
        setPadding(getPaddingLeft(),getPaddingTop(),paddingRight+width,getPaddingRight());
        invalidate();
    }

    public void setDeleteDrawableHeight(int height) {
        this.drawableHeight=height;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null!=deleteDrawable&&showDrawable){
            int width = getWidth();
            int height = getHeight();
            int paddingRight = getPaddingRight();
            int startY=(height-drawableHeight)/2;
            deleteDrawable.setBounds(width-paddingRight,startY,width-paddingRight-drawableWidth,startY+drawableHeight);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(showDrawable){
                    touchInRect=touchInRect(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(showDrawable&&touchInRect&&touchInRect(x,y)&&null!=deleteListener){
                    deleteListener.onTextDeleted();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean touchInRect(int x, int y) {
        int width = getWidth();
        int height = getHeight();
        int paddingRight = getPaddingRight();
        int startY=(height-drawableHeight)/2;
        Rect rect=new Rect(width-paddingRight,startY,width-paddingRight-drawableWidth,startY+drawableHeight);
        return rect.contains(x,y);
    }

    public void setTextChangeListener(TextChangeListener listener){
        this.listener=listener;
    }

    public interface TextChangeListener{
        void onTextChanged(CharSequence s);
    }

    public void setOnDeleteTextListener(OnDeleteTextListener deleteListener){
        this.deleteListener=deleteListener;
    }

    public interface OnDeleteTextListener{
        void onTextDeleted();
    }
}
