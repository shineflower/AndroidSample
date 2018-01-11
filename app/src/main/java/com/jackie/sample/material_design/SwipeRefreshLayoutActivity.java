package com.jackie.sample.material_design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.CustomSwipeRefreshLayout;
import com.jackie.sample.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2018/1/11.
 * 自定下拉刷新上拉加载控件
 */

public class SwipeRefreshLayoutActivity extends AppCompatActivity {
    private CustomSwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private ArrayAdapter mAdapter;

    private List<String> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_swipe_refresh_layout);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mSwipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mListView = (ListView) findViewById(R.id.list_view);

        mList = new ArrayList<>();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            mList.add("第" + (i + 1) + "条数据");
        }

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
    }

    private void refreshData() {
        ThreadUtils.newThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 5; i++) {
                            mList.add("第" + (i + 1) + "条数据");
                        }

                        mAdapter.notifyDataSetChanged();

                        mSwipeRefreshLayout.onRefreshComplete();
                    }
                });
            }
        });
    }

    private void initEvent() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        /**
         * 下拉刷新
         */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               refreshData();
            }
        });

        /**
         * 上拉加载更多
         */
        mSwipeRefreshLayout.setOnLoadListener(new CustomSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                refreshData();
            }
        });
    }
}
