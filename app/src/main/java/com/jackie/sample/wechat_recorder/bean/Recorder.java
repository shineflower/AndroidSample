package com.jackie.sample.wechat_recorder.bean;

/**
 * Created by Jackie on 2016/1/5.
 * Recorder
 */
public class Recorder {
    private float mTime;
    private String mFilePath;

    public Recorder(float time, String filePath) {
        this.mTime = time;
        this.mFilePath = filePath;
    }

    public float getTime() {
        return mTime;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setTime(float time) {
        this.mTime = time;
    }

    public void setFilePath(String filePath) {
        this.mFilePath = filePath;
    }
}