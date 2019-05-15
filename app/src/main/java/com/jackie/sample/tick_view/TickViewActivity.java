package com.jackie.sample.tick_view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.tick_view.view.OnCheckedChangeListener;
import com.jackie.sample.tick_view.view.TickAnimatorListener;
import com.jackie.sample.tick_view.view.TickView;

/**
 * Created by Jackie on 2019/5/14.
 */
public class TickViewActivity extends AppCompatActivity {
    TickView tickView;
    TickView tickViewAccent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tick_view);

        tickView = (TickView) findViewById(R.id.tick_view);
        tickViewAccent = (TickView) findViewById(R.id.tick_view_accent);

        tickView.getConfig().setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TickView tickView, boolean isCheck) {

            }
        }).setTickAnimatorListener(new TickAnimatorListener.TickAnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(TickView tickView) {
                super.onAnimationStart(tickView);
            }
        });

        findViewById(R.id.check_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tickView.setChecked(true);
                tickViewAccent.setChecked(true);
            }
        });

        findViewById(R.id.uncheck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tickView.setChecked(false);
                tickViewAccent.setChecked(false);
            }
        });
    }
}
