package com.jackie.sample.image_code;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.jackie.sample.R;
import com.jackie.sample.utils.ImageCodeUtils;

/**
 * Created by Jackie on 2017/5/12.
 * 图片验证码
 */

public class ImageCodeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageCodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_code);

        mImageCodeView = (ImageView) findViewById(R.id.iv_image_code);
        mImageCodeView.setImageBitmap(ImageCodeUtils.getInstance().generateImageCode());
        mImageCodeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image_code:
                mImageCodeView.setImageBitmap(ImageCodeUtils.getInstance().generateImageCode());
                break;
        }
    }
}
