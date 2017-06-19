package com.jackie.sample.video_player.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jackie.sample.R;
import com.jackie.sample.video_player.listener.OnVideoPlayerListener;
import com.jackie.sample.video_player.utils.VideoPlayerUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jackie on 2017/6/16.
 */

public class VideoPlayerController extends FrameLayout implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    private Context mContext;
    private OnVideoPlayerListener mOnVideoPlayerListener;
    private ImageView mImage;
    private ImageView mCenterStart;
    private LinearLayout mTop;
    private ImageView mBack;
    private TextView mTitle;
    private LinearLayout mBottom;
    private ImageView mRestartPause;
    private TextView mPosition;
    private TextView mDuration;
    private SeekBar mSeek;
    private ImageView mFullScreen;
    private LinearLayout mLoading;
    private TextView mLoadText;
    private LinearLayout mError;
    private TextView mRetry;
    private LinearLayout mCompleted;
    private TextView mReplay;
    private TextView mShare;

    private Timer mUpdateProgressTimer;
    private TimerTask mUpdateProgressTimerTask;
    private boolean topBottomVisible;
    private CountDownTimer mDismissTopBottomCountDownTimer;

    public VideoPlayerController(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayerController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayerController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;

        initView();
        initEvent();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.video_palyer_controller, this, true);
        mCenterStart = (ImageView) findViewById(R.id.center_start);
        mImage = (ImageView) findViewById(R.id.image);

        mTop = (LinearLayout) findViewById(R.id.top);
        mBack = (ImageView) findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);

        mBottom = (LinearLayout) findViewById(R.id.bottom);
        mRestartPause = (ImageView) findViewById(R.id.restart_or_pause);
        mPosition = (TextView) findViewById(R.id.position);
        mDuration = (TextView) findViewById(R.id.duration);
        mSeek = (SeekBar) findViewById(R.id.seek);
        mFullScreen = (ImageView) findViewById(R.id.full_screen);

        mLoading = (LinearLayout) findViewById(R.id.loading);
        mLoadText = (TextView) findViewById(R.id.load_text);

        mError = (LinearLayout) findViewById(R.id.error);
        mRetry = (TextView) findViewById(R.id.retry);

        mCompleted = (LinearLayout) findViewById(R.id.completed);
        mReplay = (TextView) findViewById(R.id.replay);
        mShare = (TextView) findViewById(R.id.share);
    }

    private void initEvent() {
        mCenterStart.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mRestartPause.setOnClickListener(this);
        mFullScreen.setOnClickListener(this);
        mRetry.setOnClickListener(this);
        mReplay.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mSeek.setOnSeekBarChangeListener(this);
        this.setOnClickListener(this);
    }

    public void setImage(String imageUrl) {
        Glide.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.img_default)
                .crossFade()
                .into(mImage);
    }

    public void setImage(@DrawableRes int resId) {
        mImage.setImageResource(resId);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setOnVideoPlayerListener(OnVideoPlayerListener onVideoPlayerListener) {
        this.mOnVideoPlayerListener = onVideoPlayerListener;

        if (mOnVideoPlayerListener.isIdle()) {
            mBack.setVisibility(View.GONE);
            mTop.setVisibility(View.VISIBLE);
            mBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.center_start:
                mOnVideoPlayerListener.start();
                break;
            case R.id.back:
                if (mOnVideoPlayerListener.isFullScreen()) {
                    mOnVideoPlayerListener.exitFullScreen();
                } else {
                    mOnVideoPlayerListener.exitTinyWindow();
                }
                break;
            case R.id.restart_or_pause:
                if (mOnVideoPlayerListener.isPlaying() || mOnVideoPlayerListener.isBufferingPlaying()) {
                    mOnVideoPlayerListener.pause();
                } else if (mOnVideoPlayerListener.isPaused() || mOnVideoPlayerListener.isBufferingPaused()) {
                    mOnVideoPlayerListener.restart();
                }
                break;
            case R.id.full_screen:
                if (mOnVideoPlayerListener.isNormal()) {
                    mOnVideoPlayerListener.enterFullScreen();
                } else if (mOnVideoPlayerListener.isFullScreen()) {
                    mOnVideoPlayerListener.exitFullScreen();
                }
                break;
            case R.id.retry:
                mOnVideoPlayerListener.release();
                mOnVideoPlayerListener.start();
                break;
            case R.id.replay:
                mRetry.performClick();
                break;
            case R.id.share:
                Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
                break;
            default:
                if (mOnVideoPlayerListener.isPlaying()
                        || mOnVideoPlayerListener.isPaused()
                        || mOnVideoPlayerListener.isBufferingPlaying()
                        || mOnVideoPlayerListener.isBufferingPaused()) {
                    setTopBottomVisible(!topBottomVisible);
                }
        }
    }

    private void setTopBottomVisible(boolean visible) {
        mTop.setVisibility(visible ? View.VISIBLE : View.GONE);
        mBottom.setVisibility(visible ? View.VISIBLE : View.GONE);
        topBottomVisible = visible;

        if (visible) {
            if (!mOnVideoPlayerListener.isPaused() && !mOnVideoPlayerListener.isBufferingPaused()) {
                startDismissTopBottomTimer();
            }
        } else {
            cancelDismissTopBottomTimer();
        }
    }

    private void startDismissTopBottomTimer() {
        cancelDismissTopBottomTimer();

        if (mDismissTopBottomCountDownTimer == null) {
            mDismissTopBottomCountDownTimer = new CountDownTimer(8000, 8000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    setTopBottomVisible(false);
                }
            };
        }

        mDismissTopBottomCountDownTimer.start();
    }

    private void cancelDismissTopBottomTimer() {
        if (mDismissTopBottomCountDownTimer != null) {
            mDismissTopBottomCountDownTimer.cancel();
        }
    }

    public void setControllerState(int playerState, int playState) {
        switch (playerState) {
            case VideoPlayer.PLAYER_NORMAL:
                mBack.setVisibility(View.GONE);
                mFullScreen.setVisibility(View.VISIBLE);
                mFullScreen.setImageResource(R.drawable.ic_player_enlarge);
                break;
            case VideoPlayer.PLAYER_FULL_SCREEN:
                mBack.setVisibility(View.VISIBLE);
                mFullScreen.setVisibility(View.VISIBLE);
                mFullScreen.setImageResource(R.drawable.ic_player_shrink);
                break;
            case VideoPlayer.PLAYER_TINY_WINDOW:
                mFullScreen.setVisibility(View.GONE);
                break;
        }

        switch (playState) {
            case VideoPlayer.STATE_IDLE:
                break;
            case VideoPlayer.STATE_PREPARING:
                // 只显示准备中动画，其他不显示
                mImage.setVisibility(View.GONE);
                mLoading.setVisibility(View.VISIBLE);
                mLoadText.setText("正在准备...");
                mError.setVisibility(View.GONE);
                mCompleted.setVisibility(View.GONE);
                mTop.setVisibility(View.GONE);
                mCenterStart.setVisibility(View.GONE);
                break;
            case VideoPlayer.STATE_PREPARED:
                startUpdateProgressTimer();
                break;
            case VideoPlayer.STATE_PLAYING:
                mLoading.setVisibility(View.GONE);
                mRestartPause.setImageResource(R.drawable.ic_player_pause);
                startDismissTopBottomTimer();
                break;
            case VideoPlayer.STATE_PAUSED:
                mLoading.setVisibility(View.GONE);
                mRestartPause.setImageResource(R.drawable.ic_player_start);
                cancelDismissTopBottomTimer();
                break;
            case VideoPlayer.STATE_BUFFERING_PLAYING:
                mLoading.setVisibility(View.VISIBLE);
                mRestartPause.setImageResource(R.drawable.ic_player_pause);
                mLoadText.setText("正在缓冲...");
                startDismissTopBottomTimer();
                break;
            case VideoPlayer.STATE_BUFFERING_PAUSED:
                mLoading.setVisibility(View.VISIBLE);
                mRestartPause.setImageResource(R.drawable.ic_player_start);
                mLoadText.setText("正在缓冲...");
                cancelDismissTopBottomTimer();
                break;
            case VideoPlayer.STATE_COMPLETED:
                cancelUpdateProgressTimer();
                setTopBottomVisible(false);
                mImage.setVisibility(View.VISIBLE);
                mCompleted.setVisibility(View.VISIBLE);
                if (mOnVideoPlayerListener.isFullScreen()) {
                    mOnVideoPlayerListener.exitFullScreen();
                }

                if (mOnVideoPlayerListener.isTinyWindow()) {
                    mOnVideoPlayerListener.exitTinyWindow();
                }
                break;
            case VideoPlayer.STATE_ERROR:
                cancelUpdateProgressTimer();
                setTopBottomVisible(false);
                mTop.setVisibility(View.VISIBLE);
                mError.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void startUpdateProgressTimer() {
        cancelUpdateProgressTimer();

        if (mUpdateProgressTimer == null) {
            mUpdateProgressTimer = new Timer();
        }

        if (mUpdateProgressTimerTask == null) {
            mUpdateProgressTimerTask = new TimerTask() {
                @Override
                public void run() {
                    VideoPlayerController.this.post(new Runnable() {
                        @Override
                        public void run() {
                            updateProgress();
                        }
                    });
                }
            };
        }

        mUpdateProgressTimer.schedule(mUpdateProgressTimerTask, 0, 300);
    }

    private void cancelUpdateProgressTimer() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer.cancel();
            mUpdateProgressTimer = null;
        }

        if (mUpdateProgressTimerTask != null) {
            mUpdateProgressTimerTask.cancel();
            mUpdateProgressTimerTask = null;
        }
    }

    private void updateProgress() {
        long position = mOnVideoPlayerListener.getCurrentPosition();
        long duration = mOnVideoPlayerListener.getDuration();
        int bufferPercentage = mOnVideoPlayerListener.getBufferPercentage();
        mSeek.setSecondaryProgress(bufferPercentage);
        int progress = (int) (100f * position / duration);
        mSeek.setProgress(progress);
        mPosition.setText(VideoPlayerUtils.formatTime(position));
        mDuration.setText(VideoPlayerUtils.formatTime(duration));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        cancelDismissTopBottomTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mOnVideoPlayerListener.isBufferingPaused() || mOnVideoPlayerListener.isPaused()) {
            mOnVideoPlayerListener.restart();
        }

        int position = (int) (mOnVideoPlayerListener.getDuration() * seekBar.getProgress() / 100f);
        mOnVideoPlayerListener.seekTo(position);
        startDismissTopBottomTimer();
    }

    /**
     * 控制器恢复到初始状态
     */
    public void reset() {
        topBottomVisible = false;
        cancelUpdateProgressTimer();
        cancelDismissTopBottomTimer();
        mSeek.setProgress(0);
        mSeek.setSecondaryProgress(0);

        mCenterStart.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.VISIBLE);

        mBottom.setVisibility(View.GONE);
        mFullScreen.setImageResource(R.drawable.ic_player_enlarge);

        mTop.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.GONE);

        mLoading.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mCompleted.setVisibility(View.GONE);
    }
}
