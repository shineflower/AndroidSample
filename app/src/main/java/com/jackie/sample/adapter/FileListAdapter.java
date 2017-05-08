package com.jackie.sample.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.bean.FileInfo;
import com.jackie.sample.service.DownloadService;

import java.util.List;

/**
 * Created by Jackie on 2016/4/8.
 * 文件列表适配器
 */
public class FileListAdapter extends BaseAdapter {
    private Context mContext;
    private List<FileInfo> mList;

    public FileListAdapter(Context context, List<FileInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_download, parent, false);
            holder.downloadFileName = (TextView) convertView.findViewById(R.id.download_file_name);
            holder.downloadProgressBar = (ProgressBar) convertView.findViewById(R.id.download_progress_bar);
            holder.downloadProgress = (TextView) convertView.findViewById(R.id.download_progress);
            holder.downloadStart = (Button) convertView.findViewById(R.id.download_start);
            holder.downloadStop = (Button) convertView.findViewById(R.id.download_stop);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final FileInfo fileInfo = mList.get(position);

        holder.downloadFileName.setText(fileInfo.getFileName());
        holder.downloadProgressBar.setMax(100);
        holder.downloadProgressBar.setProgress(fileInfo.getProgress());
        if (fileInfo.getProgress() > 0) {
            holder.downloadProgress.setText(String.format(mContext.getString(R.string.download_progress_text), (fileInfo.getProgress() * (fileInfo.getLength() >> 20) / 100) , fileInfo.getLength() >> 20));
        } else {
            holder.downloadProgress.setText("");
        }

        if ((fileInfo.getProgress() * (fileInfo.getLength() >> 20) / 100) == fileInfo.getLength() >> 20) {
            //下载完成，允许点击下载按钮
            holder.downloadStart.setEnabled(true);
        }

        holder.downloadStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DownloadService.class);
                intent.setAction(DownloadService.DOWNLOAD_ACTION_START);
                intent.putExtra(DownloadService.KEY_DOWNLOAD_FILE_INFO, fileInfo);
                mContext.startService(intent);

                //防止多次点击
                holder.downloadStart.setEnabled(false);
            }
        });

        holder.downloadStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DownloadService.class);
                intent.setAction(DownloadService.DOWNLOAD_ACTION_STOP);
                intent.putExtra(DownloadService.KEY_DOWNLOAD_FILE_INFO, fileInfo);
                mContext.startService(intent);

                holder.downloadStart.setEnabled(true);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView downloadFileName;
        ProgressBar downloadProgressBar;
        TextView downloadProgress;
        Button downloadStart;
        Button downloadStop;
    }

    public void updateProgress(int id, int progress, int length) {
        FileInfo fileInfo = mList.get(id);
        fileInfo.setProgress(progress);
        fileInfo.setLength(length);

        notifyDataSetChanged();
    }
}
