package com.jackie.sample.divider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.jackie.sample.R;
import com.jackie.sample.adapter.MyFragmentPagerAdapter;
import com.jackie.sample.custom_view.CirclePageIndicator;
import com.jackie.sample.custom_view.CustomViewPagerInternal;
import com.jackie.sample.custom_view.SlideBarView;

import java.util.ArrayList;

public class DividerActivity extends FragmentActivity {
    private CirclePageIndicator mCirclePageIndicator;
    private ArrayList<Fragment> mFragmentList1;
    private SlideBarView mSlideBarView;
    private CustomViewPagerInternal mViewPagerInternal1;

    // 与SlideBarView结合的ViewPager
    private ArrayList<Fragment> mFragmentList2;
    private CustomViewPagerInternal mViewPagerInternal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divider);

        mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.pager_indicator);
        mViewPagerInternal1 = (CustomViewPagerInternal) findViewById(R.id.view_pager_1);
        mViewPagerInternal2 = (CustomViewPagerInternal) findViewById(R.id.view_pager_2);
        mSlideBarView = (SlideBarView) findViewById(R.id.slide_bar_view);

        mFragmentList1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MyPageFragment fragment = new MyPageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            fragment.setArguments(bundle);
            mFragmentList1.add(fragment);
        }

        MyFragmentPagerAdapter adapter1 = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList1);
        mViewPagerInternal1.setAdapter(adapter1);
        mCirclePageIndicator.setViewPager(mViewPagerInternal1);

        // SideBarView
        mFragmentList2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MyPageFragment fragment = new MyPageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            fragment.setArguments(bundle);
            mFragmentList2.add(fragment);
        }

        MyFragmentPagerAdapter adapter2 = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList2);
        mViewPagerInternal2.setAdapter(adapter2);
        mSlideBarView.setViewPagerInternal(mViewPagerInternal2);

        mSlideBarView.setOnSlideChangeListener(new SlideBarView.OnSlideChangeListener() {
            @Override
            public void onSlideChange(int page) {
                mViewPagerInternal2.setCurrentItem(page);
//                mViewPagerInternal2.setCurrentItem(page, false);
            }
        });



//        Intent intent=new Intent(Intent.ACTION_SEND);
//
//        intent.setType("text/plain");
//
//        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//        intent.putExtra(Intent.EXTRA_TEXT, "hello，world！");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(Intent.createChooser(intent, "share"));
    }
}
