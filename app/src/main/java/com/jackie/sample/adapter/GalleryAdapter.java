package com.jackie.sample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jackie.sample.R;

import java.util.List;

/**
 * Created by Jackie on 2017/6/23.
 */

public class GalleryAdapter extends BaseAdapter {
    private int mSelectPosition;
    private List<String> mDataList;

    public GalleryAdapter(int selectPosition, List<String> dataList) {
        this.mSelectPosition = selectPosition;
        this.mDataList = dataList;
    }

    public void setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_range, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(mDataList.get(position));

        if (mSelectPosition == position) {
//			holder.textView.setActivated(true);
//			holder.lineView.setBackgroundColor(Color.parseColor("#0971ce"));
//			holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        } else {
//			holder.textView.setActivated(false);
//			holder.lineView.setBackgroundColor(Color.parseColor("#999999"));
//			holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView textView ;
        View lineView;

        public ViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.tv_number);
            lineView = convertView.findViewById(R.id.v_line);
        }
    }
}
