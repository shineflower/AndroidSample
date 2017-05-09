package com.jackie.sample.animator;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.jackie.sample.R;

/**
 * 自定义的animation动画，模拟电视的关机的动画。
 */
public class TurnOffTvActivity extends Activity {
    private ImageView mImageView;
    private int mWidth;
    private int mHeight;
    private ViewTreeObserver mViewTreeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_off_tv);
        initView();
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.image_view);

        mViewTreeObserver = mImageView.getViewTreeObserver();
        mViewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mHeight = mImageView.getMeasuredHeight();
                mWidth = mImageView.getMeasuredWidth();
                return true;
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnOffTvAnimation turnOffTvAnimation = new TurnOffTvAnimation(0, 0);
                turnOffTvAnimation.setDuration(1000);

//                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
//                alphaAnimation.setDuration(1000);

                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(turnOffTvAnimation);
//                animationSet.addAnimation(alphaAnimation);
                //动画结束之后保留到结束的状态 , 但是animation 的动画并不是真正的将view 移动或者是旋转了，
                // 所以设置了该值之后，照样可以点击这个view，一样播放动画
                animationSet.setFillAfter(true);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        mImageView.setVisibility(View.GONE);//电视的一个关闭效果模仿，缩小消失掉
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                mImageView.startAnimation(animationSet);
            }
        });
    }

    /**
     * 电视的一个关闭动画的模仿
     */
    private class TurnOffTvAnimation extends RotateAnimation {
        public TurnOffTvAnimation(float fromDegrees, float toDegrees) {
            super(fromDegrees, toDegrees);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            final Matrix matrix = t.getMatrix();
            matrix.preScale(1, 1 - interpolatedTime, mWidth / 2, mHeight / 2);
        }
    }
}
