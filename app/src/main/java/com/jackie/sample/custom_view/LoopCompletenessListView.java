package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import com.jackie.sample.R;
import com.jackie.sample.utils.ScreenUtils;

/**
 * Created by Jackie on 2017/5/19.
 * 1、循环滑动
 * 2、每次滑动结束，保持每个Item的完整
 */

public class LoopCompletenessListView extends ListView implements AbsListView.OnScrollListener {
    //每个屏幕显示多少个Item
    private int mItemCountInOneScreen;
    //每个Item的高度
    private int mItemHeight;
    //记录第一个显示的Item
    private int mFirstVisibleItem;

    public LoopCompletenessListView(Context context) {
        this(context, null);
    }

    public LoopCompletenessListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopCompletenessListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LoopCompletenessListView, defStyleAttr, 0);

        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.LoopCompletenessListView_loop_item_count:
                    mItemCountInOneScreen = ta.getInt(attr, 6);
                    break;
            }
        }

        ta.recycle();

        //计算每个Item高度
        mItemHeight = (ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context)) / mItemCountInOneScreen;
        //设置滚动监听
        setOnScrollListener(this);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_UP:
//                checkForReset();
//                return true;
//        }
//        return super.onTouchEvent(ev);
//    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滚动结束
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            checkForReset();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //滚动过程中不断记录当前显示的第一个Item
        mFirstVisibleItem = firstVisibleItem;
    }

    private void checkForReset() {
        //获取第一个Item的top
        int top = getChildAt(0).getTop();
        if (top == 0) {
            return;
        }

        //绝对值不为0时，如果绝对值大于mItemHeight的一半，则收缩，否则即显示下一个Item
        if (Math.abs(top) > mItemHeight / 2) {
            this.setSelection(mFirstVisibleItem + 1);

            // this.scrollTo(x, y)
            // smoothScrollToPosition(mFirstVisibleItem - 1);
            // scrollBy(0, mItemHeight- Math.abs(top));
            // smoothScrollBy(mItemHeight- Math.abs(top), 200);
        } else {
            // 绝对值不为0时，如果绝对值小于于mItemHeight的一半，则展开，显示当前完整的Item
            this.setSelection(mFirstVisibleItem);

            // this.scrollTo(x, y)
            // smoothScrollBy( -Math.abs(top), 200);
            // smoothScrollByOffset(offset);
            // scrollBy(0, -Math.abs(top));
            // smoothScrollToPosition(mFirstVisibleItem);
        }

        // smoothScrollToPosition(mFirstVisibleItem);
    }

    /**
     * 对外公布每个Item的高度
     * @return
     */
    public int getItemHeight() {
        return mItemHeight;
    }
}
