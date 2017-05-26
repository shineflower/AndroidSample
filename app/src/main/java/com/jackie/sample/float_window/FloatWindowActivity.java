package com.jackie.sample.float_window;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jackie.sample.R;
import com.jackie.sample.service.FloatWindowService;

/**
 * Created by Jackie on 2017/5/25.
 */

public class FloatWindowActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mStartFloatWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_float_window);

        mStartFloatWindow = (Button) findViewById(R.id.start_float_window);
        mStartFloatWindow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_float_window:
                Intent intent = new Intent(this, FloatWindowService.class);
                startService(intent);
                finish();
                break;
        }
    }
}
