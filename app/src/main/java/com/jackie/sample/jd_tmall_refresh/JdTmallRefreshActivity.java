package com.jackie.sample.jd_tmall_refresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.jd_tmall_refresh.jd.JdActivity;
import com.jackie.sample.jd_tmall_refresh.tmall.TmallActivity;

public class JdTmallRefreshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jd_tmall_refresh);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.btn_jd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToActivity(JdActivity.class);
            }
        });

        findViewById(R.id.btn_tmall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToActivity(TmallActivity.class);
            }
        });
    }

    /**
     * 跳转
     *
     * @param cl
     */
    public void jumpToActivity(Class cl) {
        Intent intent = new Intent(this, cl);
        startActivity(intent);
    }
}
