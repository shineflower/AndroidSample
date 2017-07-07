package com.jackie.sample.contact.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/7/6.
 */

public class SideBar extends View {
    private Context mContext;

    private Paint mPaint;

    private int mWidth;
    private int mHeight;
    private int mSingleHeight;

    private String mSortLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";

    private final int TOTAL_MARGIN = 160;
    private final int TOP_MARGIN = 80;

    private OnIndexChangedListener mOnIndexChangedListener;

    public interface OnIndexChangedListener {
        void onIndexChanged(String sortLetter);
    }

    public void setOnIndexChangedListener(OnIndexChangedListener onIndexChangedListener) {
        this.mOnIndexChangedListener = onIndexChangedListener;
    }

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;

        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setTextSize(35);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //导航栏居中显示，上下各有80dp的边距
        mWidth = w;
        mHeight = (h - DensityUtils.dp2px(mContext, TOTAL_MARGIN));

        mSingleHeight = mHeight / mSortLetters.length();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mSortLetters.length(); i++) {
            String text = mSortLetters.substring(i, i + 1);
            float x = (mWidth - mPaint.measureText(text)) / 2;
            canvas.drawText(text, x, mSingleHeight * (i + 1) + DensityUtils.dp2px(mContext, TOP_MARGIN), mPaint);
        }
    }

    public void setSortLetters(String sortLetters) {
        this.mSortLetters = sortLetters;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                mPaint.setColor(Color.BLACK);

                invalidate();
//                break;
            case MotionEvent.ACTION_MOVE:
                //滑动 event.getY 得到在父View中的Y坐标，通过和总高度的比例再乘以字符个数总长度得到按下的位置
                int position = (int) ((event.getY() - getTop() - DensityUtils.dp2px(mContext, 80)) / mSingleHeight);

                if (position >= 0 && position < mSortLetters.length()) {
                    ((IndexBar) getParent()).setDrawData(event.getY(), String.valueOf(mSortLetters.toCharArray()[position]), position);

                    if (mOnIndexChangedListener != null) {
                        mOnIndexChangedListener.onIndexChanged(mSortLetters.substring(position, position + 1));
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                //抬起
                ((IndexBar) getParent()).setTagStatus(false);
                mPaint.setColor(Color.GRAY);
                invalidate();
                break;
        }

        return true;
    }
}
