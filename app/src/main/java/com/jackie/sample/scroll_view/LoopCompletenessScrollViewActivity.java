package com.jackie.sample.scroll_view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.bean.LoopCompletenessBean;
import com.jackie.sample.custom_view.LoopCompletenessScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jackie on 2017/5/19.
 */

public class LoopCompletenessScrollViewActivity extends AppCompatActivity {
    private LoopCompletenessScrollView mScrollView;
    private LoopCompletenessScrollView.ScrollViewAdapter mAdapter;
    private LayoutInflater mInflater;

    private List<LoopCompletenessBean> mLoopCompletenessList = new ArrayList<>(Arrays.asList(
            new LoopCompletenessBean(R.drawable.icon_about, Color.rgb(123, 34, 45)),
            new LoopCompletenessBean(R.drawable.icon_map, Color.rgb(12, 34, 145)),
            new LoopCompletenessBean(R.drawable.icon_message, Color.rgb(177, 234, 145)),
            new LoopCompletenessBean(R.drawable.icon_oil, Color.rgb(88, 134, 145))
    ));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_completeness_scroll_view);

        mInflater = LayoutInflater.from(this);

        mScrollView = (LoopCompletenessScrollView) findViewById(R.id.scroll_view);

        mAdapter = new LoopCompletenessScrollView.ScrollViewAdapter() {
            @Override
            public View getView(LoopCompletenessScrollView parent, int position) {
                View view = mInflater.inflate(R.layout.item_loop_completeness_scroll_view, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
                imageView.setImageResource(mLoopCompletenessList.get(position).getResId());
                view.setBackgroundColor(mLoopCompletenessList.get(position).getColor());
                return view;
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        mScrollView.setAdapter(mAdapter);
        mScrollView.setOnItemClickListener(new LoopCompletenessScrollView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
