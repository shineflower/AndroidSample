package com.jackie.sample.wechat_recorder.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.jackie.sample.R;
import com.jackie.sample.wechat_recorder.utils.AudioManager;
import com.jackie.sample.wechat_recorder.utils.DialogManager;

import java.io.File;

/**
 * Created by Jackie on 2016/1/2.
 * 录音按钮
 */
public class RecorderButton extends Button implements AudioManager.OnAudioStateListener {
    private static final int DISTANCE_Y_CANCEL = 50;

    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_CANCEL = 3;
    private int mCurrentState = STATE_NORMAL;

    private boolean mIsRecording = false;  //是否开始录音
    private boolean mIsReady = false;      //长按时间是否ready

    private DialogManager mDialogManager;
    private AudioManager mAudioManager;

    private float mTime;

    private static final int MSG_PREPARE_FINISH = 0;
    private static final int MSG_GET_VOLUME_LEVEL = 1;
    private static final int MSG_DIALOG_DISMISS = 2;
    private static final int MAX_VOLUME_LEVEL = 7;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PREPARE_FINISH:
                    mDialogManager.createDialog();
                    mIsRecording = true;

                    //开启线程获取音量
                    new Thread(mGetVolumeLevelRunnable).start();
                    break;
                case MSG_GET_VOLUME_LEVEL:
                    mDialogManager.updateVolumeLevel(mAudioManager.getVolumeLevel(MAX_VOLUME_LEVEL));
                    break;
                case MSG_DIALOG_DISMISS:
                    mDialogManager.dismissDialog();
                    break;
                default:
                    break;
            }
        }
    };

    public RecorderButton(Context context) {
        this(context, null);
    }

    public RecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDialogManager = new DialogManager(context);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String directory = Environment.getExternalStorageDirectory() + File.separator + "wechat_recorder";
            mAudioManager = AudioManager.getInstance(directory);
            mAudioManager.setOnAudioStateListener(this);
        }

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mIsReady = true;
                mAudioManager.prepare();
                return false;
            }
        });
    }

    /**
     * 录音完成后的回调
     */
    public interface OnRecordStateListener {
        void onRecordFinish(float time, String filePath);
    }

    private OnRecordStateListener mOnRecordStateListener;

    public void setOnRecordStateListener(OnRecordStateListener onRecordStateListener) {
        this.mOnRecordStateListener = onRecordStateListener;
    }

    private Runnable mGetVolumeLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (mIsRecording) {
                SystemClock.sleep(100);  //每隔0.1s获取一次音量大小
                mTime += 0.1f;
                mHandler.sendEmptyMessage(MSG_GET_VOLUME_LEVEL);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsRecording) {
                    if (wantToCancel(x, y)) {
                        changeState(STATE_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsReady) {
                    reset();
                    return super.onTouchEvent(event);
                }

                if (!mIsRecording || mTime < 0.6f) {
                    mDialogManager.showTooShortDialog();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);
                } else if (mCurrentState == STATE_RECORDING) {
                    //正常结束，保存录音
                    mDialogManager.dismissDialog();
                    mAudioManager.release();

                    if (mOnRecordStateListener != null) {
                        mOnRecordStateListener.onRecordFinish(mTime, mAudioManager.getCurrentFilePath());
                    }
                } else if (mCurrentState == STATE_CANCEL) {
                    //cancel
                    mDialogManager.dismissDialog();
                    mAudioManager.cancel();
                }
                reset();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (state) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.shape_recorder_normal_bg);
                    setText(R.string.state_normal);
                break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.shpe_recorder_recording_bg);
                    setText(R.string.state_recording);

                    if (mIsRecording) {
                        mDialogManager.showRecordingDialog();
                    }
                    break;
                case STATE_CANCEL:
                    setBackgroundResource(R.drawable.shpe_recorder_recording_bg);
                    setText(R.string.state_cancel);

                    mDialogManager.showCancelDialog();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 根据x,y的坐标判断是否需要取消
     * @param x    x轴坐标
     * @param y    y轴坐标
     * @return     是否需要取消
     */
    private boolean wantToCancel(int x, int y) {
        if (x < 0 || x > getWidth()) {
            return true;
        } else {
            if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
                return true;
            }
        }

        return false;
    }

    /**
     * 恢复状态及标志位
     */
    private void reset() {
        mIsRecording = false;
        mIsReady = false;
        mTime = 0;
        changeState(STATE_NORMAL);
    }

    @Override
    public void onPrepareFinish() {
        mHandler.sendEmptyMessage(MSG_PREPARE_FINISH);
    }
}
