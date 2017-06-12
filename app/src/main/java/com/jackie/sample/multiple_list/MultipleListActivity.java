package com.jackie.sample.multiple_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jackie.sample.R;
import com.jackie.sample.multiple_list.adapter.MultipleAdapter;
import com.jackie.sample.multiple_list.model.Normal;
import com.jackie.sample.multiple_list.model.One;
import com.jackie.sample.multiple_list.model.Three;
import com.jackie.sample.multiple_list.model.Two;
import com.jackie.sample.multiple_list.model.Visitable;
import com.jackie.sample.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/6/12.
 */

public class MultipleListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Visitable> mVisitableList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multiple_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mVisitableList = getData();
        mVisitableList.add(0, new One("Type One 0"));
        mVisitableList.add(1, new Two("Type Two 0"));
        mVisitableList.add(2, new Three("Type Three 0"));
        mVisitableList.add(3, new One("Type One 1"));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(new MultipleAdapter(mVisitableList));
    }

    private List<Visitable> getData(){
        List<Visitable> visitableList = new ArrayList<>();

        for (int index = 0; index < 50; index++ ){
            visitableList.add(new Normal("Type Normal " + index));
        }

        return visitableList;
    }
}
