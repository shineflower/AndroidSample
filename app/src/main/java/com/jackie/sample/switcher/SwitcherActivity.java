package com.jackie.sample.switcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.SwitchButton;
import com.jackie.sample.listener.OnSwitchStateChangeListener;

public class SwitcherActivity extends AppCompatActivity {
    private SwitchButton mSwitchButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switcher);

        mSwitchButton = (SwitchButton) findViewById(R.id.switcher);
        mSwitchButton.setSwitchState(true);
        mSwitchButton.setOnSwitchStateChangeListener(new OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean state) {
                if (state) {
                    Toast.makeText(SwitcherActivity.this, "开关打开了", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SwitcherActivity.this, "开关关闭了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}