package com.jackie.sample.custom_view;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import com.jackie.sample.utils.ScreenUtils;

import static android.R.attr.level;

/**
 * Created by Jackie on 2017/6/15.
 * 通用PopupWindow，建造者模式
 */

public class CommonPopupWindow extends PopupWindow {
    private PopupWindowController mPopupWindowController;

    @Override
    public int getWidth() {
        return mPopupWindowController.mPopupWindowView.getMeasuredWidth();
    }

    @Override
    public int getHeight() {
        return mPopupWindowController.mPopupWindowView.getMeasuredHeight();
    }

    public interface OnPopupWindowListener {
        void getView(View view, int layoutResId);
    }

    private CommonPopupWindow(Context context) {
        mPopupWindowController = new PopupWindowController(context, this);
    }

    @Override
    public void dismiss() {
        super.dismiss();

        mPopupWindowController.setBackgroundAlpha(1.0f);
    }

    public static class Builder {
        private PopupWindowController.PopupWindowParams popupWindowParams;
        private OnPopupWindowListener onPopupWindowListener;

        public Builder(Context context) {
            popupWindowParams = new PopupWindowController.PopupWindowParams(context);
        }

        /**
         * @param layoutResId
         * @return Builder
         */
        public Builder setView(int layoutResId) {
            popupWindowParams.view = null;
            popupWindowParams.layoutResId = layoutResId;
            return this;
        }

        /**
          * @param view
         * @return Builder
         */
        public Builder setView(View view) {
            popupWindowParams.view = view;
            popupWindowParams.layoutResId = 0;
            return this;
        }

        public Builder setOnPopupWindowListener(OnPopupWindowListener onPopupWindowListener) {
            this.onPopupWindowListener = onPopupWindowListener;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            popupWindowParams.width = width;
            popupWindowParams.height = height;
            return this;
        }

        /**
         * 设置背景灰色程度
         * @param alpha 0.0f-1.0f
         * @return Builder
         */
        public Builder setBackGroundAlpha(float alpha) {
            popupWindowParams.isShowBackground = true;
            popupWindowParams.alpha = level;
            return this;
        }

        /**
         * 设置动画
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            popupWindowParams.isShowAnimation = true;
            popupWindowParams.animationStyle = animationStyle;
            return this;
        }

        /**
         * 是否可点击Outside消失
         * @param isTouchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean isTouchable) {
            popupWindowParams.isTouchable = isTouchable;
            return this;
        }

        public CommonPopupWindow create() {
            CommonPopupWindow commonPopupWindow = new CommonPopupWindow(popupWindowParams.context);
            popupWindowParams.apply(commonPopupWindow.mPopupWindowController);

            if (onPopupWindowListener != null && popupWindowParams.layoutResId != 0) {
                onPopupWindowListener.getView(commonPopupWindow.mPopupWindowController.mPopupWindowView, popupWindowParams.layoutResId);
            }

            ScreenUtils.measureWidthAndHeight(commonPopupWindow.mPopupWindowController.mPopupWindowView);

            return commonPopupWindow;
        }
    }
}
