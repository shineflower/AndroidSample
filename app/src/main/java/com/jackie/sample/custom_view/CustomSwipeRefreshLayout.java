package com.jackie.sample.custom_view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.jackie.sample.R;
import com.jackie.sample.utils.LogUtils;

/**
 * Created by Jackie on 2018/1/11
 * 自定义下拉刷新上拉加载控件 目前只支持ListView
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout implements OnScrollListener {
	private Context mContext;

    // 滑动到最下面时的上拉操作
    private int mTouchSlop;

    // ListView
    private ListView mListView;

    // 上拉监听器, 到了最底部的上拉加载操作
    private OnLoadListener mOnLoadListener;
    private OnScrollListener mOnScrollListener;
    private OnScrollStateChangeListener mOnScrollStateChangeListener;
    private OnPullRefreshListener mOnPullRefreshListener;

    // ListView的加载中FooterView
    private View mHasMoreDataFooterView;
    private View mNoMoreDataFooterView;

    // 按下时的y坐标
    private int mDownY;
    // 移动时的y坐标, 与mDownY一起用于滑动到底部时判断是上拉还是下拉
    private int mMoveY;
    // 是否在加载中 (上拉加载更多)
    private boolean mIsLoading = false;

    private boolean mIsNoMoreData = false;   // 是否没有更多数据  true 没有更多数据 false 有更多数据

    private int mCanLoadCount = 6;
    private int mStartLoadCount = 3;


    public CustomSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    private void initView(Context context) {
		mContext = context;

		// 表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件，如ViewPager就是用这个距离来判断用户是否翻页
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        // 设置下拉progress的开始位置和结束位置
//        setProgressViewOffset(false, DensityUtils.dp2px(context, 20), DensityUtils.dp2px(context, 80));

        mNoMoreDataFooterView = LayoutInflater.from(mContext).inflate(R.layout.listview_footer_no_data, null);

        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mOnPullRefreshListener != null) {
                    mOnPullRefreshListener.onRefresh();

                    mIsNoMoreData = false;
                }
            }
        });
	}

    @Override  
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {  
        super.onLayout(changed, left, top, right, bottom);

        // 初始化ListView对象  
        if (mListView == null) {
            getListView();
        }
    }  
  
    /** 
     * 获取ListView对象 
     */  
    private void getListView() {  
        int childCount = getChildCount();

        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);

                if (childView instanceof ListView) {
                    mListView = (ListView) childView;

                    // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                    mListView.setOnScrollListener(this);
                }
            }
        }  
    }

    /**
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {  
        final int action = event.getAction();  
  
        switch (action) {  
            case MotionEvent.ACTION_DOWN:
                // 按下
                mDownY = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:
                // 移动
                mMoveY = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_UP:  
                // 抬起
                if (canLoad()) {  
                    loadData();  
                }

                break;  
            default:  
                break;  
        }  
  
        return super.dispatchTouchEvent(event);  
    }  
  
    // 是否可以加载更多, 条件是到了最底部, ListView不在加载中, 且为上拉操作.
    private boolean canLoad() {
        if (mListView == null) {
            LogUtils.showLog("不能加载");
            return false;
        }

        return !mIsNoMoreData && isBottom() && !mIsLoading && isPullUp() && mListView.getAdapter().getCount() > mCanLoadCount && !isRefreshing();
    }

    /**
     * 设置列表的数目达到多少条才能上拉加载更多
     * @param canLoadCount
     */
    public void setCanLoadCount(int canLoadCount) {
        this.mCanLoadCount = canLoadCount;
    }
  
    // 判断是否到了最底部
    private boolean isBottom() {
        if (mListView != null && mListView.getAdapter() != null) {  
            return mListView.getLastVisiblePosition() >= (mListView.getAdapter().getCount() - mStartLoadCount);
        }

        return false;  
    }

    /**
     * 设置上拉到列表倒数第几条的时候开始加载更多
     * @param startLoadCount
     */
    public void setStartLoadCount(int startLoadCount) {
        this.mStartLoadCount = startLoadCount;
    }
  
    // 是否是上拉操作
    private boolean isPullUp() {  
        return (mDownY - mMoveY) >= mTouchSlop;
    }  
  
    // 如果到了最底部,而且是上拉操作.那么执行onLoad方法
    private void loadData() {  
        if (mOnLoadListener != null) {  
            // 设置状态  
            setLoading(true);

            mOnLoadListener.onLoad();
        }  
    }  

    private void setLoading(boolean loading) {
        mIsLoading = loading;

        if (mIsLoading) {
            mHasMoreDataFooterView = LayoutInflater.from(mContext).inflate(R.layout.listview_footer, null, false);

//            if (mListView.getFooterViewsCount() <= 0) {
//                mListView.addFooterView(mHasMoreDataFooterView);
//            }
//
//            mHasMoreDataFooterView.setVisibility(View.VISIBLE);

            mListView.removeFooterView(mNoMoreDataFooterView);
            mListView.addFooterView(mHasMoreDataFooterView, null, false);  //禁用FooterView的点击事件
        } else {
            if (mListView == null || mListView.getFooterViewsCount() <= 0 || mListView.getAdapter() == null || mHasMoreDataFooterView == null) {
                return;
            }

//            mHasMoreDataFooterView.setVisibility(View.GONE);

            mListView.removeFooterView(mNoMoreDataFooterView);
            mListView.removeFooterView(mHasMoreDataFooterView);

//        	ObjectAnimator animation = ObjectAnimator.ofFloat(mHasMoreDataFooterView, "scaleY", 1, 0);
//			animation.setDuration(100);
//			animation.start();
//			animation.addListener(new Animator.AnimatorListener() {
//				public void onAnimationStart(Animator arg0) {
//
//				}
//
//				public void onAnimationRepeat(Animator arg0) {
//
//				}
//
//				public void onAnimationEnd(Animator arg0) {
//					mListView.removeFooterView(mHasMoreDataFooterView);
//
//				}
//
//				public void onAnimationCancel(Animator arg0) {
//
//				}
//			});
            
            mDownY = 0;
            mMoveY = 0;
        }  
    }  
  
    // 设置上拉加载的监听
    public void setOnLoadListener(OnLoadListener loadListener) {  
        mOnLoadListener = loadListener;  
    }

    // 滑动状态改变的代理
    @Override  
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mOnScrollStateChangeListener != null) {
            mOnScrollStateChangeListener.onScrollStateChanged(view, scrollState);
        }
    }  
  
    @Override  
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        // 滚动时到了最底部也可以加载更多  
        if (canLoad()) {  
            loadData();  
        }
    }  
  
    // 加载更多的监听器
    public interface OnLoadListener {
        void onLoad();
    }

    public interface OnScrollListener {
        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    public interface OnScrollStateChangeListener {
        void onScrollStateChanged(AbsListView listView, int state);
    }

    public interface OnPullRefreshListener {
        void onRefresh();
    }

    public void onRefreshComplete() {
        setLoading(false);
        setRefreshing(false);

//        if (isPullUp()) {
//            setLoading(false);
//        } else {
//            setRefreshing(false);
//        }
    }

    // 设置没有更多数据，从此以后没有加载更多
    public void setNoMoreData() {
        try {
            mIsNoMoreData = true;

            mListView.removeFooterView(mHasMoreDataFooterView);
            mListView.addFooterView(mNoMoreDataFooterView, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setOnScrollStateChangeListener(OnScrollStateChangeListener onScrollStateChangeListener) {
        this.mOnScrollStateChangeListener = onScrollStateChangeListener;
    }

    public void setOnPullRefreshListener(OnPullRefreshListener onPullRefreshListener) {
        mOnPullRefreshListener = onPullRefreshListener;
    }
}
