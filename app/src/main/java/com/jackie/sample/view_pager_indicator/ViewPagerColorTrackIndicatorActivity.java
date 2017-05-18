package com.jackie.sample.view_pager_indicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ColorTrackView;
import com.jackie.sample.fragment.TabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/5/17.
 */

public class ViewPagerColorTrackIndicatorActivity extends FragmentActivity {
    private String[] mTabTitles = new String[] { "简介", "评价", "相关" };
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private TabFragment[] mFragments = new TabFragment[mTabTitles.length];
    private List<ColorTrackView> mTabList = new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_color_track);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mTabList.add((ColorTrackView) findViewById(R.id.tab_01));
        mTabList.add((ColorTrackView) findViewById(R.id.tab_02));
        mTabList.add((ColorTrackView) findViewById(R.id.tab_03));
    }

    private void initData() {
        for (int i = 0; i < mTabTitles.length; i++) {
            mFragments[i] = TabFragment.newInstance(mTabTitles[i]);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    ColorTrackView leftColorTrackView = mTabList.get(position);
                    ColorTrackView rightColorTrackView = mTabList.get(position + 1);

                    leftColorTrackView.setDirection(1);
                    rightColorTrackView.setDirection(0);

                    leftColorTrackView.setProgress(1 - positionOffset);
                    rightColorTrackView.setProgress(positionOffset);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
