package com.jackie.sample.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * Created by Jackie on 2016/2/22.
 */
public class IndexScroller {
    private float mIndexBarWidth;
    private float mIndexBarMargin;
    private float mPreviewPadding;
    private float mDensity;
    private float mScaledDensity;
    private float mAlphaRate;
    private int mState = STATE_HIDDEN;
    private int mListViewWidth;
    private int mListViewHeight;
    private int mCurrentSection = -1;
    private boolean mIsIndexing = false;
    private ListView mListView = null;
    private SectionIndexer mIndexer = null;
    private String[] mSections = null;
    private RectF mIndexBarRect;

    private static final int STATE_HIDDEN = 0;
    private static final int STATE_SHOWING = 1;
    private static final int STATE_SHOWN = 2;
    private static final int STATE_HIDING = 3;

    public IndexScroller(Context context, ListView listView) {
        mDensity = context.getResources().getDisplayMetrics().density;
        mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        mListView = listView;
        setAdapter(mListView.getAdapter());

        mIndexBarWidth = 20 * mDensity;
        mIndexBarMargin = 10 * mDensity;
        mPreviewPadding = 5 * mDensity;
    }

    public void draw(Canvas canvas) {
        if (mState == STATE_HIDDEN) {
            return;
        }

        // mAlphaRate determines the rate of opacity
        Paint indexBarPaint = new Paint();
        indexBarPaint.setColor(Color.BLACK);
        indexBarPaint.setAlpha((int) (64 * mAlphaRate));
        indexBarPaint.setAntiAlias(true);
        canvas.drawRoundRect(mIndexBarRect, 5 * mDensity, 5 * mDensity, indexBarPaint);

        Paint indexTextPaint = new Paint();
        indexTextPaint.setColor(Color.WHITE);
        indexTextPaint.setAlpha((int) (255 * mAlphaRate));
        indexTextPaint.setAntiAlias(true);
        indexTextPaint.setTextSize(12 * mScaledDensity);

        float sectionHeight = (mIndexBarRect.height() - 2 * mIndexBarMargin) / mSections.length;
        float paddingTop = (sectionHeight - (indexTextPaint.descent() - indexTextPaint.ascent())) / 2;
        for (int i = 0; i < mSections.length; i++) {
            float paddingLeft = (mIndexBarWidth - indexTextPaint.measureText(mSections[i])) / 2;
            canvas.drawText(mSections[i], mIndexBarRect.left + paddingLeft
                    , mIndexBarRect.top + mIndexBarMargin + sectionHeight * i + paddingTop - indexTextPaint.ascent(), indexTextPaint);
        }

        if (mSections != null && mSections.length > 0) {
            // Preview is shown when mCurrentSection is set
            if (mCurrentSection >= 0) {
                Paint previewPaint = new Paint();
                previewPaint.setColor(Color.BLACK);
                previewPaint.setAlpha(96);
                previewPaint.setAntiAlias(true);
                previewPaint.setShadowLayer(3, 0, 0, Color.argb(64, 0, 0, 0));

                Paint previewTextPaint = new Paint();
                previewTextPaint.setColor(Color.WHITE);
                previewTextPaint.setAntiAlias(true);
                previewTextPaint.setTextSize(50 * mScaledDensity);

                float previewTextWidth = previewTextPaint.measureText(mSections[mCurrentSection]);
                float previewSize = 2 * mPreviewPadding + previewTextPaint.descent() - previewTextPaint.ascent();
                RectF previewRect = new RectF((mListViewWidth - previewSize) / 2
                        , (mListViewHeight - previewSize) / 2
                        , (mListViewWidth - previewSize) / 2 + previewSize
                        , (mListViewHeight - previewSize) / 2 + previewSize);

                canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity, previewPaint);
                canvas.drawText(mSections[mCurrentSection], previewRect.left + (previewSize - previewTextWidth) / 2 - 1
                        , previewRect.top + mPreviewPadding - previewTextPaint.ascent() + 1, previewTextPaint);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // If down event occurs inside index bar region, start indexing
                if (mState != STATE_HIDDEN && contains(ev.getX(), ev.getY())) {
                    setState(STATE_SHOWN);

                    // It demonstrates that the motion event started from index bar
                    mIsIndexing = true;
                    // Determine which section the point is in, and move the list to that section
                    mCurrentSection = getSectionByPoint(ev.getY());
                    mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsIndexing) {
                    // If this event moves inside index bar
                    if (contains(ev.getX(), ev.getY())) {
                        // Determine which section the point is in, and move the list to that section
                        mCurrentSection = getSectionByPoint(ev.getY());
                        mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsIndexing) {
                    mIsIndexing = false;
                    mCurrentSection = -1;
                }
                if (mState == STATE_SHOWN) {
                    setState(STATE_HIDING);
                }
                break;
        }
        return false;
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mListViewWidth = w;
        mListViewHeight = h;
        mIndexBarRect = new RectF(w - mIndexBarMargin - mIndexBarWidth
                , mIndexBarMargin
                , w - mIndexBarMargin
                , h - mIndexBarMargin);
    }

    public void show() {
        if (mState == STATE_HIDDEN) {
            setState(STATE_SHOWING);
        }
        else if (mState == STATE_HIDING) {
            setState(STATE_HIDING);
        }
    }

    public void hide() {
        if (mState == STATE_SHOWN) {
            setState(STATE_HIDING);
        }
    }

    public void setAdapter(Adapter adapter) {
        if (adapter instanceof SectionIndexer) {
            mIndexer = (SectionIndexer) adapter;
            mSections = (String[]) mIndexer.getSections();
        }
    }

    private void setState(int state) {
        if (state < STATE_HIDDEN || state > STATE_HIDING) {
            return;
        }

        mState = state;
        switch (mState) {
            case STATE_HIDDEN:
                // Cancel any fade effect
                mHandler.removeMessages(0);
                break;
            case STATE_SHOWING:
                // Start to fade in
                mAlphaRate = 0;
                fade(0);
                break;
            case STATE_SHOWN:
                // Cancel any fade effect
                mHandler.removeMessages(0);
                break;
            case STATE_HIDING:
                // Start to fade out after three seconds
                mAlphaRate = 1;
                fade(3000);
                break;
        }
    }

    public boolean contains(float x, float y) {
        // Determine if the point is in index bar region, which includes the right margin of the bar
        return (x >= mIndexBarRect.left && y >= mIndexBarRect.top && y <= mIndexBarRect.top + mIndexBarRect.height());
    }

    private int getSectionByPoint(float y) {
        if (mSections == null || mSections.length == 0) {
            return 0;
        }
        if (y < mIndexBarRect.top + mIndexBarMargin) {
            return 0;
        }
        if (y >= mIndexBarRect.top + mIndexBarRect.height() - mIndexBarMargin) {
            return mSections.length - 1;
        }
        return (int) ((y - mIndexBarRect.top - mIndexBarMargin) / ((mIndexBarRect.height() - 2 * mIndexBarMargin) / mSections.length));
    }

    private void fade(long delay) {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageAtTime(0, SystemClock.uptimeMillis() + delay);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (mState) {
                case STATE_SHOWING:
                    // Fade in effect
                    mAlphaRate += (1 - mAlphaRate) * 0.2;
                    if (mAlphaRate > 0.9) {
                        mAlphaRate = 1;
                        setState(STATE_SHOWN);
                    }

                    mListView.invalidate();
                    fade(10);
                    break;
                case STATE_SHOWN:
                    // If no action, hide automatically
                    setState(STATE_HIDING);
                    break;
                case STATE_HIDING:
                    // Fade out effect
                    mAlphaRate -= mAlphaRate * 0.2;
                    if (mAlphaRate < 0.1) {
                        mAlphaRate = 0;
                        setState(STATE_HIDDEN);
                    }

                    mListView.invalidate();
                    fade(10);
                    break;
            }
        }
    };
}
