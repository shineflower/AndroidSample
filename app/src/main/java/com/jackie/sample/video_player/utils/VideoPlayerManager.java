package com.jackie.sample.video_player.utils;

import com.jackie.sample.video_player.view.VideoPlayer;

/**
 * Created by Jackie on 2017/6/16.
 * 视频播放器管理器
 */

public class VideoPlayerManager {
    private VideoPlayer mVideoPlayerContainer;

    private VideoPlayerManager() {

    }

    private static VideoPlayerManager mInstance;

    public static synchronized VideoPlayerManager getInstance() {
        if (mInstance == null) {
            synchronized (VideoPlayerManager.class) {
                if (mInstance == null) {
                    mInstance = new VideoPlayerManager();
                }
            }
        }

        return mInstance;
    }

    public void setVideoPlayer(VideoPlayer videoPlayerContainer) {
        this.mVideoPlayerContainer = videoPlayerContainer;
    }

    public void releaseVideoPlayer() {
        if (mVideoPlayerContainer != null) {
            mVideoPlayerContainer.release();
            mVideoPlayerContainer = null;
        }
    }

    public boolean onBackPressd() {
        if (mVideoPlayerContainer != null) {
            if (mVideoPlayerContainer.isFullScreen()) {
                return mVideoPlayerContainer.exitFullScreen();
            } else if (mVideoPlayerContainer.isTinyWindow()) {
                return mVideoPlayerContainer.exitTinyWindow();
            } else {
                mVideoPlayerContainer.release();
                return false;
            }
        }

        return false;
    }
}
