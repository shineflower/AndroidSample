package com.jackie.sample.adapter;

import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.listener.OnPhotoGridItemClickListener;

import java.util.List;

/**
 * Created by Jackie on 17/03/10.
 * Fragment的网格适配器
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ViewHolder> {
    private List<Pair<Integer, Integer>> mList; // 名字和图片
    private OnPhotoGridItemClickListener mListener; // 点击事件

    public PhotoGridAdapter(List<Pair<Integer, Integer>> list, OnPhotoGridItemClickListener listener) {
        mList = list;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imageView.setImageResource(mList.get(position).second);
        holder.textView.setText(mList.get(position).first);

        // 把每个图片视图设置不同的Transition名称, 防止在一个视图内有多个相同的名称, 在变换的时候造成混乱
        // Fragment支持多个View进行变换, 使用适配器时, 需要加以区分
        ViewCompat.setTransitionName(holder.imageView, String.valueOf(position) + "_image");

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPhotoGridItemClick(holder, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.grid_image);
            textView = (TextView) itemView.findViewById(R.id.grid_text);
        }
    }
}
