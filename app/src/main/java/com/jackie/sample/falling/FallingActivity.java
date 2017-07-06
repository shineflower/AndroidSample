package com.jackie.sample.falling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.FallingView;

import java.lang.reflect.Method;

public class FallingActivity extends AppCompatActivity {
    private SeekBar mSizeSeekBar, mSpeedSeekBar, mDensitySeekBar;
    private FallingView mFallingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falling);

        mFallingView = (FallingView) findViewById(R.id.falling_view);

        mSizeSeekBar = (SeekBar) findViewById(R.id.seek_size);
        mSpeedSeekBar = (SeekBar) findViewById(R.id.seek_speed);
        mDensitySeekBar = (SeekBar) findViewById(R.id.seek_density);

        initEvent();
    }

    private void initEvent() {

        mSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFallingView.setScale(progress);    //设置碎片的大小，数值越大，碎片越小，默认值是3
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFallingView.setDelay(progress);    //设置碎片飘落的速度，数值越大，飘落的越慢，默认值是10
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mDensitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFallingView.setDensity(progress);  //设置密度，数值越大，碎片越密集
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.falling_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if ("MenuBuilder".equals(menu.getClass().getSimpleName())) {
                try {
                    Method method = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {

                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_flower:
                mFallingView.setImageResource(R.drawable.falling_menu_flower);
                break;
            case R.id.menu_dandelion:
                mFallingView.setImageResource(R.drawable.falling_menu_dandelion);
                break;
            case R.id.menu_heart:
                mFallingView.setImageResource(R.drawable.falling_menu_heart);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
