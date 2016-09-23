package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cz.library.R;


/**
 * Created by cz on 9/1/16.
 */
public class SearchView extends LinearLayout {
    private ImageView hintView;
    private EditText editor;
    private ImageView deleteView;
    private OnTextChangeListener listener;

    public SearchView(Context context) {
        this(context,null,0);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.search_layout, this);
        setGravity(Gravity.CENTER_VERTICAL);

        hintView= (ImageView) findViewById(R.id.iv_hint_icon);
        editor= (EditText) findViewById(R.id.et_editor);
        deleteView= (ImageView) findViewById(R.id.iv_delete_icon);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchView);
        setHintDrawable(a.getDrawable(R.styleable.SearchView_sv_hintDrawable));
        setSearchHint(a.getString(R.styleable.SearchView_sv_searchHint));
        setSearchText(a.getString(R.styleable.SearchView_sv_searchText));
        setSearchHintTextColor(a.getColor(R.styleable.SearchView_sv_searchHintTextColor, Color.DKGRAY));
        setSearchTextColor(a.getColor(R.styleable.SearchView_sv_searchTextColor, Color.GRAY));
        setSearchTextSize(a.getDimensionPixelSize(R.styleable.SearchView_sv_searchTextSize,0));
        setSearchDeleteDrawable(a.getDrawable(R.styleable.SearchView_sv_searchDeleteDrawable));
        setEditPadding((int) a.getDimension(R.styleable.SearchView_sv_editPadding,0));
        a.recycle();
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        deleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editor.getText())) {
                    editor.setText(null);
                    v.setVisibility(View.GONE);
                }
            }
        });
        editor.addTextChangedListener(new TextWatcher() {
            private CharSequence lastItem = null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                deleteView.setVisibility(TextUtils.isEmpty(editor.getText()) ? View.GONE : View.VISIBLE);
                if (null != listener) {
                    listener.onTextChanged(s, lastItem);
                }
                lastItem = s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setHintDrawableResource(@DrawableRes int res) {
        setHintDrawable(getResources().getDrawable(res));
    }

    public void setHintDrawable(Drawable drawable) {
        if(null!=drawable){
            hintView.setVisibility(View.VISIBLE);
            hintView.setImageDrawable(drawable);
        } else {
            hintView.setVisibility(View.GONE);
        }
    }

    public void setSearchHint(String text) {
        editor.setHint(text);
    }

    public void setSearchText(String text) {
        editor.setText(text);
    }

    public void setSearchHintTextColor(int color) {
        editor.setHintTextColor(color);
    }

    public void setSearchTextColor(int color) {
        editor.setTextColor(color);
    }

    public void setSearchTextSize(int textSize){
        editor.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setSearchInputType(int inputType){
        editor.setInputType(inputType);
    }

    public void setEditPadding(int padding) {
        editor.setPadding(padding,0,padding,0);
    }

    public EditText getEditor(){
        return editor;
    }

    public Editable getText(){
        return editor.getText();
    }

    public void setSearchDeleteDrawable(Drawable drawable) {
        if(null!=drawable){
            deleteView.setVisibility(View.VISIBLE);
            deleteView.setImageDrawable(drawable);
        } else {
            deleteView.setVisibility(View.GONE);
        }
    }

    public void setOnTextChangeListener(OnTextChangeListener listener){
        this.listener=listener;
    }

    public interface OnTextChangeListener{
        void onTextChanged(CharSequence newItem, CharSequence oldItem);
    }


}
