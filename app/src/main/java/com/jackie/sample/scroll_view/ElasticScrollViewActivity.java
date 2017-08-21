package com.jackie.sample.scroll_view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ElasticScrollView;

public class ElasticScrollViewActivity extends AppCompatActivity {
    private ElasticScrollView mElasticScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elastic_scroll_view);

        mElasticScrollView = (ElasticScrollView) findViewById(R.id.sv_elastic);

        mElasticScrollView.setOnPullListener(new ElasticScrollView.OnPullListener() {
            @Override
            public void onDownPull() {
                Toast.makeText(ElasticScrollViewActivity.this, "下拉操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpPull() {
                Toast.makeText(ElasticScrollViewActivity.this, "上拉操作", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
