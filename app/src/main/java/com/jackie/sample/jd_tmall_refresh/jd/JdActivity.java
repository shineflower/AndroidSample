package com.jackie.sample.jd_tmall_refresh.jd;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jackie.sample.R;
import com.jackie.sample.adapter.JdTmallRefreshAdapter;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class JdActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private JdRefreshLayout mJdRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Object> mList;
    private JdTmallRefreshAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jd);

        mList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            mList.add(new Object());
        }

        mJdRefreshLayout = (JdRefreshLayout) findViewById(R.id.jd_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new JdTmallRefreshAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mJdRefreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                doSomething();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

    private void doSomething() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mList.add(new Object());
                mAdapter.notifyDataSetChanged();
                mJdRefreshLayout.refreshComplete();
            }
        }.execute();
    }
}
