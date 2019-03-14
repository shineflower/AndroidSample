package com.jackie.sample.custom_view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2019/3/14.
 * 自定义Toast
 */

public class CustomToast {
    private Toast mToast;

    private CustomToast(Context context, CharSequence text, int duration) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);

        TextView textView = (TextView) view.findViewById(R.id.tv_content);
        textView.setText(text);

        if (mToast == null) {
            mToast = new Toast(context);
            mToast.setDuration(duration);
            mToast.setGravity(Gravity.TOP, 0, 220);
            mToast.setView(view);
        } else {
            mToast.cancel();
            mToast.setDuration(duration);
        }
    }

    public static CustomToast makeText(Context context, CharSequence text, int duration) {
        return new CustomToast(context, text, duration);
    }

    public static CustomToast makeText(Context context, int res, int duration) {
        return new CustomToast(context, context.getString(res), duration);
    }

    public CustomToast setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }

        return this;
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }
}
