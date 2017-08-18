package com.jackie.sample.view_pager_indicator;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;
import com.jackie.sample.view_pager_indicator.view.BezierPagerIndicator;
import com.jackie.sample.view_pager_indicator.view.CommonNavigator;
import com.jackie.sample.view_pager_indicator.view.CommonNavigatorAdapter;
import com.jackie.sample.view_pager_indicator.view.IPagerIndicator;
import com.jackie.sample.view_pager_indicator.view.IPagerTitleView;
import com.jackie.sample.view_pager_indicator.view.MagicIndicator;
import com.jackie.sample.view_pager_indicator.view.SimplePagerTitleView;
import com.jackie.sample.view_pager_indicator.view.ViewPagerHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jackie on 2017/8/18.
 */

public class ViewPagerReboundIndicatorActivity extends AppCompatActivity {
    private ViewPager mViewPager;

    private List<String> mList;

    private static final String[] CHANNELS = new String[]{ "CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "HONEYCOMB", "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT" };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_pager_rebound);

        mList = Arrays.asList(CHANNELS);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        ExamplePagerAdapter adapter = new ExamplePagerAdapter(mList);
        mViewPager.setAdapter(adapter);

        initMagicIndicator1();
        initMagicIndicator2();
    }

    private void initMagicIndicator1() {
        MagicIndicator magicIndicator1 = (MagicIndicator) findViewById(R.id.magic_indicator_1);
        magicIndicator1.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mList == null ? 0 : mList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mList.get(index));
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor("#73e8f4"), Color.parseColor("#76b0ff"), Color.parseColor("#c683fe"));
                return indicator;
            }
        });

        magicIndicator1.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator1, mViewPager);
    }

    private void initMagicIndicator2() {
        MagicIndicator magicIndicator2 = (MagicIndicator) findViewById(R.id.magic_indicator_2);
        magicIndicator2.setBackgroundColor(Color.parseColor("#fafafa"));

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mList == null ? 0 : mList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#9e9e9e"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#00c853"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });

                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(DensityUtils.dp2px(context, 6));
                indicator.setLineWidth(DensityUtils.dp2px(context, 10));
                indicator.setRoundRadius(DensityUtils.dp2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#00c853"));
                return indicator;
            }
        });

        magicIndicator2.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator2, mViewPager);
    }
}
