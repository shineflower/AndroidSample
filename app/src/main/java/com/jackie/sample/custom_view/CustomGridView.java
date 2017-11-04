package com.jackie.sample.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

//GridView铺开，用于ListView ScrollView 中嵌套GridView，GridView的高度没有展开时用到
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
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
