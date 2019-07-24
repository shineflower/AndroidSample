package com.jackie.sample.slide_open;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2019/7/23 0023.
 * 滑动开门控件
 */

public class SlideOpenActivity extends AppCompatActivity {
    private SlideOpenLayout SlideOpenLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_open);

        SlideOpenLayout = (SlideOpenLayout) findViewById(R.id.slide_open);

        SlideOpenLayout.setOnSliderStatusChange(new OnSliderStatusChange() {
            @Override
            public void onSliderStatusChange(boolean isOpen) {
                if (isOpen) {
                    Toast.makeText(SlideOpenActivity.this, "Open", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
