package com.jackie.sample.utils;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Jackie on 2016/1/3.
 * AudioManager
 */
public class AudioManager {
    private MediaRecorder mMediaRecorder;
    private String mDirectory;  //存放录音的文件夹
    private String mCurrentFilePath;

    private static AudioManager mInstance;

    private OnAudioStateListener mOnAudioStateListener;
    private boolean isPrepared;

    /**
     * 回调准备完毕
     */
    public interface OnAudioStateListener {
        void onPrepareFinish();
    }

    public void setOnAudioStateListener(OnAudioStateListener onAudioStateListener) {
        this.mOnAudioStateListener = onAudioStateListener;
    }

    private AudioManager(String directory) {
        this.mDirectory = directory;
    }

    public static AudioManager getInstance(String directory) {
        if (mInstance == null) {
            synchronized (AudioManager.class)  {
                if (mInstance == null) {
                    mInstance = new AudioManager(directory);
                }
            }
        }

        return mInstance;
    }

    public void prepare() {
        try {
            isPrepared = false;

            File directory = new File(mDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = generateFileName();
            File file = new File(directory, fileName);
            mCurrentFilePath = file.getAbsolutePath();

            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置从音频源为麦克风
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR); //设置音频格式
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 设置音频的编码格式
            mMediaRecorder.setOutputFile(file.getAbsolutePath());  //设置输出文件路径
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isPrepared = true; //准备结束

            if (mOnAudioStateListener != null) {
                mOnAudioStateListener.onPrepareFinish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机生成文件的名称
     * @return  文件名
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    /**
     * 获取当前音量大小
     * @param maxLevel 音量的最大值，当前为7
     * @return 当前的音量大小，取值为1~7
     */
    public int getVolumeLevel(int maxLevel) {
        if(isPrepared) {
            //mMediaRecorder.getMaxAmplitude()返回值为：1~32767
            try {
                return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;  //返回1~7
            } catch (Exception e) {
            }
        }
        return 1;
    }

    public void release() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void cancel() {
        File file = new File(mCurrentFilePath);
        if (mCurrentFilePath != null) {
            file.delete();
            mCurrentFilePath = null;
        }
        release();
    }

    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }
}
