package com.jackie.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jackie.sample.R;

import java.util.List;

/**
 * Created by Jackie on 2016/12/5.
 */

public class JdTmallRefreshAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> mList;

    public JdTmallRefreshAdapter(List<Object> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jd_tmall_goods, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(), "good", Toast.LENGTH_SHORT).show();
            }
        });
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
