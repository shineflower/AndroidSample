package com.jackie.sample.jd_tmall_refresh.tmall;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 仿天猫下拉刷新view
 */
public class TmallRefreshLayout extends PtrFrameLayout {

    /**
     * HeaderView
     */
    private TmallRefreshHeader mHeaderView;

    public TmallRefreshLayout(Context context) {
        this(context, null);
    }

    public TmallRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TmallRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView();
    }


    /**
     * 初始化view
     */
    private void initView() {
        mHeaderView = new TmallRefreshHeader(getContext());
        setHeaderView(mHeaderView);
        addPtrUIHandler(mHeaderView);
    }
}
