package com.jackie.sample.jd_tmall_refresh.tmall;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jackie.sample.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * 下拉刷新HeaderView
 */
public class TmallRefreshHeader extends FrameLayout implements PtrUIHandler {

    /**
     * 提醒文本
     */
    private TextView mTvRemind;


    /**
     * 状态识别
     */
    private int mState;

    /**
     * 重置
     * 准备刷新
     * 开始刷新
     * 结束刷新
     */
    public static final int STATE_RESET = -1;
    public static final int STATE_PREPARE = 0;
    public static final int STATE_BEGIN = 1;
    public static final int STATE_FINISH = 2;


    public TmallRefreshHeader(Context context) {
        this(context, null);
    }

    public TmallRefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TmallRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tmall_refresh_header_view, this, false);
        mTvRemind = (TextView) view.findViewById(R.id.tv_remind);
        SimpleDraweeView sdv = (SimpleDraweeView) view.findViewById(R.id.tm_logo);
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                //设置uri,加载本地的gif资源
                .setUri(Uri.parse("res://" + getContext().getPackageName() + "/" + R.drawable.tm_mui_bike))//设置uri
                .build();
        sdv.setController(draweeController);
        addView(view);
    }


    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mState = STATE_RESET;
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mState = STATE_PREPARE;
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mState = STATE_BEGIN;
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mState = STATE_FINISH;
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        //处理提醒字体
        switch (mState) {
            case STATE_PREPARE:
                if (ptrIndicator.getCurrentPercent() < 1) {
                    mTvRemind.setText("下拉刷新");
                } else {
                    mTvRemind.setText("松开立即刷新");
                }
                break;
            case STATE_BEGIN:
                mTvRemind.setText("正在刷新...");
                break;
            case STATE_FINISH:
                mTvRemind.setText("加载完成...");
                break;
        }
    }
}
