package com.jackie.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jackie.sample.utils.ViewHolder;

import java.util.List;

/**
 * Created by Jackie on 2015/12/29.
 * CommonUtils Adapter
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    private int mLayoutId;
    protected List<T> mList;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context context, int layoutId, List<T> list) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mList = list;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getViewHolder(mContext, convertView, parent, mLayoutId, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    };

    public abstract void convert(ViewHolder holder, T t);
}
