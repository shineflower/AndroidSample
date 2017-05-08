package com.jackie.sample.file_stream_recorder;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jackie.sample.R;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FileModeActivity extends AppCompatActivity {

    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.btn_speak)
    Button mBtnSpeak;

    private ExecutorService mExecutorService;
    private MediaRecorder mMediaRecorder;
    private File mAudioFile;
    private long mStartRecordTime, mStopRecordTime;
    private Handler mHandler;

    //主线程和后台播放线程数据同步
    private volatile boolean mIsPlaying;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_mode);
        ButterKnife.inject(this);

        //录音JNI函数不具备线程安全性，所以要用单线程
        mExecutorService = Executors.newSingleThreadExecutor();

        mHandler = new Handler(Looper.getMainLooper());

        //按下说话，释放发送
        mBtnSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startRecorder();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        stopRecorder();
                        break;
                }

                //处理了onTouch事件，返回true;
                return true;
            }
        });
    }

    @OnClick(R.id.btn_play)
    public void onClick() {
        //检查状态，防止重复播放
        if (mAudioFile != null && mIsPlaying) {
            //设置当前播放状态
            mIsPlaying = true;

            //提交后台任务，开始播放
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    doPlay(mAudioFile);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //activity销毁时，停止后台任务，避免内存泄漏
        mExecutorService.shutdownNow();
        releaseRecorder();
        stopPlay();
    }

    /**
     * 开始录音
     */
    private void startRecorder() {
        //改变UI的状态
        mBtnSpeak.setText("正在说话...");

        //提交后台任务，执行开始录音逻辑
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //释放之前的录音MediaRecorder(反复按住说话)
                releaseRecorder();

                //执行开始录音逻辑，如果失败提示用户
                if (!doStart()) {
                    recordFail();
                }
            }
        });
    }

    /**
     * 停止录音
     */
    private void stopRecorder() {
        mBtnSpeak.setText("按住说话");

        //提交后台任务，执行停止录音逻辑
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //执行停止录音逻辑，如果失败提示用户
                if (!doStop()) {
                    recordFail();
                }

                //释放MediaRecorder
                releaseRecorder();
            }
        });
    }

    /**
     * 启动录音逻辑
     *
     * @return 启动录音是否成功
     */
    private boolean doStart() {
        try {
            //创建MediaRecorder
            mMediaRecorder = new MediaRecorder();

            //创建录音文件
            mAudioFile = new File(Environment.getExternalStorageDirectory() + "/DemoAudio/" +
                    System.currentTimeMillis() + ".m4a");
            //保证父目录存在
            mAudioFile.getParentFile().mkdirs();
            //创建文件
            mAudioFile.createNewFile();

            //配置MediaRecorder
            //从麦克风采集
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            //保存文件为 MP4 格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            //所有安卓系统都支持的采样频率
            mMediaRecorder.setAudioSamplingRate(44100);

            //通用的 AAC 编码格式
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            //音质比较好的频率
            mMediaRecorder.setAudioEncodingBitRate(96000);

            //设置录音文件位置
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());

            //开始录音
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            //记录开始录音的时间，用于统计时长
            mStartRecordTime = System.currentTimeMillis();

        } catch (Exception e) {
            e.printStackTrace();
            //捕获异常，避免闪退，返回false，提示用户
            return false;
        }

        //录音成功
        return true;
    }

    /**
     * 停止录音逻辑
     *
     * @return 停止录音是否成功
     */
    private boolean doStop() {
        try {
            //停止录音
            mMediaRecorder.stop();

            //记录停止时间，统计时长
            mStopRecordTime = System.currentTimeMillis();

            //只接受超过3秒的录音，在UI上显示出来
            final int duration = (int) ((mStopRecordTime - mStartRecordTime) / 1000);
            if (duration > 3) {
                //在主线程显示出来
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTvTime.setText(mTvTime.getText() + "\n录音成功" + duration + "秒");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            //捕获异常，避免闪退，返回false，提示用户
            return false;
        }

        //停止成功
        return true;
    }

    /**
     * 释放MediaRecorder
     */
    private void releaseRecorder() {
        //检查MediaRecorder不为null
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

    }

    /**
     * 录音错误处理
     */
    private void recordFail() {
        mAudioFile = null;
        //提示用户录音错误，要在主线程中执行
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FileModeActivity.this, "录音失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 实际播放逻辑
     * @param file 播放的文件
     */
    private void doPlay(File file) {
        //配置播放器MediaPlayer
        mMediaPlayer = new MediaPlayer();

        try {
            //设置声音文件
            mMediaPlayer.setDataSource(file.getAbsolutePath());

            //设置监听回调
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //播放结束，释放播放器
                    stopPlay();
                }
            });

            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    //提醒用户
                    playFail();

                    //错误已经处理，返回true
                    return true;
                }
            });

            //配置音量，是否循环
            mMediaPlayer.setVolume(1, 1);
            mMediaPlayer.setLooping(false);

            //准备，开始
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            //异常处理，防止闪退
            e.printStackTrace();

            //提示用户
            playFail();

            //释放播放器
            stopPlay();
        }
    }

    /**
     * 停止播放的逻辑
     */
    private void stopPlay() {
        //重置播放状态
        mIsPlaying = false;

        //释放播放器
        if (mMediaPlayer != null) {
            //重置监听器，防止内存泄漏
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnErrorListener(null);

            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 提醒用户播放失败
     */
    private void playFail() {
        //在主线程toast提示
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FileModeActivity.this, "播放失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
