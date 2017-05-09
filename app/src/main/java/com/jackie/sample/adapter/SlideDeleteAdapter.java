package com.jackie.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.bean.SlideDeleteBean;
import com.jackie.sample.listener.OnStartDragListener;

import java.util.List;

public class SlideDeleteAdapter extends RecyclerView.Adapter<SlideDeleteAdapter.ViewHolder> {
    private List<SlideDeleteBean> mList;
    public OnStartDragListener mListener;

    public SlideDeleteAdapter(List<SlideDeleteBean> list, OnStartDragListener listener) {
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SlideDeleteBean slideDeleteBean = mList.get(position);
        holder.title.setText(slideDeleteBean.getTitle());
        holder.number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mListener.startDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide_delete, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView title;
        public final ImageView number;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            number = (ImageView) v.findViewById(R.id.number);
        }
    }
}
