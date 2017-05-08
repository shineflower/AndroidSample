package com.jackie.sample.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jackie on 2016/4/6.
 * 下载的文件实体类
 */
public class FileInfo implements Parcelable {
    private int mId;
    private String mUrl;
    private String mFileName;
    private int mLength;
    //文件下载的进度
    private int mProgress;

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

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        this.mLength = length;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public FileInfo(int id, String url, String fileName, int length, int progress) {
        this.mId = id;
        this.mUrl = url;
        this.mFileName = fileName;
        this.mLength = length;
        this.mProgress = progress;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "mId=" + mId +
                ", mUrl='" + mUrl + '\'' +
                ", mFileName='" + mFileName + '\'' +
                ", mLength=" + mLength +
                ", mProgress=" + mProgress +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mUrl);
        dest.writeString(this.mFileName);
        dest.writeInt(this.mLength);
        dest.writeInt(this.mProgress);
    }

    protected FileInfo(Parcel in) {
        this.mId = in.readInt();
        this.mUrl = in.readString();
        this.mFileName = in.readString();
        this.mLength = in.readInt();
        this.mProgress = in.readInt();
    }

    public static final Creator<FileInfo> CREATOR = new Creator<FileInfo>() {
        @Override
        public FileInfo createFromParcel(Parcel source) {
            return new FileInfo(source);
        }

        @Override
        public FileInfo[] newArray(int size) {
            return new FileInfo[size];
        }
    };
}
