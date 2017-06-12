package com.jackie.sample.multiple_list.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jackie.sample.multiple_list.holder.BaseViewHolder;
import com.jackie.sample.multiple_list.type.TypeFactory;
import com.jackie.sample.multiple_list.type.TypeFactoryForList;
import com.jackie.sample.multiple_list.model.Visitable;

import java.util.List;

/**
 * Created by Jackie on 2017/6/12.
 */

public class MultipleAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private TypeFactory mTypeFactory;
    private List<Visitable> mVisitableList;

    public MultipleAdapter(List<Visitable> visitableList) {
        this.mTypeFactory = new TypeFactoryForList();
        this.mVisitableList = visitableList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View itemView = View.inflate(context, viewType, null);
        return mTypeFactory.createViewHolder(viewType, itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setupView(mVisitableList.get(position), position, this);
    }

    @Override
    public int getItemCount() {
        return mVisitableList == null ? 0 : mVisitableList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mVisitableList.get(position).type(mTypeFactory);
    }
}
