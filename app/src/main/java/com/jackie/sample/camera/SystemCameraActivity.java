package com.jackie.sample.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.jackie.sample.BuildConfig;
import com.jackie.sample.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Jackie on 2017/7/19
 * 摄像头基础，启动系统相机
 */
public class SystemCameraActivity extends AppCompatActivity {

    private ImageView mImageView;
    private String mFilePath;

    private static final int REQUEST_1 = 1;
    private static final int REQUEST_2 = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_camera);

        mImageView = (ImageView) findViewById(R.id.image_view);

        mFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + System.currentTimeMillis() + ".jpg";
    }

    /**
     * 启动系统相机，默认返回缩略图
     */
    public void startCamera1(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_1);
    }

    /**
     * 启动系统相机，实现返回原图
     */
    public void startCamera2(){
        //隐式Intent调用
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri photoUri;

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", new File(mFilePath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_2);
        } else {
            // 把文件地址转换成Uri格式
            photoUri = Uri.fromFile(new File(mFilePath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_2);
        }

        startActivityForResult(intent, REQUEST_2);
    }

    /**
     * 启动自定义相机
     */
    public void customCamera(){
        Intent intent = new Intent(this, CustomCameraActivity.class);
        startActivity(intent);
    }


    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data //缩略图
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_1) {   //缩略图
                Bundle bundle = data.getExtras();
                Bitmap bmp = (Bitmap) bundle.get("data");
                mImageView.setImageBitmap(bmp);
            } else if (requestCode == REQUEST_2) { //原图
                FileInputStream fis = null;

                try {
                    fis = new FileInputStream(mFilePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    mImageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}