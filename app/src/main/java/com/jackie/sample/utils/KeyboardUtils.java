package com.jackie.sample.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * Created by Jackie on 2017/6/12.
 * 打开或关闭键盘
 */

public class KeyboardUtils {

    /**
     * 打开系统软键盘
     * 魅族可能有问题
     * @param context
     * @param editText
     */
    public static void openKeyboard(Context context, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭系统软键盘
     * @param context
     * @param editText
     */
    public static void closeKeyboard(Context context, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 隐藏系统软键盘
     * @param editTexts
     */
    public static void hideSystemKeyboard(Activity activity, EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            // 设置不调用系统键盘
            if (android.os.Build.VERSION.SDK_INT <= 10) {
                editTexts[i].setInputType(InputType.TYPE_NULL);
            } else {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                Class<EditText> clazz = EditText.class;
                Method method;

                try {
                    method = clazz.getMethod("setShowSoftInputOnFocus", boolean.class);
                    method.setAccessible(true);
                    method.invoke(editTexts[i], false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    method = clazz.getMethod("setSoftInputShownOnFocus", boolean.class);
                    method.setAccessible(true);
                    method.invoke(editTexts[i], false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
