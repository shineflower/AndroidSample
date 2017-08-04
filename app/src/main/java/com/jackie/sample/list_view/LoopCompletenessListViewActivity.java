package com.jackie.sample.list_view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.LoopCompletenessListView;
import com.jackie.sample.wechat_image_picker.adapter.CommonAdapter;
import com.jackie.sample.wechat_image_picker.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jackie on 2017/5/19.
 */

public class LoopCompletenessListViewActivity extends AppCompatActivity {
    private List<String> mList;
    private LoopCompletenessListView mListView;
    private CommonAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_completeness_list_view);

        initView();
        initData();
    }

    private void initView() {
        mListView = (LoopCompletenessListView) findViewById(R.id.list_view);
    }

    private void initData() {
        mList = new ArrayList<>();

        for (int i = 'A'; i <= 'Z'; i++) {
            mList.add(String.valueOf((char) +i));
        }

        mAdapter = new CommonAdapter<String>(this, R.layout.item_loop_completeness_list_view, mList) {
            @Override
            public void convert(ViewHolder helper, String title) {
                View convertView = helper.getConvertView();
                convertView.setBackgroundColor(Color.rgb(
                        new Random().nextInt(255), new Random().nextInt(255),
                        new Random().nextInt(255)));
                ViewGroup.LayoutParams params = convertView.getLayoutParams();
                params.height = mListView.getItemHeight();
                convertView.setLayoutParams(params);
                helper.setText(R.id.text_view, title);
            }
        };

        mListView.setAdapter(mAdapter);
    }
}
