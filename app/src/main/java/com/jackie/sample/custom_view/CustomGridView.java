package com.jackie.sample.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CustomGridView extends GridView {
	public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public CustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public CustomGridView(Context context) {
		super(context);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//最多一行显示2个
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
