package com.jackie.sample.wechat_camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.wechat_camera.listener.JCameraListener;
import com.jackie.sample.wechat_camera.view.JCameraView;

import java.io.File;

/**
 * Created by Jackie on 2017/6/8.
 */

public class WechatCameraActivity extends AppCompatActivity {
    private JCameraView mJCameraView;
    private boolean mGranted = false;

    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wechat_camera);

        mJCameraView = (JCameraView) findViewById(R.id.camera_view);

        //设置视频保存路径
        mJCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        //JCameraView监听
        mJCameraView.setJCameraListener(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
            }

            @Override
            public void recordSuccess(String url) {

            }

            @Override
            public void quit() {
                WechatCameraActivity.this.finish();
            }
        });

        //6.0 动态权限获取
        getPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGranted) {
            mJCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mJCameraView.onPause();
    }

    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //具有权限
                mGranted = true;
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(WechatCameraActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
                mGranted = false;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }

                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }

                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }

                if (size == 0) {
                    mGranted = true;
                    mJCameraView.onResume();
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
