package com.jackie.sample.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.bean.Recorder;

import java.util.List;

/**
 * Created by Jackie on 2016/1/5.
 * 适配器
 */
public class RecorderAdapter extends ArrayAdapter<Recorder> {
    private int mMinItemWidth;
    private int mMaxItemWidth;

    private LayoutInflater mInflater;

    public RecorderAdapter(Context context, List<Recorder> objects) {
        super(context, -1, objects);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mMinItemWidth = (int) (displayMetrics.widthPixels * 0.15f);
        mMaxItemWidth = (int) (displayMetrics.widthPixels * 0.7f);

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_recorder, parent, false);
            holder = new ViewHolder();
            holder.mTimeTextView = (TextView) convertView.findViewById(R.id.recorder_time);
            holder.mLengthView = convertView.findViewById(R.id.recorder_length);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTimeTextView.setText(Math.round(getItem(position).getTime()) + "");
        ViewGroup.LayoutParams params = holder.mLengthView.getLayoutParams();
        params.width = (int) (mMinItemWidth + mMaxItemWidth / 60f * getItem(position).getTime());
        return convertView;
    }

    private class ViewHolder {
        TextView mTimeTextView;
        View mLengthView;
    }
}
