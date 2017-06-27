package com.jackie.sample.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.bean.SampleBean;

import java.util.LinkedList;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleViewHolder> {
    private Context mContext;
    private View mItemView;
    private LinkedList<SampleBean> mList;

    public SampleAdapter(Context context,LinkedList<SampleBean> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mItemView = LayoutInflater.from(mContext).inflate(R.layout.item_desk_adapter, parent, false);
        SampleViewHolder sampleViewHolder = new SampleViewHolder(mItemView);
        return sampleViewHolder;
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder, int position) {
        final SampleBean sampleBean = mList.get(position);
        holder.textView.setText(sampleBean.getTitle());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, sampleBean.getClassName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList == null){
            return 0 ;
        }else{
            return mList.size();
        }
    }

    public class SampleViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private RelativeLayout relativeLayout;

        public SampleViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) mItemView.findViewById(R.id.desk_adapter_title);
            relativeLayout = (RelativeLayout) mItemView.findViewById(R.id.desk_adapter_layout);
        }
    }
}