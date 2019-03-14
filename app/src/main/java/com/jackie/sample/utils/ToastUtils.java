package com.jackie.sample.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.jackie.sample.application.SampleApplication;
import com.jackie.sample.custom_view.CustomToast;

/**
 * Created by Jackie on 2019/3/14.
 */
public class ToastUtils {
    public static void showToast(String text) {
        if (text != null) {
            CustomToast.makeText(SampleApplication.getApplicationInstance(), text, Toast.LENGTH_SHORT)
                    .setGravity(Gravity.BOTTOM, 0, ConvertUtils.dp2px(SampleApplication.getApplicationInstance(), 90))
                    .show();
        }
    }

    public static void showToast(int resource) {
        CustomToast.makeText(SampleApplication.getApplicationInstance(), resource, Toast.LENGTH_SHORT)
                .setGravity(Gravity.BOTTOM, 0, ConvertUtils.dp2px(SampleApplication.getApplicationInstance(), 90))
                .show();
    }
}
