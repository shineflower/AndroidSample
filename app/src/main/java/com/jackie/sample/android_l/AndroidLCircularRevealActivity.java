package com.jackie.sample.android_l;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;

/**
 * Android L新增的动画效果
 * 图形揭示
 */
public class AndroidLCircularRevealActivity extends Activity {
    private Context mContext;
    private View mCircularRevealView1, mCircularRevealView2;
    private Animator mAnimator1;
    private Animator mAnimator2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_l_circular_reveal);
        mContext = this;

        initView();
    }

    public void initView(){
        mCircularRevealView1 = findViewById(R.id.circular_reveal_view_1);
        mCircularRevealView2 = findViewById(R.id.circular_reveal_view_2);

        //view加载完成时回调
        mCircularRevealView1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mCircularRevealView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimator1 = ViewAnimationUtils.createCircularReveal(
                        mCircularRevealView1,
                        DensityUtils.dip2px(mContext, 100) / 2,
                        DensityUtils.dip2px(mContext, 100) / 2,
                        DensityUtils.dip2px(mContext, 100),
                        0);
                mAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
                mAnimator1.setDuration(2000);
                mAnimator1.start();
            }
        });

        mCircularRevealView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimator2 = ViewAnimationUtils.createCircularReveal(
                        mCircularRevealView2,
                        0,
                        0,
                        0,
                        (float) Math.hypot(DensityUtils.dip2px(mContext,100), DensityUtils.dip2px(mContext,100)));
                mAnimator2.setInterpolator(new AccelerateInterpolator());
                mAnimator2.setDuration(2000);
                mAnimator2.start();
            }
        });

        super.onWindowFocusChanged(hasFocus);
    }
}
