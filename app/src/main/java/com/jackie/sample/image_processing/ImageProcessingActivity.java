package com.jackie.sample.image_processing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jackie.sample.R;

public class ImageProcessingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mPrimaryColor;
    private Button mColorMatrix;
    private Button mPixelsEffect;
    private Button mMatrix;
    private Button mRoundRectXfermode;
    private Button mBitmapShader;
    private Button mReflect;
    private Button mMesh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processing);

        initView();
        initEvent();
    }

    private void initView() {
        mPrimaryColor = (Button) findViewById(R.id.primary_color);
        mColorMatrix = (Button) findViewById(R.id.color_matrix);
        mPixelsEffect = (Button) findViewById(R.id.pixels_effect);
        mMatrix = (Button) findViewById(R.id.matrix);
        mRoundRectXfermode = (Button) findViewById(R.id.round_view_xfermode);
        mBitmapShader = (Button) findViewById(R.id.bitmap_shader);
        mReflect = (Button) findViewById(R.id.reflect);
        mMesh = (Button) findViewById(R.id.mesh);
    }

    private void initEvent() {
        mPrimaryColor.setOnClickListener(this);
        mColorMatrix.setOnClickListener(this);
        mPixelsEffect.setOnClickListener(this);
        mMatrix.setOnClickListener(this);
        mRoundRectXfermode.setOnClickListener(this);
        mBitmapShader.setOnClickListener(this);
        mReflect.setOnClickListener(this);
        mMesh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.primary_color:
                intent = new Intent(this, PrimaryColorActivity.class);
                break;
            case R.id.color_matrix:
                intent = new Intent(this, ColorMatrixActivity.class);
                break;
            case R.id.pixels_effect:
                intent = new Intent(this, PixelsEffectActivity.class);
                break;
            case R.id.matrix:
                intent = new Intent(this, MatrixActivity.class);
                break;
            case R.id.round_view_xfermode:
                intent = new Intent(this, RoundRectXfermodeActivity.class);
                break;
            case R.id.bitmap_shader:
                intent = new Intent(this, BitmapShaderActivity.class);
                break;
            case R.id.reflect:
                intent = new Intent(this, ReflectActivity.class);
                break;
            case R.id.mesh:
                intent = new Intent(this, MeshActivity.class);
                break;
        }

        startActivity(intent);
    }
}
