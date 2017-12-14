package com.jackie.sample.zhihu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.AdImageView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/12/12.
 * 仿知乎列表广告展示效果
 */

public class ZhiHuAdActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_ad);

        mRecyclerView = (RecyclerView) findViewById(R.id.rl_ad);

        List<String> mockData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mockData.add(i + "");
        }

        mRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));

        mRecyclerView.setAdapter(new CommonAdapter<String>(ZhiHuAdActivity.this,
                R.layout.item_zhihu_ad,
                mockData) {

            @Override
            protected void convert(ViewHolder holder, String o, int position) {
                if (position > 0 && position % 6 == 0) {
                    holder.setVisible(R.id.tv_title, false);
                    holder.setVisible(R.id.tv_desc, false);
                    holder.setVisible(R.id.iv_ad, true);
                } else {
                    holder.setVisible(R.id.tv_title, true);
                    holder.setVisible(R.id.tv_desc, true);
                    holder.setVisible(R.id.iv_ad, false);
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int fPos = mLinearLayoutManager.findFirstVisibleItemPosition();
                int lPos = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                for (int i = fPos; i <= lPos; i++) {
                    View view = mLinearLayoutManager.findViewByPosition(i);

                    AdImageView adImageView = (AdImageView) view.findViewById(R.id.iv_ad);
                    if (adImageView.getVisibility() == View.VISIBLE) {
                        adImageView.setDy(mLinearLayoutManager.getHeight() - view.getTop());
                    }
                }
            }
        });
    }
}
