package com.jackie.sample.custom_view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class SquareRelativeLayout extends RelativeLayout {
    public SquareRelativeLayout(Context context) {
        super(context);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec,heightMeasureSpec);
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
//
//        if(isInEditMode()){
//            if (abs(heightMeasureSpec) < abs(widthMeasureSpec)) {
//                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
//            }
//
//            else {
//                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
//            }
//        }
//
        init();
    }

    private void init() {
        this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                if (getWidth() != getHeight()) {
//                    int squareSize = Math.min(getWidth(), getHeight());

                    ViewGroup.LayoutParams lp = getLayoutParams();
                    lp.width = getHeight();
                    lp.height = getHeight();
                    requestLayout();
                    return false;
                }
                return true;
            }
        });
    }

}
