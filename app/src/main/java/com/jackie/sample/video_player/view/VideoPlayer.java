package com.jackie.sample.video_player.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jackie.sample.video_player.listener.OnVideoPlayerListener;
import com.jackie.sample.video_player.utils.VideoPlayerManager;
import com.jackie.sample.video_player.utils.VideoPlayerUtils;
import com.jackie.sample.utils.LogUtils;

import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Jackie on 2017/6/16.
 * 播放器
 */

public class VideoPlayer extends FrameLayout implements OnVideoPlayerListener,
        TextureView.SurfaceTextureListener {

    private Context mContext;
    private FrameLayout mFrameLayout;
    private TextureView mTextureView;
    private VideoPlayerController mController;
    private SurfaceTexture mSurfaceTexture;
    private String mUrl;
    private Map<String, String> mHeaderMap;
    private IMediaPlayer mMediaPlayer;

    private int mBufferPercentage;

    private int mPlayerType = PLAYER_TYPE_IJK;
    private int mCurrentState = STATE_IDLE;
    private int mPlayerState = PLAYER_NORMAL;

    public static final int STATE_ERROR = -1;        //播放错误
    public static final int STATE_IDLE = 0;          //播放未开始
    public static final int STATE_PREPARING = 1;     //播放准备中
    public static final int STATE_PREPARED = 2;      //播放准备就绪
    public static final int STATE_PLAYING = 3;       //正在播放
    public static final int STATE_PAUSED = 4;        //播放暂停

    //正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
    public static final int STATE_BUFFERING_PLAYING = 5;
    //正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此刻暂停播放器，继续缓冲，缓冲区数据足够后恢复播放)
    public static final int STATE_BUFFERING_PAUSED = 6;
    public static final int STATE_COMPLETED = 7;

    public static final int PLAYER_NORMAL = 10;
    public static final int PLAYER_FULL_SCREEN = 11;
    public static final int PLAYER_TINY_WINDOW = 12;

    public static final int PLAYER_TYPE_IJK = 111;
    public static final int PLAYER_TYPE_NATIVE = 222;

    public VideoPlayer(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;

        initView();
    }

    private void initView() {
        mFrameLayout = new FrameLayout(mContext);
        mFrameLayout.setBackgroundColor(Color.BLACK);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mFrameLayout, params);
    }

    public void setup(String url, Map<String, String> headerMap) {
        this.mUrl = url;
        this.mHeaderMap = headerMap;
    }

    public void setController(VideoPlayerController controller) {
        mController = controller;
        mController.setOnVideoPlayerListener(this);
        mFrameLayout.removeView(mController);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mFrameLayout.addView(mController, params);
    }

    /**
     * 设置播放器类型
     * @param playerType IjkPlayer or MediaPlayer.
     */
    public void setPlayerType(int playerType) {
        mPlayerType = playerType;
    }

    @Override
    public void start() {
        VideoPlayerManager.getInstance().releaseVideoPlayer();
        VideoPlayerManager.getInstance().setVideoPlayer(this);

        if (mCurrentState == STATE_IDLE
                || mCurrentState == STATE_ERROR
                || mCurrentState == STATE_COMPLETED) {
            initMediaPlayer();
            initTextureView();
            addTextureView();
        }
    }

    private void initMediaPlayer() {
        if (mMediaPlayer == null) {
            switch (mPlayerType) {
                case PLAYER_TYPE_NATIVE:
                    mMediaPlayer = new AndroidMediaPlayer();
                    break;
                case PLAYER_TYPE_IJK:
                default:
                    mMediaPlayer = new IjkMediaPlayer();
                    break;
            }
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);

            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        }
    }

    private void initTextureView() {
        if (mTextureView == null) {
            mTextureView = new TextureView(mContext);
            mTextureView.setSurfaceTextureListener(this);
        }
    }

    private void addTextureView() {
        mFrameLayout.removeView(mTextureView);

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        mFrameLayout.addView(mTextureView, 0, params);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surfaceTexture;
            openMediaPlayer();
        } else {
            mTextureView.setSurfaceTexture(mSurfaceTexture);
        }
    }

    private void openMediaPlayer() {
        try {
            mMediaPlayer.setDataSource(mContext.getApplicationContext(), Uri.parse(mUrl), mHeaderMap);
            mMediaPlayer.setSurface(new Surface(mSurfaceTexture));
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtils.showLog("STATE_PREPARING");
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.showErrLog("打开播放器发生错误");
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return mSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            mp.start();
            mCurrentState = STATE_PREPARED;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtils.showLog("onPrepared ——> STATE_PREPARED");
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener
            = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            LogUtils.showLog("onVideoSizeChanged ——> width：" + width + "，height：" + height);
        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener
            = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            mCurrentState = STATE_COMPLETED;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtils.showLog("onCompletion ——> STATE_COMPLETED");
            VideoPlayerManager.getInstance().setVideoPlayer(null);
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener
            = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            mCurrentState = STATE_ERROR;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtils.showLog("onError ——> STATE_ERROR ———— what：" + what);
            return false;
        }
    };

    private IMediaPlayer.OnInfoListener mOnInfoListener
            = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                // 播放器开始渲染
                mCurrentState = STATE_PLAYING;
                mController.setControllerState(mPlayerState, mCurrentState);
                LogUtils.showLog("onInfo ——> MEDIA_INFO_VIDEO_RENDERING_START：STATE_PLAYING");
            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                // MediaPlayer暂时不播放，以缓冲更多的数据
                if (mCurrentState == STATE_PAUSED || mCurrentState == STATE_BUFFERING_PAUSED) {
                    mCurrentState = STATE_BUFFERING_PAUSED;
                    LogUtils.showLog("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PAUSED");
                } else {
                    mCurrentState = STATE_BUFFERING_PLAYING;
                    LogUtils.showLog("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PLAYING");
                }
                mController.setControllerState(mPlayerState, mCurrentState);
            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                // 填充缓冲区后，MediaPlayer恢复播放/暂停
                if (mCurrentState == STATE_BUFFERING_PLAYING) {
                    mCurrentState = STATE_PLAYING;
                    mController.setControllerState(mPlayerState, mCurrentState);
                    LogUtils.showLog("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PLAYING");
                }
                if (mCurrentState == STATE_BUFFERING_PAUSED) {
                    mCurrentState = STATE_PAUSED;
                    mController.setControllerState(mPlayerState, mCurrentState);
                    LogUtils.showLog("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PAUSED");
                }
            } else {
                LogUtils.showLog("onInfo ——> what：" + what);
            }

            return true;
        }
    };

    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener
            = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            mBufferPercentage = percent;
        }
    };

    @Override
    public void restart() {
        if (mCurrentState == STATE_PAUSED) {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtils.showLog("STATE_PLAYING");
        }

        if (mCurrentState == STATE_BUFFERING_PAUSED) {
            mMediaPlayer.start();
            mCurrentState = STATE_BUFFERING_PLAYING;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtils.showLog("STATE_BUFFERING_PLAYING");
        }
    }

    @Override
    public void pause() {
        if (mCurrentState == STATE_PLAYING) {
            mMediaPlayer.pause();
            mCurrentState = STATE_PAUSED;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtils.showLog("STATE_PAUSED");
        }

        if (mCurrentState == STATE_BUFFERING_PLAYING) {
            mMediaPlayer.pause();
            mCurrentState = STATE_BUFFERING_PAUSED;
            mController.setControllerState(mPlayerState, mCurrentState);
            LogUtils.showLog("STATE_BUFFERING_PAUSED");
        }
    }

    @Override
    public void seekTo(int pos) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(pos);
        }
    }

    @Override
    public boolean isIdle() {
        return mCurrentState == STATE_IDLE;
    }

    @Override
    public boolean isPreparing() {
        return mCurrentState == STATE_PREPARING;
    }

    @Override
    public boolean isPrepared() {
        return mCurrentState == STATE_PREPARED;
    }

    @Override
    public boolean isBufferingPlaying() {
        return mCurrentState == STATE_BUFFERING_PLAYING;
    }

    @Override
    public boolean isBufferingPaused() {
        return mCurrentState == STATE_BUFFERING_PAUSED;
    }

    @Override
    public boolean isPlaying() {
        return mCurrentState == STATE_PLAYING;
    }

    @Override
    public boolean isPaused() {
        return mCurrentState == STATE_PAUSED;
    }

    @Override
    public boolean isError() {
        return mCurrentState == STATE_ERROR;
    }

    @Override
    public boolean isCompleted() {
        return mCurrentState == STATE_COMPLETED;
    }

    @Override
    public boolean isFullScreen() {
        return mPlayerState == PLAYER_FULL_SCREEN;
    }

    @Override
    public boolean isTinyWindow() {
        return mPlayerState == PLAYER_TINY_WINDOW;
    }

    @Override
    public boolean isNormal() {
        return mPlayerState == PLAYER_NORMAL;
    }

    @Override
    public long getDuration() {
        return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public int getBufferPercentage() {
        return mBufferPercentage;
    }

    /**
     * 退出全屏，移除mTextureView和mController，并添加到非全屏的容器中。
     * 切换竖屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期.
     *
     * @return true退出全屏.
     */
    @Override
    public void enterFullScreen() {
        if (mPlayerState == PLAYER_FULL_SCREEN) return;

        // 隐藏ActionBar、状态栏，并横屏
        VideoPlayerUtils.hideActionBar(mContext);
        VideoPlayerUtils.scanForActivity(mContext)
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.removeView(mFrameLayout);
        ViewGroup contentView = (ViewGroup) VideoPlayerUtils.scanForActivity(mContext)
                .findViewById(android.R.id.content);

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        contentView.addView(mFrameLayout, params);

        mPlayerState = PLAYER_FULL_SCREEN;
        mController.setControllerState(mPlayerState, mCurrentState);
    }

    /**
     * 退出全屏，移除mTextureView和mController，并添加到非全屏的容器中。
     * 切换竖屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期.
     *
     * @return true退出全屏.
     */
    @Override
    public boolean exitFullScreen() {
        if (mPlayerState == PLAYER_FULL_SCREEN) {
            VideoPlayerUtils.showActionBar(mContext);
            VideoPlayerUtils.scanForActivity(mContext)
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            ViewGroup contentView = (ViewGroup) VideoPlayerUtils.scanForActivity(mContext)
                    .findViewById(android.R.id.content);
            contentView.removeView(mFrameLayout);

            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            this.addView(mFrameLayout, params);

            mPlayerState = PLAYER_NORMAL;
            mController.setControllerState(mPlayerState, mCurrentState);
            return true;
        }

        return false;
    }

    /**
     * 进入小窗口播放，小窗口播放的实现原理与全屏播放类似。
     */
    @Override
    public void enterTinyWindow() {
        if (mPlayerState == PLAYER_TINY_WINDOW) return;
        this.removeView(mFrameLayout);

        ViewGroup contentView = (ViewGroup) VideoPlayerUtils.scanForActivity(mContext)
                .findViewById(android.R.id.content);
        // 小窗口的宽度为屏幕宽度的60%，长宽比默认为16:9，右边距、下边距为8dp。
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                (int) (VideoPlayerUtils.getScreenWidth(mContext) * 0.6f),
                (int) (VideoPlayerUtils.getScreenWidth(mContext) * 0.6f * 9f / 16f));
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.rightMargin = VideoPlayerUtils.dp2px(mContext, 8f);
        params.bottomMargin = VideoPlayerUtils.dp2px(mContext, 8f);

        contentView.addView(mFrameLayout, params);

        mPlayerState = PLAYER_TINY_WINDOW;
        mController.setControllerState(mPlayerState, mCurrentState);
    }

    /**
     * 退出小窗口播放
     */
    @Override
    public boolean exitTinyWindow() {
        if (mPlayerState == PLAYER_TINY_WINDOW) {
            ViewGroup contentView = (ViewGroup) VideoPlayerUtils.scanForActivity(mContext)
                    .findViewById(android.R.id.content);
            contentView.removeView(mFrameLayout);

            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            this.addView(mFrameLayout, params);

            mPlayerState = PLAYER_NORMAL;
            mController.setControllerState(mPlayerState, mCurrentState);
            return true;
        }

        return false;
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        mFrameLayout.removeView(mTextureView);

        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if (mController != null) {
            mController.reset();
        }

        mCurrentState = STATE_IDLE;
        mPlayerState = PLAYER_NORMAL;
    }
}
