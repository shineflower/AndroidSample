package com.jackie.sample.lucky_wheel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.LuckyWheelSurfaceView;

public class LuckyWheelActivity extends AppCompatActivity {
    private LuckyWheelSurfaceView mLuckyWheel;
    private ImageButton mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_wheel);

        mLuckyWheel = (LuckyWheelSurfaceView) findViewById(R.id.lucky_wheel);
        mStartButton = (ImageButton) findViewById(R.id.start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLuckyWheel.isStarting()) {
                    //防止stop方法被执行多次
                    if (!mLuckyWheel.isShouldStop()) {
                        mLuckyWheel.stop();
                        mStartButton.setBackgroundResource(R.drawable.start);
                    }
                } else {
                    mLuckyWheel.start(0);
                    mStartButton.setBackgroundResource(R.drawable.stop);
                }
            }
        });
    }
}
