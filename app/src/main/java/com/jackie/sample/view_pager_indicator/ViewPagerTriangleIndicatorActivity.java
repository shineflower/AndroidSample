package com.jackie.sample.view_pager_indicator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jackie on 2017/5/18.
 */

public class ViewPagerTriangleIndicatorActivity extends FragmentActivity {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;

    private List<String> mTabTitleList = Arrays.asList("短信1", "短信2", "短信3", "短信4",
            "短信5", "短信6", "短信7", "短信8", "短信9");
//	private List<String> mTabTitleList = Arrays.asList("短信", "收藏", "推荐");

    private ViewPagerIndicator mViewPagerIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_pager_triangle);

        initView();
        initData();

        //设置Tab上的标题
        mViewPagerIndicator.setTabTitleList(mTabTitleList);
        mViewPager.setAdapter(mAdapter);

        //设置关联的ViewPager
        mViewPagerIndicator.setViewPager(mViewPager, 0);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
    }

    private void initData() {
        for (String tabTitle : mTabTitleList) {
            TabFragment fragment = TabFragment.newInstance(tabTitle);
            mFragmentList.add(fragment);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }
        };
    }
}
