package com.jackie.sample.custom_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/5/25.
 */

public class FloatWindowBigView extends LinearLayout implements View.OnClickListener {
    //记录大悬浮窗的宽度
    private int mWindowWidth;
    //记录大悬浮窗的宽度
    private int mWindowHeight;

    public FloatWindowBigView(Context context) {
        this(context, null);
    }

    public FloatWindowBigView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatWindowBigView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
        View view = findViewById(R.id.float_window_big_layout);
        mWindowWidth = view.getLayoutParams().width;
        mWindowHeight = view.getLayoutParams().height;

        Button close = (Button) findViewById(R.id.close);
        Button back = (Button) findViewById(R.id.back);

        close.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                //点击关闭悬浮窗的时候，移除所有悬浮窗，并停止service

                break;
            case R.id.back:
                break;
        }
    }

    public int getWindowWidth() {
        return mWindowWidth;
    }

    public int getWindowHeight() {
        return mWindowHeight;
    }
}
