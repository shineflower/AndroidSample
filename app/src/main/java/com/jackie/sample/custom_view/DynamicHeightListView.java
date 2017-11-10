package com.jackie.sample.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/10/24
 * 设置最大高度的ListView
 * 小于最大高度  wrap_content
 * 大于最大高度  300dp
 */
public class DynamicHeightListView extends ListView {
	private int mMaxHeight = DensityUtils.dp2px(getContext(), 300);

	public DynamicHeightListView(Context context) {
		super(context);
	}


	public DynamicHeightListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public DynamicHeightListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		setViewHeightBasedOnChildren();
	}

	public void setViewHeightBasedOnChildren() {
		ListAdapter listAdapter = this.getAdapter();

		if (listAdapter == null) {
			return;
		}

		int sumHeight = 0;
		int size = listAdapter.getCount();

		for (int i = 0; i < size; i++) {
			View view = listAdapter.getView(i, null, this);
			view.measure(0, 0);
			sumHeight += view.getMeasuredHeight();
		}

		if (sumHeight > mMaxHeight) {
			sumHeight = mMaxHeight;
		}

		android.view.ViewGroup.LayoutParams params = this.getLayoutParams();
		params.height = sumHeight;

		this.setLayoutParams(params);
	}

	public int getMaxHeight() {
		return mMaxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.mMaxHeight = maxHeight;
	}
}
