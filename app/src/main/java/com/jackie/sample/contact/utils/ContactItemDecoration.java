package com.jackie.sample.contact.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.jackie.sample.bean.ContactBean;
import com.jackie.sample.utils.DensityUtils;

import java.util.List;

/**
 * Created by Jackie on 2017/7/6.
 */

public class ContactItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private Paint mPaint;

    private List<ContactBean> mContactList;
    private String mSortLetters;

    private Rect mRect = new Rect();

    private static final int DIVIDER_HEIGHT = 80;

    public ContactItemDecoration(Context context) {
        this.mContext = context;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<ContactBean> contactList, String sortLetters) {
        this.mContactList = contactList;
        this.mSortLetters = sortLetters;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }

        canvas.save();

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (mContactList == null || mContactList.size() == 0 || mContactList.size() <= position || position < 0) {
                continue;
            }

            if (position == 0) {
                //第一条数据有TitleBar
                drawTitleBar(canvas, parent, child, mContactList.get(position), mSortLetters.indexOf(mContactList.get(position).getFirstLetter()));
            } else if (position > 0) {
                if (TextUtils.isEmpty(mContactList.get(position).getFirstLetter())) {
                    continue;
                }

                //与上一条数据中firstLetter不同时，显示TitleBar
                if (!mContactList.get(position).getFirstLetter().equals(mContactList.get(position - 1).getFirstLetter())) {
                    drawTitleBar(canvas, parent, child, mContactList.get(position), mSortLetters.indexOf(mContactList.get(position).getFirstLetter()));
                }
            }
        }

        canvas.restore();
    }

    private void drawTitleBar(Canvas canvas, RecyclerView parent, View child, ContactBean contactBean, int position) {
        int left = 0;
        int right = parent.getWidth();
        //返回一个包含Decoration和Margin在内的Rect
        parent.getDecoratedBoundsWithMargins(child, mRect);
        int top = mRect.top;
        int bottom = mRect.top + Math.round(ViewCompat.getTranslationY(child)) + DIVIDER_HEIGHT;
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(left, top, right, bottom, mPaint);
        //根据位置不断变换Paint的颜色
        ColorUtils.setPaintColor(mPaint, position);
        mPaint.setTextSize(40);
        canvas.drawCircle(DensityUtils.dp2px(mContext, 42.5f), bottom - DIVIDER_HEIGHT / 2, 35, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawText(contactBean.getFirstLetter(), DensityUtils.dp2px(mContext, 42.5f), bottom - DIVIDER_HEIGHT / 3, mPaint);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        //用来绘制悬浮框
        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        if (mContactList == null || mContactList.size() == 0 || mContactList.size() <= position || position < 0) {
            return;
        }

        final int bottom = parent.getPaddingTop() + DIVIDER_HEIGHT;
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(parent.getLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + DIVIDER_HEIGHT, mPaint);
        ColorUtils.setPaintColor(mPaint, mSortLetters.indexOf(mContactList.get(position).getFirstLetter()));
        mPaint.setTextSize(40);
        canvas.drawCircle(DensityUtils.dp2px(mContext, 42.5f), bottom - DIVIDER_HEIGHT / 2, 35, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawText(mContactList.get(position).getFirstLetter(), DensityUtils.dp2px(mContext, 42.5f), bottom - DIVIDER_HEIGHT / 3, mPaint);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (mContactList == null || mContactList.size() == 0 || mContactList.size() <= position || position < 0) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }

        if (position == 0) {
            outRect.set(0, DIVIDER_HEIGHT, 0, 0);
        } else if (position > 0) {
            if (TextUtils.isEmpty(mContactList.get(position).getFirstLetter())) {
                return;
            }

            if (!mContactList.get(position).getFirstLetter().equals(mContactList.get(position - 1).getFirstLetter())) {
                outRect.set(0, DIVIDER_HEIGHT, 0, 0);
            }
        }
    }
}
