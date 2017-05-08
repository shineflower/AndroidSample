package com.jackie.sample.jd_tmall_refresh.jd;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 *仿京东下拉刷新
 */
public class JdRefreshLayout extends PtrFrameLayout {
    /**
     * HeaderView
     */
    private JdRefreshHeader mHeaderView;

    public JdRefreshLayout(Context context) {
        this(context, null);
    }

    public JdRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JdRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView();
    }


    /**
     * 初始化HeaderView
     */
    private void initView() {
        mHeaderView = new JdRefreshHeader(getContext());
        setHeaderView(mHeaderView);
        addPtrUIHandler(mHeaderView);
    }
}
