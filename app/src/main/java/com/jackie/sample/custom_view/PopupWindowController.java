package com.jackie.sample.custom_view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by Jackie on 2017/6/15.
 */

public class PopupWindowController {
    private Context mContext;
    private PopupWindow mPopupWindow;

    private int mLayoutResId;

    private View mView;
    public View mPopupWindowView;

    private Window mWindow;

    public PopupWindowController(Context context, PopupWindow popupWindow) {
        this.mContext = context;
        this.mPopupWindow = popupWindow;
    }

    public void setView(int layoutResId) {
        mView = null;
        this.mLayoutResId = layoutResId;
        initView();
    }

    public void setView(View view) {
        mView = view;
        this.mLayoutResId = 0;
        initView();
    }

    private void initView() {
        if (mLayoutResId != 0) {
            mPopupWindowView = LayoutInflater.from(mContext).inflate(mLayoutResId, null);
        } else if (mView != null) {
            mPopupWindowView = mView;
        }

        mPopupWindow.setContentView(mPopupWindowView);
    }

    /**
     * 设置宽度和高度
     * @param width  宽
     * @param height 高
     */
    private void setWidthAndHeight(int width, int height) {
        if (width == 0 || height == 0) {
            //如果没有设置宽高，默认是WARP_CONTENT
            mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            mPopupWindow.setWidth(width);
            mPopupWindow.setHeight(height);
        }
    }

    /**
     * 设置背景透明度
     * @param alpha 透明度
     */
    public void setBackgroundAlpha(float alpha) {
        mWindow = ((Activity) mContext).getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = alpha;
        mWindow.setAttributes(params);
    }

    /**
     * 设置动画
     * @param animationStyle 动画
     */
    private void setAnimationStyle(int animationStyle) {
        mPopupWindow.setAnimationStyle(animationStyle);
    }

    /**
     * 设置outside是否可点击
     * @param isTouchable 是否可点击
     */
    private void setOutsideTouchable(boolean isTouchable) {
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));  //设置透明背景
        mPopupWindow.setOutsideTouchable(isTouchable);  //设置outside可点击
        mPopupWindow.setFocusable(isTouchable);
    }

    public static class PopupWindowParams {
        public Context context;

        public int layoutResId;
        public View view;

        public int width, height;

        public boolean isShowBackground, isShowAnimation;
        public float alpha;
        public int animationStyle;
        public boolean isTouchable = true;

        public PopupWindowParams(Context context) {
            this.context = context;
        }

        public void apply(PopupWindowController popupWindowController) {
            if (layoutResId != 0) {
                popupWindowController.setView(layoutResId);
            } else if (view != null) {
                popupWindowController.setView(view);
            } else {
                throw new IllegalArgumentException("PopupView ContentView is null");
            }

            popupWindowController.setWidthAndHeight(width, height);
            popupWindowController.setOutsideTouchable(isTouchable);

            if (isShowBackground) {
                //设置背景
                popupWindowController.setBackgroundAlpha(alpha);
            }

            if (isShowAnimation) {
                popupWindowController.setAnimationStyle(animationStyle);
            }
        }
    }
}
