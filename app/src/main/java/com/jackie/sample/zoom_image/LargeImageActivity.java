package com.jackie.sample.zoom_image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jackie on 2017/6/5.
 */

public class LargeImageActivity extends AppCompatActivity {
    private LargeImageView mLargeImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_large_image);

        mLargeImageView = (LargeImageView) findViewById(R.id.large_image_view);

        try {
            InputStream inputStream = getAssets().open("qm.jpg");
            mLargeImageView.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
