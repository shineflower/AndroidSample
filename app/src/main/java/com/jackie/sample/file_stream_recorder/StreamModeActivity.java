package com.jackie.sample.file_stream_recorder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jackie.sample.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class StreamModeActivity extends AppCompatActivity {

    @InjectView(R.id.btn_start)
    Button mBtnStart;
    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.btn_play)
    Button btnPlay;

    //录音的状态 volatile 保证多线程内存同步，避免出问题
    private volatile boolean mIsRecording;

    private ExecutorService mExecutorService;
    private Handler mHandler;

    private File mAudioFile;
    private FileOutputStream mFileOutputStream;
    private AudioRecord mAudioRecord;
    private long mStartRecordTime, mStopRecordTime;
    //buffer不能太大，避免OOM
    private byte[] mBuffer;

    private boolean mIsPlaying;

    private static final int BUFFER_SIZE = 2048;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_mode);
        ButterKnife.inject(this);

        //录音JNI函数不具备线程安全性，所以要用单线程
        mExecutorService = Executors.newSingleThreadExecutor();

        mHandler = new Handler(Looper.getMainLooper());

        mBuffer = new byte[BUFFER_SIZE];
    }

    @OnClick({R.id.btn_start, R.id.btn_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                //根据当前状态改变UI，执行开始/停止录音的逻辑
                if (mIsRecording) {
                    //改变UI状态
                    mBtnStart.setText("开始");

                    mIsRecording = false;
                } else {
                    mBtnStart.setText("停止");

                    //改变录音状态
                    mIsRecording = true;

                    //提交后台任务，执行开始录音逻辑
                    mExecutorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            //执行开始录音逻辑，如果失败提示用户
                            if (!startRecord()) {
                                recordFail();
                            }
                        }

                    });
                }
                break;
            case R.id.btn_play:
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
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //activity销毁时，停止后台任务，避免内存泄漏
        mExecutorService.shutdownNow();
    }

    /**
     * 启动录音逻辑
     *
     * @return 启动录音是否成功
     */
    private boolean startRecord() {
        try {
            //创建录音文件
            mAudioFile = new File(Environment.getExternalStorageDirectory() + "/DemoAudio/" +
                    System.currentTimeMillis() + ".pcm");
            //保证父目录存在
            mAudioFile.getParentFile().mkdirs();
            //创建文件
            mAudioFile.createNewFile();

            //创建文件输出流
            mFileOutputStream = new FileOutputStream(mAudioFile);

            //配置AudioRecord
            //从麦克风采集
            int audioSource = MediaRecorder.AudioSource.MIC;
            int sampleRate = 44100;  //所有安卓系统都支持的频率
            //单声道输入
            int channelConfig = AudioFormat.CHANNEL_IN_MONO;
            //PCM 16是所有安卓系统都支持
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            //计算AudioRecord内部buffer最小的大小
            int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);

            //buffer不能小于最低要求，也不能小于我们每次读取的大小
            mAudioRecord = new AudioRecord(audioSource, sampleRate, channelConfig,
                    audioFormat, Math.max(minBufferSize, BUFFER_SIZE));

            //开始录音
            mAudioRecord.startRecording();

            //记录开始录音时间
            mStartRecordTime = System.currentTimeMillis();

            //循环读取数据，写到输出流中
            while (mIsRecording) {
                //只要还在录音状态，就一直读取数据
                int len = mAudioRecord.read(mBuffer, 0, BUFFER_SIZE);
                if (len > 0) {
                    //读取成功，写到文件中
                    mFileOutputStream.write(mBuffer, 0, len);
                } else {
                    //读取失败，返回false，提示用户
                    return false;
                }
            }

            //退出循环，停止录音，释放资源
            return stopRecord();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            //释放AudioRecord
            if (mAudioRecord != null) {
                mAudioRecord.release();
            }
        }
    }

    /**
     * 停止录音
     *
     * @return 停止录音是否成功
     */
    private boolean stopRecord() {
        try {
            //停止录音，关闭文件输出流
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;

            mFileOutputStream.close();

            //记录结束时间，统计录音时长
            mStopRecordTime = System.currentTimeMillis();

            //大于3秒才算成功，在主线程中改变UI显示
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
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
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
                Toast.makeText(StreamModeActivity.this, "录音失败", Toast.LENGTH_SHORT).show();

                //重置录音状态以及UI状态
                mIsRecording = false;
                mBtnStart.setText("开始");
            }
        });
    }

    /**
     * 实际播放逻辑
     * @param file 播放的文件
     */
    private void doPlay(File file) {
        //配置播放器
        //音乐类型，扬声器播放
        int streamType = AudioManager.STREAM_MUSIC;
        //录音时采用的采样频率，播放的时候采用同样的采样频率
        int sampleRate = 44100;
        //MONO表示单声道，录音输入单声道，播放时用输出单声道
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        //录音时采用的16bit, 所以播放要使用同样的格式
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        //流模式
        int mode = AudioTrack.MODE_STREAM;

        //计算最小buffer大小
        int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);

        //构造AudioTrack
        AudioTrack audioTrack = new AudioTrack(streamType, sampleRate, channelConfig, audioFormat,
                //不能小于AudioTrack最低要求，也不能小于我们每次读的大小
                Math.max(minBufferSize, BUFFER_SIZE),
                mode);

        //从文件流中读数据
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);

            //循环读数据，写到播放器去播放
            int len;
            while ((len = fileInputStream.read(mBuffer)) > 0) {
                int result = audioTrack.write(mBuffer, 0, len);
                switch (result) {
                    case AudioTrack.ERROR_INVALID_OPERATION:
                    case AudioTrack.ERROR_BAD_VALUE:
                    case AudioTrack.ERROR_DEAD_OBJECT:
                        playFail();
                        break;
                    default:
                        break;
                }

            }
        } catch (Exception e) {
            playFail();
        } finally {
            mIsPlaying = false;

            //关闭文件输入流
            if (fileInputStream != null) {
                closeQuietly(fileInputStream);
            }

            //播放器释放
            resetQuietly(audioTrack);
        }
    }


    //静默关闭输入流
    private void closeQuietly(FileInputStream fileInputStream) {
        try {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetQuietly(AudioTrack audioTrack) {
        try {
            audioTrack.stop();
            audioTrack.release();
        } catch (Exception e) {
            e.printStackTrace();
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
                Toast.makeText(StreamModeActivity.this, "播放失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
