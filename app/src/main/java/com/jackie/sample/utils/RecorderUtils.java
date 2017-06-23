package com.jackie.sample.utils;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *  Created by Jackie on 2017/6/22.
 *  录音工具类
 */
public class RecorderUtils {
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;

    public RecorderUtils(MediaRecorder mediaRecorder){
        this.mMediaRecorder = mediaRecorder;
        mMediaPlayer = new MediaPlayer();

        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {//异常处理的地方。
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mMediaPlayer.reset();
                return false;
            }
        });
    }

    public void startRecorder(List<String> list) {
        try {
            if(mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
            }

            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return;
            }

            /* 创建录音频临时文件， JackieRecorder开头，.mp3结尾， 最后一个参数是文件录音完之后存放的路径 */
            File audioFile = File.createTempFile("JackieRecorder", ".mp3", makeDir());
            /* 设置录音来源为麦克风 */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mMediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            list.add(audioFile.getName());
        }
        catch (Exception e) {
//            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 停止录音
     */
    public void stopRecorder() {
        /* 停止录音 */
        if (mMediaRecorder == null) {
            return ;
        }

        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;

    }

    /**
     * MediaPlayer播放录音(文件)
     * @param path  文件路径
     */
    public void playRecorderPath(String path) throws IOException {
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(path);
        mMediaPlayer.prepare();
        mMediaPlayer.start();

//        默认播放器下载   使用默认播放器播放录音
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        String type = getMIMEType(new File(path));
//        intent.setDataAndType(Uri.fromFile(new File(path)), type);
//        mContext.startActivity(intent);
    }

    /**
     * 使MediaPlayer播放录音(网络文件)
     */
    public void playRecorderUrl(String url, MediaPlayer.OnCompletionListener listener) {
        try {
//            stop();
            mMediaPlayer.setOnCompletionListener(listener);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            stop();
            e.printStackTrace();
        }
    }

    /**
     * 获取文件类型
     * @param file 文件
     * @return
     */
    public String getMIMEType(File file) {
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length())
                .toLowerCase();
        String type;
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
                || end.equals("amr") || end.equals("mpeg")
                || end.equals("mp4")) {
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif")
                || end.equals("png") || end.equals("jpeg")) {
            type = "image";
        } else {
            type = "*";
        }

        type += "/*";

        return type;
    }

    /**
     * 删除录音
     */
    public void deleteRecorder() {

    }

    /**
     * 创建文件夹
     * @return
     */
    public File makeDir() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/JackieRecorder");

        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }

    /**
     * 删除文件夹里的文件
     * @return
     */
    public  void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if(file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
//                file.delete();
                return;
            }

           for (int i = 0; i < childFiles.length; i++) {
               delete(childFiles[i]);
           }
        }
    }

    /**
     * MediaPlayer是否在播放
     * @return
     */
    public boolean isPlaying(){
        if(mMediaPlayer != null){
            return mMediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    /**
     * MediaPlayer去停止
     * @return
     */
    public void stop(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
    }
}
