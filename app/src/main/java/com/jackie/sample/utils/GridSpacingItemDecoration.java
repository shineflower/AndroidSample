package com.jackie.sample.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置RecyclerView GridLayoutManager or StaggeredGridLayoutManager Spacing
 * Created by Jackie on 2017/12/14
 *
 * GridLayoutManager GridLayoutManager = new GridLayoutManager(this, COLUMN, GridLayoutManager.VERTICAL, false);
   mRecyclerView.setLayoutManager(GridLayoutManager);
   mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(COLUMN, getResources().getDimensionPixelSize(R.dimen.padding_middle), true));
   mRecyclerView.setHasFixedSize(true);
   mRecyclerAdapter = new RecyclerAdapter(this);
   mRecyclerAdapter.setItemList(DataMock.mockItemBean());
   mRecyclerView.setAdapter(mRecyclerAdapter);
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpanCount;
    private int mSpacing;
    private boolean mIncludeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.mSpanCount = spanCount;
        this.mSpacing = spacing;
        this.mIncludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % mSpanCount; // item column

        if (mIncludeEdge) {
            outRect.left = mSpacing - column * mSpacing / mSpanCount; // mSpacing - column * ((1f / mSpanCount) * mSpacing)
            outRect.right = (column + 1) * mSpacing / mSpanCount; // (column + 1) * ((1f / mSpanCount) * mSpacing)

            if (position < mSpanCount) { // top edge
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing;   // item bottom
        } else {
            outRect.left = column * mSpacing / mSpanCount; // column * ((1f / mSpanCount) * mSpacing)
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount; // mSpacing - (column + 1) * ((1f / mSpanCount) * mSpacing)
            if (position >= mSpanCount) {
                outRect.top = mSpacing; // item top
            }
        }
    }
}