package com.jackie.sample.image_processing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.jackie.sample.R;
import com.jackie.sample.utils.ImageHelper;

public class PrimaryColorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private ImageView mImageView;
    private Bitmap mBitmap;

    private SeekBar mSeekBarHue, mSeekBarSaturation, mSeekBarLum;
    private float mHue, mSaturation, mLum;

    private static int MAX_VALUE = 255;
    private static int MIDDLE_VALUE = 127;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_color);

        initView();
        initEvent();
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.source);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test3);
        mImageView.setImageBitmap(mBitmap);

        mSeekBarHue = (SeekBar) findViewById(R.id.seek_bar_hue);
        mSeekBarSaturation = (SeekBar) findViewById(R.id.seek_bar_saturation);
        mSeekBarLum = (SeekBar) findViewById(R.id.seek_bar_lum);

        mSeekBarHue.setMax(MAX_VALUE);
        mSeekBarSaturation.setMax(MAX_VALUE);
        mSeekBarLum.setMax(MAX_VALUE);

        mSeekBarHue.setProgress(MIDDLE_VALUE);
        mSeekBarSaturation.setProgress(MIDDLE_VALUE);
        mSeekBarLum.setProgress(MIDDLE_VALUE);
    }

    private void initEvent() {
        mSeekBarHue.setOnSeekBarChangeListener(this);
        mSeekBarSaturation.setOnSeekBarChangeListener(this);
        mSeekBarLum.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seek_bar_hue:
                mHue = (progress - MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE * 180;
                break;
            case R.id.seek_bar_saturation:
                mSaturation = progress * 1.0F / MIDDLE_VALUE;
                break;
            case R.id.seek_bar_lum:
                mLum = progress * 1.0F / MIDDLE_VALUE;
                break;
        }

        mImageView.setImageBitmap(ImageHelper.handleImageEffect(mBitmap, mHue, mSaturation, mLum));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
