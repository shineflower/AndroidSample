package com.jackie.sample.slide_delete;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.jackie.sample.R;
import com.jackie.sample.adapter.SlideDeleteAdapter;
import com.jackie.sample.bean.SlideDeleteBean;
import com.jackie.sample.listener.OnStartDragListener;
import com.jackie.sample.listener.SimpleItemTouchCallback;

import java.util.ArrayList;
import java.util.List;

public class SlideDeleteActivity extends AppCompatActivity implements OnStartDragListener {
    private RecyclerView mRecyclerView;
    private SlideDeleteAdapter mAdapter;
    private ItemTouchHelper mHelper;
    private List<SlideDeleteBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_delete);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SlideDeleteAdapter(mList = initData(), this);
        mRecyclerView.setAdapter(mAdapter);
        mHelper = new ItemTouchHelper(new SimpleItemTouchCallback(mAdapter, mList));
        mHelper.attachToRecyclerView(mRecyclerView);
    }

    public List<SlideDeleteBean> initData(){
        List<SlideDeleteBean> list = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.array_title);
        for(int i = 0;i < titles.length; i++){
            SlideDeleteBean slideDeleteBean = new SlideDeleteBean();
            slideDeleteBean.setNumber(i + 1);
            slideDeleteBean.setTitle(titles[i]);
            list.add(slideDeleteBean);
        }

        return list;
    }

    @Override
    public void startDrag(RecyclerView.ViewHolder holder) {
        mHelper.startDrag(holder);
    }
}

