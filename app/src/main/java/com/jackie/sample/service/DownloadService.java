package com.jackie.sample.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.jackie.sample.bean.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jackie on 2016/4/6.
 * 下载
 */
public class DownloadService extends Service {
    public static final String DOWNLOAD_ACTION_START = "download_action_start";
    public static final String DOWNLOAD_ACTION_STOP = "download_action_stop";
    public static final String DOWNLOAD_ACTION_UPDATE = "download_action_update";
    public static final String DOWNLOAD_ACTION_FINISHED = "download_action_finished";

    public static final String KEY_DOWNLOAD_FILE_INFO = "key_download_file_info";
    public static final String KEY_DOWNLOAD_LENGTH =  "key_download_length";
    public static final String KEY_DOWNLOAD_ID = "key_download_id";
    public static final String KEY_DOWNLOAD_PROGRESS = "key_download_progress";

    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download";

    private static final int MSG_DOWNLOAD_INIT = 0;

    //下载任务的集合
    private SparseArray<DownloadTask> mDownloadTaskSparseArray = new SparseArray<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DOWNLOAD_INIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    //启动下载任务
                    DownloadTask downloadTask = new DownloadTask(DownloadService.this, fileInfo, 5);
                    downloadTask.download();

                    //把下载任务添加到集合中
                    mDownloadTaskSparseArray.put(fileInfo.getId(), downloadTask);
                    break;
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (DOWNLOAD_ACTION_START.equals(intent.getAction())) {
                FileInfo fileInfo = intent.getParcelableExtra(KEY_DOWNLOAD_FILE_INFO);

                //启动初始化线程
                InitThread initThread = new InitThread(fileInfo);
//                initThread.start();
                DownloadTask.mExecutorService.execute(initThread);
            } else if (DOWNLOAD_ACTION_STOP.equals(intent.getAction())) {
                FileInfo fileInfo = intent.getParcelableExtra(KEY_DOWNLOAD_FILE_INFO);
                //从集合中取出下载任务
                DownloadTask downloadTask = mDownloadTaskSparseArray.get(fileInfo.getId());
                if (downloadTask != null) {
                    //停止下载任务
                    downloadTask.mIsPause = true;
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class InitThread extends Thread {
        private FileInfo fileInfo;

        public InitThread(FileInfo fileInfo) {
            this.fileInfo = fileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile randomAccessFile = null;
            try {
                //连接网络文件
                URL url = new URL(fileInfo.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");

                int length = -1;
                if (connection.getResponseCode() != -1) {
                    //获取文件长度
                    length = connection.getContentLength();
                }

                if (length <= 0) {
                    return;
                }

                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }

                //创建文件
                File file = new File(dir, fileInfo.getFileName());
                randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.setLength(length);

                fileInfo.setLength(length);
                mHandler.obtainMessage(MSG_DOWNLOAD_INIT, fileInfo).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
