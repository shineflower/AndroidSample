package com.jackie.sample.bean;

/**
 * Created by Jackie on 2016/4/6.
 * 下载的线程实体类
 */
public class ThreadInfo {
    private int mId;
    private String mUrl;
    private int mStart;
    private int mEnd;
    private int mProgress;

    public ThreadInfo(){

    }

    public ThreadInfo(int mId, String mUrl, int mStart, int mEnd, int mProgress) {
        this.mId = mId;
        this.mUrl = mUrl;
        this.mStart = mStart;
        this.mEnd = mEnd;
        this.mProgress = mProgress;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public int getStart() {
        return mStart;
    }

    public void setStart(int start) {
        this.mStart = start;
    }

    public int getEnd() {
        return mEnd;
    }

    public void setEnd(int end) {
        this.mEnd = end;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }
}
