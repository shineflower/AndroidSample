package com.jackie.sample.view_pager_anim_transfer.guide;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class RotateDownPageTransformer implements ViewPager.PageTransformer {
    private static final float MAX_ROTATE = 20f;
    private float mRotation;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            ViewHelper.setRotation(view, 0);
        /**
         * A页切换到B页，A页的position 0.0 ~ -1.0, B页的position 1.0 ~ 0.0
         */
        } else if (position <= 0) { // [-1,0] A页
            //需求：0° ~ -20°
            mRotation = position * MAX_ROTATE;
            ViewHelper.setPivotX(view, pageWidth / 2);
            ViewHelper.setPivotY(view, view.getMeasuredHeight());
            ViewHelper.setRotation(view, mRotation);
        } else if (position <= 1) { // (0,1] B页
            //需求：20° ~ 0°
            mRotation = position * MAX_ROTATE;
            ViewHelper.setPivotX(view, pageWidth / 2);
            ViewHelper.setPivotY(view, view.getMeasuredHeight());
            ViewHelper.setRotation(view, mRotation);
        } else { // (1,+Infinity]
            ViewHelper.setRotation(view, 0);
        }
    }
}