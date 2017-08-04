package com.jackie.sample.ripple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jackie.sample.R;

/**
 *  可以将波纹的效果放到任何你想放的view上面
 *  android:background="?android:selectableItemBackground"   有边框的
 *  android:background="?android:selectableItemBackground-borderless"  无边框的效果
 */
public class RippleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);
    }
}
