package com.jackie.sample.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.jackie.sample.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Jackie on 2017/7/19
 * 摄像头基础，拍照结果
 */
public class CaptureResultActivity extends AppCompatActivity {
    private String mPicPath;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_result);

        mPicPath = getIntent().getStringExtra("picPath");

        mImageView = (ImageView) findViewById(R.id.image_view);

        try {
            FileInputStream fis = new FileInputStream(mPicPath);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);   //旋转90度矩阵
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            mImageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}