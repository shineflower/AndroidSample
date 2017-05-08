package com.jackie.sample.service;

import android.content.Context;
import android.content.Intent;

import com.jackie.sample.bean.FileInfo;
import com.jackie.sample.bean.ThreadInfo;
import com.jackie.sample.db.DBDao;
import com.jackie.sample.db.DBDaoImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jackie on 2016/4/6.
 * 下载任务类
 */
public class DownloadTask {
    private Context mContext;
    private FileInfo mFileInfo;
    private DBDao mDBDao;
    private int mProgress;

    public boolean mIsPause; //标识是否暂停下载

    private int mThreadCount = 1;  //线程数量
    private List<DownloadThread> mDownloadThreadList;

    public static ExecutorService mExecutorService = Executors.newCachedThreadPool();

    public DownloadTask(Context context, FileInfo fileInfo, int threadCount) {
        this.mContext = context;
        this.mFileInfo = fileInfo;
        this.mThreadCount = threadCount;

        mDBDao = new DBDaoImpl(context);
    }

    public void download() {
        //读取数据库的线程信息
        List<ThreadInfo> threadInfoList =  mDBDao.getThreads(mFileInfo.getUrl());

        /**
         * 单线程下载
         */
//        ThreadInfo threadInfo;
//        if (threadInfoList.size() == 0) { //第一次下载
//            //初始化线程信息对象
//            threadInfo = new ThreadInfo(0, mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);
//        } else {
//            threadInfo = mDBDao.getThreads(mFileInfo.getUrl()).get(0);  //单线程
//        }
//
//        //创建子线程开始下载
//        DownloadThread downloadThread = new DownloadThread(threadInfo);
//        downloadThread.start();

        /**
         * 多线程分段下载
         */
        if (threadInfoList.size() == 0) {
            //获得每个线程的下载长度
            int length = mFileInfo.getLength() / mThreadCount;

            for (int i = 0; i < mThreadCount; i++) {
                ThreadInfo threadInfo = new ThreadInfo(i, mFileInfo.getUrl(), i * length,
                        (i + 1) * length - 1, 0);

                //有可能除不尽，最后一个线程的结束位置就是整个文件的长度
                if (i == mThreadCount - 1) {
                    threadInfo.setEnd(mFileInfo.getLength());
                }

                //添加线程信息到集合
                threadInfoList.add(threadInfo);

                //向数据库插入线程信息
                mDBDao.insert(threadInfo);
            }
        }

        mDownloadThreadList = new ArrayList<>();
        //启动多个线程进行下载
        for (ThreadInfo threadInfo : threadInfoList) {
            DownloadThread downloadThread = new DownloadThread(threadInfo);
//            downloadThread.start();
            mExecutorService.execute(downloadThread);
            //添加线程到集合
            mDownloadThreadList.add(downloadThread);
        }
    }

    public class DownloadThread extends Thread {
        private ThreadInfo threadInfo;
        private boolean isFinished;   //标识线程是否执行结束

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile randomAccessFile = null;
            InputStream inputStream = null;

            try {
                URL url = new URL(threadInfo.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");

                //设置下载位置
                int start = threadInfo.getStart() + threadInfo.getProgress();
                connection.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());

                //设置文件写入位置
                File file = new File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
                randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.seek(start);

                Intent intent = new Intent(DownloadService.DOWNLOAD_ACTION_UPDATE);
                mProgress += threadInfo.getProgress();

                //开始下载
                if (connection.getResponseCode() != -1) {
                    //读取数据
                    inputStream = connection.getInputStream();
                    byte[] buffer = new byte[1024 << 2];
                    int len;

                    long time = System.currentTimeMillis();
                    while ((len = inputStream.read(buffer)) != -1) {
                        //写入文件
                        randomAccessFile.write(buffer, 0, len);
                        //累加整个文件的完成进度
                        mProgress += len;

                        //累加每个线程的完成进度
                        threadInfo.setProgress(threadInfo.getProgress() + len);

                        if (System.currentTimeMillis() - time > 1000) {
                            time = System.currentTimeMillis();

                            //下载的进度发送广播，更新UI
                            /**
                             * int mProgress，随着mProgress不断增大，当mProgress * 100的值大于2的31次方时，会变成负数
                             * mProgress单位是byte，右移20位，转化成M
                             */
//                            intent.putExtra(DownloadService.KEY_DOWNLOAD_PROGRESS, mProgress * 100 / mFileInfo.getLength());
                            intent.putExtra(DownloadService.KEY_DOWNLOAD_ID, mFileInfo.getId());
                            intent.putExtra(DownloadService.KEY_DOWNLOAD_PROGRESS, (mProgress >> 20) * 100 / (mFileInfo.getLength() >> 20));
                            intent.putExtra(DownloadService.KEY_DOWNLOAD_LENGTH, mFileInfo.getLength());
                            mContext.sendBroadcast(intent);
                        }

                        //暂停的时候，保存下载的进度
                        if (mIsPause) {
                            //单线程，保存整个文件的下载进度
//                            mDBDao.update(threadInfo.getId(), threadInfo.getUrl(), mProgress);

                            //多线程，保存每个线程的下载进度
                            mDBDao.update(threadInfo.getId(), threadInfo.getUrl(), threadInfo.getProgress());
                            return;
                        }
                    }
                }

                //标识线程执行完毕
                isFinished = true;

                //检查是否下载任务执行完毕
                checkAllThreadFinished();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

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

    /**
     * 多线程下载同一个文件，判断是否所有线程执行完成
     */
    private synchronized void checkAllThreadFinished() {
        boolean isAllThreadFinished = true;

        for (DownloadThread downloadThread : mDownloadThreadList) {
            if (!downloadThread.isFinished) {
                isAllThreadFinished = false;
                break;
            }
        }

        if (isAllThreadFinished) {
            //下载完成后，删除线程信息
            mDBDao.delete(mFileInfo.getUrl());

            //发送广播，通知UI下载任务结束
            Intent intent = new Intent(DownloadService.DOWNLOAD_ACTION_FINISHED);
            intent.putExtra(DownloadService.KEY_DOWNLOAD_FILE_INFO, mFileInfo);
            mContext.sendBroadcast(intent);
        }
    }
}
