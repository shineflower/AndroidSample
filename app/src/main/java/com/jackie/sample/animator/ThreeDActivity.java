package com.jackie.sample.animator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ThreeDLayout;

/**
 * Android 3D 动画
 * Created by Jackie on 2018/12/17.
 */
public class ThreeDActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_three_d);

        ThreeDLayout threeDLayout = (ThreeDLayout) findViewById(R.id.tdl_root);

        // 开启触摸模式
        threeDLayout.setTouchable(true);
        // 设置模式为X，Y轴旋转
        threeDLayout.setTouchMode(ThreeDLayout.MODE_BOTH_X_Y);
        // 开启动画
        threeDLayout.startHorizontalAnimate();
    }
}
