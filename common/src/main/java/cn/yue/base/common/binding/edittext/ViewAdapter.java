package cn.yue.base.common.binding.edittext;

import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import cn.yue.base.common.binding.action.BindingCommand;

/**
 * Created by goldze on 2017/6/16.
 */

public class ViewAdapter {
    /**
     * EditText重新获取焦点的事件绑定
     */
    @BindingAdapter(value = {"requestFocus"}, requireAll = false)
    public static void requestFocusCommand(EditText editText, final Boolean needRequestFocus) {
        if (needRequestFocus) {
            editText.setSelection(editText.getText().length());
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
        editText.setFocusableInTouchMode(needRequestFocus);
    }
    @BindingAdapter(value = {"clearFocus"})
    public static void clearFocusCommand(EditText editText, final Boolean clear) {

        /*if (needFocus) {
            editText.setSelection(editText.getText().length());
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }*/
        if(clear) {
            editText.clearFocus();
        }
    }
    /**
     * 添加下划线
     * 添加中划线
     */
    @BindingAdapter(value = {"bottomLine","middleLine"}, requireAll = false)
    public static void setBottomLine(TextView editText, final Boolean needBottomLine, final Boolean needMiddleLine) {
        Paint paint = editText.getPaint();
        paint.setAntiAlias(true);//抗锯齿

        if(needBottomLine != null && needBottomLine){
            paint.setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        }
        if(needMiddleLine != null && needMiddleLine){
            paint.setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中划线
        }
    }

    public static void setBottomLine(TextView textView){
        Paint paint = textView.getPaint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中划线
    }



    /**
     * EditText输入文字改变的监听
     */
    @BindingAdapter(value = {"textChanged"}, requireAll = false)
    public static void addTextChangedListener(EditText editText, final BindingCommand<String> textChanged) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (textChanged != null) {
                    textChanged.execute(text.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
