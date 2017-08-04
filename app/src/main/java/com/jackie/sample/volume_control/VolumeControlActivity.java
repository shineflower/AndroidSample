package com.jackie.sample.volume_control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/5/17.
 */

public class VolumeControlActivity extends AppCompatActivity {
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_control_bar);
    }
}
