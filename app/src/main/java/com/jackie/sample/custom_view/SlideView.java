package com.jackie.sample.custom_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/5/23.
 * 仿微信对话列表滑动删除功能
 */

public class SlideView extends LinearLayout {
    private Context mContext;
    private Scroller mScroller;
    private LinearLayout mViewContent;
    private RelativeLayout mDeleteHolder;

    private int mHolderWidth = 120;

    private int mLastX = 0;
    private int mLastY = 0;
    private static final int TAN = 2;

    private OnSlideListener mOnSlideListener;
    public interface OnSlideListener {
        int SLIDE_STATUS_OFF = 0;
        int SLIDE_STATUS_START_SCROLL = 1;
        int SLIDE_STATUS_ON = 2;

        /**
         * @param view current SlideView
         * @param status SLIDE_STATUS_ON or SLIDE_STATUS_OFF
         */
        void onSlide(View view, int status);
    }

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        mContext = getContext();
        mScroller = new Scroller(mContext);

        setOrientation(LinearLayout.HORIZONTAL);
        /**
         * <merge />只可以作为XML Layout的根节点。
         * 当需要扩充的XML Layout本身是由merge作为根节点的话，
         * 需要将被导入的XML Layout置于ViewGroup中，同时需要设置attachToRoot为true。（更多说明请参见inflate()文档）
         */
        View.inflate(mContext, R.layout.slide_view_merge, this);
        mViewContent = (LinearLayout) findViewById(R.id.view_content);
        mDeleteHolder = (RelativeLayout) findViewById(R.id.delete_holder);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDeleteHolder.getLayoutParams();
        mHolderWidth = params.width;
    }

    public void setContentView(View view) {
        mViewContent.addView(view);
    }

    public void setButtonText(CharSequence text) {
        ((TextView)findViewById(R.id.delete_text)).setText(text);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.mOnSlideListener = onSlideListener;
    }

    public void shrink() {
        if (getScrollX() != 0) {
            this.smoothScrollTo(0, 0);
        }
    }

    public void onRequireTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(this, OnSlideListener.SLIDE_STATUS_START_SCROLL);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;

                if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                    break;
                }

                int moveScrollX = scrollX - deltaX;
                if (deltaX != 0) {
                    if (moveScrollX < 0) {
                        moveScrollX = 0;
                    } else if (moveScrollX > mHolderWidth) {
                        moveScrollX = mHolderWidth;
                    }

                    this.scrollTo(moveScrollX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                int upScrollX = 0;

                if (scrollX - mHolderWidth * 0.75 > 0) {
                    upScrollX = mHolderWidth;
                }

                this.smoothScrollTo(upScrollX, 0);

                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(this,
                            upScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF : OnSlideListener.SLIDE_STATUS_ON);
                }
                break;
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
    }

    private void smoothScrollTo(int destX, int destY) {
        //缓慢滚动到指定位置
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;
        mScroller.startScroll(scrollX, 0, deltaX, 0, Math.abs(deltaX) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
