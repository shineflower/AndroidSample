package com.jackie.sample.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/1/14.
 * 仿IOS分页器
 */

public class SlideBarView extends RelativeLayout {
    private CustomViewPagerInternal mViewPagerInternal;

    private Context mContext;
    private LayoutInflater mInflater;
    private View mView;
    private View mSlideBar;
    private PopupWindow mPopupWindow;   // 点击后弹出的PopupWindow
    private TextView mSlideText;    // 点击后弹出的PopupWindow上的text
    private TextView mSlideBlock;   // 滑块
    private int mDp40 = 0;
    private String mBound = "no";   // no表示没到边界，left为到左边界了，right表示到右边界了
    private int mCurrentPage = 0;   // 当前选中了哪一页

    private OnSlideChangeListener mOnSlideChangeListener;

    public void setViewPagerInternal(CustomViewPagerInternal mViewPagerInternal) {
        this.mViewPagerInternal = mViewPagerInternal;

        setTotalPage(mViewPagerInternal.getAdapter().getCount());

        mViewPagerInternal.setOnPageChangeListener(new CustomViewPagerInternal.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrPage(position);
                mSlideBlock.setText((mCurrentPage + 1) + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public SlideBarView(Context context) {
        this(context, null);
    }

    public SlideBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        mContext = context;

        mInflater = LayoutInflater.from(mContext);
        mDp40 = DensityUtils.dp2px(mContext, 40);
        mView = mInflater.inflate(R.layout.slide_bar_layout, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mView, params);

        mSlideBar = mView.findViewById(R.id.slide_bar_view);
        mSlideBlock = (TextView) mView.findViewById(R.id.slide_block);

        mSlideBar.setOnTouchListener(new OnTouchListener() {
            int currentX = 0;
            int startX = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    currentX = (int) event.getX();
                    startX = (int) event.getX();

                    // 设置滑块的滑动, 手指第一次点下去把滑块放到手指上
                    int left = currentX - mSlideBlock.getMeasuredWidth() / 2;
                    int top = mSlideBlock.getTop();
                    int right = left + mSlideBlock.getMeasuredWidth();
                    int bottom = mSlideBlock.getBottom();

                    if (left < 0) {
                        left = 0;
                        right = mSlideBlock.getWidth();
                    }

                    if (right > mSlideBar.getMeasuredWidth()) {
                        left = mSlideBar.getMeasuredWidth() - mSlideBlock.getMeasuredWidth();
                        right = mSlideBar.getMeasuredWidth();
                    }

                    mSlideBlock.layout(left, top, right, bottom);

                    // 手指按下弹出PopupWindow
                    int[] loc = new int[2];
                    mSlideBar.getLocationInWindow(loc);
//                    mPopupWindow.showAsDropDown(mSlideBar, 0, 0);
                    mPopupWindow.showAtLocation(mSlideBar, Gravity.NO_GRAVITY, loc[0], loc[1] - mPopupWindow.getHeight());
                    mPopupWindow.update(currentX, loc[1] - mDp40, mPopupWindow.getWidth(), mPopupWindow.getHeight(), true);
                    mCurrentPage = currentX * totalPage / mSlideBar.getMeasuredWidth();

                    if (mCurrentPage < 0) {
                        mCurrentPage = 0;
                    }

                    if (mCurrentPage >= totalPage) {
                        mCurrentPage = totalPage - 1;
                    }

                    mSlideText.setText((mCurrentPage + 1) + "");
                    mSlideBlock.setText((mCurrentPage + 1) + "");
                } else if (MotionEvent.ACTION_MOVE == event.getAction()) {
                    currentX = (int) event.getX();
                    mCurrentPage = currentX * totalPage / mSlideBar.getMeasuredWidth();
                    if (mCurrentPage < 0) {
                        mCurrentPage = 0;
                    }

                    if (mCurrentPage >= totalPage) {
                        mCurrentPage = totalPage - 1;
                    }

                    // 设置滑块的滑动
                    int left = currentX - mSlideBlock.getMeasuredWidth() / 2;
                    int top = mSlideBlock.getTop();
                    int right = left + mSlideBlock.getMeasuredWidth();
                    int bottom = mSlideBlock.getBottom();

                    // 左边界处理
                    if (left <= 0) {
                        mBound = "left";
                    } else  if (right >= mSlideBar.getMeasuredWidth()) {
                        mBound = "right";
                    } else {
                        mBound = "no";
                    }

                    if (left < 0) {
                        left = 0;
                        right = mSlideBlock.getWidth();
                    }

                    if (right > mSlideBar.getMeasuredWidth()) {
                        left = mSlideBar.getMeasuredWidth() - mSlideBlock.getMeasuredWidth();
                        right = mSlideBar.getMeasuredWidth();
                    }

                    mSlideBlock.layout(left, top, right, bottom);
                    startX = currentX;

                    if (mPopupWindow != null) {
                        mSlideText.setText((mCurrentPage + 1) + "");
                        mSlideBlock.setText((mCurrentPage + 1) + "");

                        // 设置PopWindow的滑动
                        if ("no".equals(mBound)) {
                            int[] loc = new int[2];
                            mSlideBar.getLocationInWindow(loc);
                            mPopupWindow.update(currentX, loc[1] - mDp40, mPopupWindow.getWidth(), mPopupWindow.getHeight(), true);
                        }
                    }
                } else if (MotionEvent.ACTION_UP == event.getAction()) {
                    mPopupWindow.dismiss();

                    if (mOnSlideChangeListener != null) {
                        if (mCurrentPage == totalPage) {
                            // 防止ViewPager越界
                            mCurrentPage = totalPage - 1;
                        }

                        mOnSlideChangeListener.onSlideChange(mCurrentPage);
                    }
                }

                return true;
            }
        });

        // 初始化PopupWindow
        View contentView = mInflater.inflate(R.layout.popup_window_view, null);
        mSlideText = (TextView) contentView.findViewById(R.id.slide_text);
        mPopupWindow = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(contentView);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_bg));
        mPopupWindow.setAnimationStyle(0);
    }

    public interface OnSlideChangeListener {
        void onSlideChange(int page);
    }

    public void setOnSlideChangeListener(OnSlideChangeListener onSlideChangeListener) {
        this.mOnSlideChangeListener = onSlideChangeListener;
    }

    int totalPage = 0;
    // 设置总页数
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    // 设置当前页数
    public void setCurrPage(int page) {
        mCurrentPage = page;
        int left;

        if (page == 0) {
            left = 0;
        } else if (page == totalPage - 1) {
            left = mSlideBar.getMeasuredWidth() - mSlideBlock.getMeasuredWidth();
        } else {
            left = mCurrentPage * mSlideBar.getMeasuredWidth() / (totalPage - 1) - mSlideBlock.getMeasuredWidth() / 2;
        }

        int top = mSlideBlock.getTop();
        int right = left + mSlideBlock.getMeasuredWidth();
        int bottom = mSlideBlock.getBottom();

        mSlideBlock.layout(left, top, right, bottom);
    }
}
