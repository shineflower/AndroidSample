package com.jackie.sample.volume_control;

import android.app.Activity;
import android.os.Bundle;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/5/17.
 */

public class VolumeControlActivity extends Activity {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_control_bar);
    }
}
