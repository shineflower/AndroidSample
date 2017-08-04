package com.jackie.sample.share_element;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jackie.sample.R;

public class PhotoActivity extends AppCompatActivity {
    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mSavedInstanceState = savedInstanceState;

        initView();
    }

    private void initView() {
        // 替换Fragment
        if (mSavedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new PhotoListFragment())
                    .commit();
        }

    }
}
