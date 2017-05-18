package com.jackie.sample.wechat_tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ChangeColorIconWithTextView;
import com.jackie.sample.fragment.TabFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WechatTabWithoutIndicatorActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    private String[] mTitles = new String[] { "微信", "通讯录", "发现", "我" };

    private List<ChangeColorIconWithTextView> mTabIndicatorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_tab_without_indicator);

        setOverflowShowingAlways();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        initData();

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    private void initData() {
        for (String title : mTitles) {
            TabFragment tabFragment = new TabFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            tabFragment.setArguments(args);
            mFragmentList.add(tabFragment);
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

        initTabIndicator();
    }

    private void initTabIndicator() {
        ChangeColorIconWithTextView indicatorWechat = (ChangeColorIconWithTextView) findViewById(R.id.indicator_wechat);
        ChangeColorIconWithTextView indicatorContact = (ChangeColorIconWithTextView) findViewById(R.id.indicator_contact);
        ChangeColorIconWithTextView indicatorDiscovery = (ChangeColorIconWithTextView) findViewById(R.id.indicator_discovery);
        ChangeColorIconWithTextView indicatorMe = (ChangeColorIconWithTextView) findViewById(R.id.indicator_me);

        mTabIndicatorList.add(indicatorWechat);
        mTabIndicatorList.add(indicatorContact);
        mTabIndicatorList.add(indicatorDiscovery);
        mTabIndicatorList.add(indicatorMe);

        indicatorWechat.setOnClickListener(this);
        indicatorContact.setOnClickListener(this);
        indicatorDiscovery.setOnClickListener(this);
        indicatorMe.setOnClickListener(this);

        indicatorWechat.setIconAlpha(1.0f);
    }

    @Override
    public void onPageSelected(int arg0) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.e("TAG", "position = " + position + " , positionOffset = " + positionOffset);

        if (positionOffset > 0) {
            ChangeColorIconWithTextView leftIndicator = mTabIndicatorList.get(position);
            ChangeColorIconWithTextView rightIndicator = mTabIndicatorList.get(position + 1);

            leftIndicator.setIconAlpha(1 - positionOffset);
            rightIndicator.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        resetOtherTabs();

        switch (v.getId()) {
            case R.id.indicator_wechat:
                mTabIndicatorList.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.indicator_contact:
                mTabIndicatorList.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.indicator_discovery:
                mTabIndicatorList.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.indicator_me:
                mTabIndicatorList.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 重置其他的Tab
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicatorList.size(); i++) {
            mTabIndicatorList.get(i).setIconAlpha(0);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowShowingAlways() {
        try {
            // true if a permanent menu key is present, false otherwise.
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
