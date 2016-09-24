package com.cz.library.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cz.library.R;
import com.cz.library.widget.validator.Validator;
import com.cz.library.widget.validator.impl.LengthValidator;
import com.cz.library.widget.validator.impl.NotEmptyValidator;
import com.cz.library.widget.validator.impl.pattern.CharacterValidator;
import com.cz.library.widget.validator.impl.pattern.EmailValidator;
import com.cz.library.widget.validator.impl.pattern.NumberValidator;
import com.cz.library.widget.validator.impl.pattern.PatternValidator;
import com.cz.library.widget.validator.impl.pattern.PatternValueValidator;
import com.cz.library.widget.validator.impl.pattern.PhoneValidator;
import com.cz.library.widget.validator.impl.RangeValidator;
import com.cz.library.widget.validator.impl.pattern.WebUrlValidator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by cz on 9/1/16.
 */
public class EditLayout extends LinearLayout {
    private static final String TAG = "EditLayout";

    public static final int VALIDATOR_NOT_EMPTY=0x01<<0;
    public static final int VALIDATOR_LENGTH=0x01<<1;
    public static final int VALIDATOR_RANGE=0x01<<2;

    public static final int PATTERN_CHARACTER=0x01;
    public static final int PATTERN_EMAIL=0x02;
    public static final int PATTERN_NUMBER=0x03;
    public static final int PATTERN_PHONE=0x04;
    public static final int PATTERN_WEB_URL=0x05;

    static final int STYLE_NUM_ENTRIES = 6;
    private static final HashMap<String,Integer> validatorFlags;

    static {
        validatorFlags=new HashMap<>();
        validatorFlags.put("not_empty",VALIDATOR_NOT_EMPTY);
        validatorFlags.put("length",VALIDATOR_LENGTH);
        validatorFlags.put("range",VALIDATOR_RANGE);
    }

    private final List<Validator> validatorItems;
    private final SparseArray<String> validatorItemValues;
    private PatternValidator patternValidator;
    private ImageView hintView;
    private EditText editor;
    private ImageView deleteView;
    private int errorTextColor;
    private OnTextChangeListener listener;
    private String editError;
    private int textColor;

    public EditLayout(Context context) {
        this(context,null,0);
    }

    public EditLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.edit_layout, this);
        setGravity(Gravity.CENTER_VERTICAL);

        this.validatorItems=new ArrayList<>();
        this.validatorItemValues=new SparseArray<>();
        hintView= (ImageView) findViewById(R.id.iv_hint_icon);
        editor= (EditText) findViewById(R.id.et_editor);
        deleteView= (ImageView) findViewById(R.id.iv_delete_icon);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditLayout);
        setHintDrawable(a.getDrawable(R.styleable.EditLayout_sv_hintDrawable));
        setEditHint(a.getString(R.styleable.EditLayout_sv_editHint));
        setEditText(a.getString(R.styleable.EditLayout_sv_editText));
        setEditHintTextColor(a.getColor(R.styleable.EditLayout_sv_editHintTextColor, Color.DKGRAY));
        setEditTextColor(a.getColor(R.styleable.EditLayout_sv_editTextColor, Color.GRAY));
        setEditTextSize(a.getDimensionPixelSize(R.styleable.EditLayout_sv_editTextSize,0));
        setSearchDeleteDrawable(a.getDrawable(R.styleable.EditLayout_sv_editDeleteDrawable));
        setEditPadding((int) a.getDimension(R.styleable.EditLayout_sv_editPadding,0));
        int attrType = getAttrType(a, R.styleable.EditLayout_sv_patternValidator);
        setEditErrorTextColor(a.getColor(R.styleable.EditLayout_sv_editErrorTextColor,Color.RED));
        setEditError(a.getString(R.styleable.EditLayout_sv_editError));
        setEditValidatorValue(a.getString(R.styleable.EditLayout_sv_validatorValue));
        setEditValidator(a.getInt(R.styleable.EditLayout_sv_validator,VALIDATOR_NOT_EMPTY));
        if(TypedValue.TYPE_INT_HEX==attrType){
            setPatternValidator(a.getInt(R.styleable.EditLayout_sv_patternValidator,PATTERN_CHARACTER));
        } else if(TypedValue.TYPE_STRING==attrType){
            setPatternValidator(a.getString(R.styleable.EditLayout_sv_patternValidator));
        }
        a.recycle();
    }

    public void setPatternValidator(int type){
        Log.e(TAG,"setPatternValidator:"+type);
        PatternValidator validator;
        switch (type){
            case PATTERN_EMAIL:
                validator=new EmailValidator();
                break;
            case PATTERN_NUMBER:
                validator=new NumberValidator();
                break;
            case PATTERN_PHONE:
                validator=new PhoneValidator();
                break;
            case PATTERN_WEB_URL:
                validator=new WebUrlValidator();
                break;
            case PATTERN_CHARACTER:
                default:
                    validator=new CharacterValidator();
                break;
        }
        this.patternValidator=validator;
    }

    private void setPatternValidator(String patternValue) {
        Log.e(TAG,"setPatternValidator:"+patternValue);
        this.patternValidator=new PatternValueValidator(patternValue);
    }

    private int getAttrType(TypedArray typedArray,int index){
        int attrType=TypedValue.TYPE_NULL;
        Class<TypedArray> clazz = TypedArray.class;
        try {
            Field field = clazz.getDeclaredField("mData");
            field.setAccessible(true);
            Object obj = field.get(typedArray);
            if(null!=obj){
                int[] data= (int[]) obj;
                index *= STYLE_NUM_ENTRIES;
                final int type = data[index];
                if (type >= TypedValue.TYPE_FIRST_INT
                        && type <= TypedValue.TYPE_LAST_INT) {
                    attrType=TypedValue.TYPE_INT_HEX;
                } else if (type == TypedValue.TYPE_STRING) {
                    attrType=TypedValue.TYPE_STRING;
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return attrType;
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
                    listener.onTextChanged(s, lastItem,count);
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

    public void setEditHint(String text) {
        editor.setHint(text);
    }

    public void setEditText(String text) {
        editor.setText(text);
    }

    public void setEditHintTextColor(int color) {
        editor.setHintTextColor(color);
    }

    public void setEditTextColor(int color) {
        this.textColor=color;
        editor.setTextColor(color);
    }

    public void setEditErrorTextColor(int color) {
        this.errorTextColor=color;
    }

    public void setEditTextSize(int textSize){
        editor.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setSearchInputType(int inputType){
        editor.setInputType(inputType);
    }

    public void setEditPadding(int padding) {
        editor.setPadding(padding,0,padding,0);
    }

    public void setEditError(String text) {
        this.editError=text;
    }

    private void setEditValidator(int flag) {
        validatorItems.clear();
        if(flag==(VALIDATOR_NOT_EMPTY|flag)){
            validatorItems.add(new NotEmptyValidator());
        }
        if(flag==(VALIDATOR_LENGTH|flag)){
            String propertyValue = validatorItemValues.get(VALIDATOR_LENGTH);
            if(!TextUtils.isEmpty(propertyValue)){
                validatorItems.add(new LengthValidator(Integer.valueOf(propertyValue)));
            }
        }
        if(flag==(VALIDATOR_RANGE|flag)){
            String propertyValue = validatorItemValues.get(VALIDATOR_RANGE);
            if(!TextUtils.isEmpty(propertyValue)){
                Pattern pattern = Pattern.compile("(\\d+)-(\\d+)");
                Matcher matcher = pattern.matcher(propertyValue);
                if(matcher.find()){
                    String start = matcher.group(1);
                    String end = matcher.group(2);
                    validatorItems.add(new RangeValidator(Integer.valueOf(start),Integer.valueOf(end)));
                }
            }
        }
    }

    private void setEditValidatorValue(String value) {
        if(!TextUtils.isEmpty(value)){
            Pattern pattern = Pattern.compile("(\\w+):([\\w-]+)\\s*");
            Matcher matcher = pattern.matcher(value);
            while(matcher.find()){
                String property=matcher.group(1);
                String propertyValue = matcher.group(2);
                if(validatorFlags.containsKey(property)){
                    Integer flag = validatorFlags.get(property);
                    if(!TextUtils.isEmpty(propertyValue)){
                        validatorItemValues.put(flag,propertyValue);
                    }
                }
            }
        }

    }

    public String getEditError(){
        return editError;
    }

    public boolean isValid(){
        boolean result=true;
        Editable text = getText();
        //常规条件匹配
        for(int i=0;i<validatorItems.size();i++){
            Validator validator = validatorItems.get(i);
            if(!(result&=validator.validator(text))){
                break;
            }
        }
        //正则匹配条件
        if(result&&null!=patternValidator){
            result&=patternValidator.validator(text);
        }
        return result;
    }

    public EditText getEditor(){
        return editor;
    }

    public Editable getText(){
        return editor.getText();
    }

    public void setSearchDeleteDrawable(Drawable drawable) {
        if(null!=drawable){
            deleteView.setImageDrawable(drawable);
        } else {
            deleteView.setVisibility(View.GONE);
        }
    }

    public void setOnTextChangeListener(OnTextChangeListener listener){
        this.listener=listener;
    }

    public interface OnTextChangeListener{
        void onTextChanged(CharSequence newItem, CharSequence oldItem,int count);
    }


}
