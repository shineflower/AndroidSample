package com.jackie.sample.multiple_list.holder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.jackie.sample.multiple_list.adapter.MultipleAdapter;

/**
 * Created by Jackie on 2017/6/12.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    private SparseArray<View> mSparseArray;
    private View mItemView;

    public BaseViewHolder(View itemView) {
        super(itemView);

        mSparseArray = new SparseArray<>();
        mItemView = itemView;
    }

    public View getView(int resId) {
        View view = mSparseArray.get(resId);

        if (view == null) {
            view = mItemView.findViewById(resId);
            mSparseArray.put(resId, view);
        }

        return view;
    }

    public abstract void setupView(T t, int position, MultipleAdapter adapter);
}
