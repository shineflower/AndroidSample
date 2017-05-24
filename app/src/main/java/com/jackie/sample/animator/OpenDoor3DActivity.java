package com.jackie.sample.animator;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.jackie.sample.R;

/**
 * 三维的点击效果(开门一样的效果)
 */
public class OpenDoor3DActivity extends AppCompatActivity {
    private ImageView mImage;
    private int mWidth;
    private int mHeight;
    private ViewTreeObserver mViewTreeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3d_open_door);

        initView();
    }

    private void initView() {
        mImage = (ImageView) findViewById(R.id.image_view);

        mViewTreeObserver = mImage.getViewTreeObserver();
        mViewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mHeight = mImage.getMeasuredHeight();
                mWidth = mImage.getMeasuredWidth();
                return true;
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDoor3DAnimation openDoor3DAnimation = new OpenDoor3DAnimation(0, 0);
                openDoor3DAnimation.setDuration(1000);

//                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
//                alphaAnimation.setDuration(1000);

                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(openDoor3DAnimation);
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
//                        mImage.setVisibility(View.GONE);//电视的一个关闭效果模仿，缩小消失掉
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                mImage.startAnimation(animationSet);
            }
        });
    }

    /**
     * 三维的点击效果
     * 开门一样的效果
     *
     */
    private class OpenDoor3DAnimation extends  RotateAnimation {
        private Camera mCamera;

        public OpenDoor3DAnimation(float fromDegrees, float toDegrees) {
            super(fromDegrees, toDegrees);
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            //默认时长
            setDuration(2000);
            //动画结束之后保留状态，停留在最后的状态上
            setFillAfter(true);
            //差值器设置。
            setInterpolator(new BounceInterpolator());//弹跳的差值

            mCamera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            final Matrix matrix = t.getMatrix();
            matrix.preScale(1, 1 - interpolatedTime, mWidth / 2, mHeight / 2);

            mCamera.save();

            mCamera.rotateY(10*interpolatedTime);

            mCamera.getMatrix(matrix);

            mCamera.restore();

            matrix.preTranslate(mWidth / 2, mHeight / 2);
            matrix.postTranslate(-mWidth / 2, -mHeight / 2);
        }
    }
}
