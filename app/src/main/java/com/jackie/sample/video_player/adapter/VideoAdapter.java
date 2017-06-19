package com.jackie.sample.video_player.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackie.sample.R;
import com.jackie.sample.video_player.bean.Video;
import com.jackie.sample.video_player.view.VideoPlayer;
import com.jackie.sample.video_player.view.VideoPlayerController;

import java.util.List;

/**
 * Created by Jackie on 2017/5/21.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context mContext;
    private List<Video> mVideoList;

    public VideoAdapter(Context context, List<Video> videoList) {
        mContext = context;
        mVideoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        ViewHolder holder = new ViewHolder(itemView);

        VideoPlayerController controller = new VideoPlayerController(mContext);
        holder.setController(controller);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = mVideoList.get(position);

        holder.mVideoPlayer.setController(holder.mController);
        holder.mVideoPlayer.setup(video.getVideoUrl(), null);

        holder.mController.setTitle(video.getTitle());
        holder.mController.setImage(video.getImageUrl());

        if (position == mVideoList.size() - 1) {
            holder.mListSpace.setVisibility(View.GONE);
        } else {
            holder.mListSpace.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private VideoPlayer mVideoPlayer;
        private VideoPlayerController mController;
        private View mListSpace;

        public ViewHolder(View itemView) {
            super(itemView);
            mVideoPlayer = (VideoPlayer) itemView.findViewById(R.id.video_player);
            mListSpace = itemView.findViewById(R.id.list_space);
        }

        public void setController(VideoPlayerController controller) {
            this.mController = controller;
        }
    }
}
