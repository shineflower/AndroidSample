package com.jackie.sample.wechat_tab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.fragment.ChatFragment;
import com.jackie.sample.fragment.ContactFragment;
import com.jackie.sample.fragment.FriendFragment;
import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

public class WechatTabActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    LinearLayout mChatLayout;
    private TextView mChatTextView;
    private TextView mFriendTextView;
    private TextView mContactTextView;
    private BadgeView mBadgeView;
    private ImageView mIndicator;

    private int mThirdScreenWidth;

    private List<Fragment> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_tab);

        initView();
        initData();
        data2View();
        initEvent();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mChatLayout = (LinearLayout) findViewById(R.id.chat_layout);
        mChatTextView = (TextView) findViewById(R.id.chat);
        mFriendTextView = (TextView) findViewById(R.id.friend);
        mContactTextView = (TextView) findViewById(R.id.contact);
        mIndicator = (ImageView) findViewById(R.id.indicator);

        //初始化indicator的位置
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(outMetrics);
//        mThirdScreenWidth = outMetrics.widthPixels / 3;
        mThirdScreenWidth = getResources().getDisplayMetrics().widthPixels / 3;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIndicator.getLayoutParams();
        params.width = mThirdScreenWidth;
        mIndicator.setLayoutParams(params);
    }

    private void initData() {
        ChatFragment chatFragment = new ChatFragment();
        FriendFragment friendFragment = new FriendFragment();
        ContactFragment contactFragment = new ContactFragment();

        mList.add(chatFragment);
        mList.add(friendFragment);
        mList.add(contactFragment);
    }

    private void data2View() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }

            @Override
            public int getCount() {
                return mList.size();
            }
        });
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("Jackie", "position: " +  position + "  positionOffset: " + positionOffset);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIndicator.getLayoutParams();
                params.leftMargin = (int) (mThirdScreenWidth * (position + positionOffset));
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                clearSelection();
                switch (position) {
                    case 0:
                        setSelection(0);
                        break;
                    case 1:
                        setSelection(1);
                        break;
                    case 2:
                        setSelection(2);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSelection(int index) {
        switch (index) {
            case 0:
                if (mBadgeView != null) {
                    mChatLayout.removeView(mBadgeView);
                }
                mBadgeView = new BadgeView(this);
                //设置BadgeView的半径和背景色
//                mBadgeView.setBackground(10, Color.BLUE);
                mBadgeView.setBadgeCount(7);
                mChatLayout.addView(mBadgeView);

                mChatTextView.setTextColor(Color.parseColor("#008000"));
                break;
            case 1:
                mFriendTextView.setTextColor(Color.parseColor("#008000"));
                break;
            case 2:
                mContactTextView.setTextColor(Color.parseColor("#008000"));
                break;
        }
    }

    private void clearSelection() {
        mChatTextView.setTextColor(Color.BLACK);
        mFriendTextView.setTextColor(Color.BLACK);
        mContactTextView.setTextColor(Color.BLACK);
    }
}
