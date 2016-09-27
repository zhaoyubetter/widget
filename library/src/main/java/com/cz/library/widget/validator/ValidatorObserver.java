package com.cz.library.widget.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.cz.library.widget.EditLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by czz on 2016/9/23.
 * 匹配信息检测观察者对象
 */
public class ValidatorObserver {
    private final List<EditLayout> layouts;
    private final HashMap<EditLayout,ValidatorTextWatcher> itemTextWatchers;
    private String errorMessage;
    private ValidatorAction validatorAction;

    private ValidatorObserver(EditLayout[] layouts) {
        this.layouts = new ArrayList<>();
        this.itemTextWatchers=new HashMap<>();
        if(null!=layouts){
            this.layouts.addAll(Arrays.asList(layouts));
            for(int i=0;i<layouts.length;i++){
                EditLayout layout = layouts[i];
                EditText editor = layout.getEditor();
                ValidatorTextWatcher textWatcher = new ValidatorTextWatcher(layout);
                itemTextWatchers.put(layout,textWatcher);
                editor.addTextChangedListener(textWatcher);
            }
        }
    }
    public static ValidatorObserver create(EditLayout... layouts){
        return new ValidatorObserver(layouts);
    }

    public ValidatorObserver subscribe(ValidatorAction action){
        this.validatorAction=action;
        return this;
    }

    public void addEditLayout(EditLayout layout){
        if(null!=layout){
            EditText editor = layout.getEditor();
            ValidatorTextWatcher textWatcher = new ValidatorTextWatcher(layout);
            editor.addTextChangedListener(textWatcher);
            itemTextWatchers.put(layout, textWatcher);
            layouts.add(layout);
        }
    }

    public void removeEditLayout(EditLayout layout){
        if(null!=layout){
            layouts.remove(layout);
            ValidatorTextWatcher textWatcher = itemTextWatchers.remove(layout);
            if(null!=textWatcher){
                EditText editor = layout.getEditor();
                editor.removeTextChangedListener(textWatcher);
            }
        }
    }

    /**
     * 判断所有布局条件是否匹配
     * @return
     */
    public boolean isValid(){
        boolean result=true;
        for(int i=0;i<layouts.size();i++){
            EditLayout layout = layouts.get(i);
            if(!(result&=layout.isValid())){
                errorMessage=layout.getEditError();
                break;
            }
        }
        return result;
    }

    /**
     * 获得检测失败异常信息
     * @return
     */
    public String getErrorMessage(){
        return errorMessage;
    }

    public interface ValidatorAction{
        void onChanged(EditText editText,boolean changed);
    }

    class ValidatorTextWatcher implements TextWatcher {
        private boolean isValid;
        private final EditLayout layout;

        public ValidatorTextWatcher(EditLayout layout) {
            this.layout = layout;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(isValid^layout.isValid()){
                isValid=!isValid;
                if(null!=validatorAction){
                    validatorAction.onChanged(layout.getEditor(),isValid);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
