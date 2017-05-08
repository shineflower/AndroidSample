package com.jackie.sample.zoom_image_view;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ZoomImageView;

import java.util.ArrayList;
import java.util.List;

public class ZoomActivity extends AppCompatActivity {
    private ViewPager mViewPager;

    private int[] mImgResIds = { R.drawable.yifei1, R.drawable.yifei2, R.drawable.yifei3, R.drawable.yifei4, R.drawable.yifei5, R.drawable.yifei6 };
    private List<ZoomImageView> mZoomImageViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        for (int i = 0; i < mImgResIds.length; i++) {
            ZoomImageView zoomImageView = new ZoomImageView(this);
            zoomImageView.setImageResource(mImgResIds[i]);
            mZoomImageViewList.add(zoomImageView);
        }

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mZoomImageViewList.get(position));
                return mZoomImageViewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mZoomImageViewList.get(position));
            }

            @Override
            public int getCount() {
                return mZoomImageViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }
}
