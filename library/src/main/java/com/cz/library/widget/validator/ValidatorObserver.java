package com.cz.library.widget.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.cz.library.widget.EditLayout;


/**
 * Created by czz on 2016/9/23.
 * 匹配信息检测观察者对象
 */
public class ValidatorObserver {
    private final EditLayout[] layouts;
    private String errorMessage;
    private ValidatorAction validatorAction;

    private ValidatorObserver(EditLayout[] layouts) {
        this.layouts = layouts;
        for(int i=0;i<layouts.length;i++){
            EditLayout layout = layouts[i];
            EditText editor = layout.getEditor();
            editor.addTextChangedListener(new ValidatorTextWatcher(layout));
        }
    }
    public static ValidatorObserver create(EditLayout... layouts){
        return new ValidatorObserver(layouts);
    }

    public ValidatorObserver subscribe(ValidatorAction action){
        this.validatorAction=action;
        return this;
    }

    /**
     * 判断所有布局条件是否匹配
     * @return
     */
    public boolean isValid(){
        boolean result=true;
        for(int i=0;i<layouts.length;i++){
            EditLayout layout = layouts[i];
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
