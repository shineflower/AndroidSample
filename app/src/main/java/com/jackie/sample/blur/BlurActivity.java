package com.jackie.sample.blur;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.utils.BlurUtils;

/**
 * Created by Jackie on 2017/5/11.
 * 高斯模糊
 */

public class BlurActivity extends Activity {
    private ImageView mBlurImage, mOriginImage;
    private SeekBar mSeekBar;
    private TextView mSeekProgress;

    private int mAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mBlurImage = (ImageView) findViewById(R.id.blur_image);
        mOriginImage = (ImageView) findViewById(R.id.origin_image);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        mSeekProgress = (TextView) findViewById(R.id.seek_progress);
    }

    private void initData() {
        // 获取图片
        Bitmap originBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blur);
        Bitmap blurBitmap = BlurUtils.blur(this, originBitmap);

        // 填充模糊后的图像和原图
        mBlurImage.setImageBitmap(blurBitmap);
        mOriginImage.setImageBitmap(originBitmap);
    }

    private void initEvent() {
        mSeekBar.setMax(100);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;

                mOriginImage.setAlpha((int) (255 - mAlpha * 2.55));
                mSeekProgress.setText(String.valueOf(mAlpha));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
