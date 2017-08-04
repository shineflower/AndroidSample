package com.jackie.sample.sliding_menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.SlidingMenu;

public class SlidingMenuActivity extends AppCompatActivity {
    private SlidingMenu mSlidingMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_menu);

        mSlidingMenu = (SlidingMenu) findViewById(R.id.menu);
    }

    public void toggleMenu(View view) {
        mSlidingMenu.toggle();
    }
}
