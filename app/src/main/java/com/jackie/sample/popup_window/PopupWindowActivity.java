package com.jackie.sample.popup_window;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.CommonPopupWindow;
import com.jackie.sample.utils.DensityUtils;
import com.jackie.sample.utils.ScreenUtils;

public class PopupWindowActivity extends AppCompatActivity implements CommonPopupWindow.OnPopupWindowListener {
    private CommonPopupWindow mCommonPopupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_popup_window);
    }

    //向下弹出
    public void showDownPopupWindow(View view) {
        if (mCommonPopupWindow != null && mCommonPopupWindow.isShowing()) {
            return;
        }

        mCommonPopupWindow = new CommonPopupWindow.Builder(this)
//                .setView(R.layout.popup_window_down)
                .setView(R.layout.popup_window_left_or_right)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimationDown)
                .setOnPopupWindowListener(this)
                .setOutsideTouchable(true)
                .create();

        mCommonPopupWindow.showAsDropDown(view);

        //得到button的左上角坐标
//        int[] positions = new int[2];
//        view.getLocationOnScreen(positions);
//        mCommonPopupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, positions[1] + view.getHeight());
    }

    //向右弹出
    public void showRightPopupWindow(View view) {
        if (mCommonPopupWindow != null && mCommonPopupWindow.isShowing()) {
            return;
        }

        mCommonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_window_left_or_right)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimationRight)
                .setOnPopupWindowListener(this)
                .create();

        mCommonPopupWindow.showAsDropDown(view, view.getWidth(), -view.getHeight());
    }

    //向左弹出
    public void showLeftPopupWindow(View view) {
        if (mCommonPopupWindow != null && mCommonPopupWindow.isShowing()) {
            return;
        }

        mCommonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_window_left_or_right)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimationLeft)
                .setOnPopupWindowListener(this)
                .create();

        mCommonPopupWindow.showAsDropDown(view, -mCommonPopupWindow.getWidth(), -view.getHeight());
    }

    //相册弹出
    public void showPhotoPopupWindow(View view) {
        if (mCommonPopupWindow != null && mCommonPopupWindow.isShowing()) {
            return;
        }

        View upView = LayoutInflater.from(this).inflate(R.layout.popup_window_up, null);

        //测量View的宽高
        ScreenUtils.measureWidthAndHeight(upView);

        mCommonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_window_up)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundAlpha(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimationUp)
                .setOnPopupWindowListener(this)
                .create();

        mCommonPopupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    //向上弹出
    public void showUpPopupWindow(View view) {
        if (mCommonPopupWindow != null && mCommonPopupWindow.isShowing()) {
            return;
        }

        mCommonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_window_left_or_right)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnPopupWindowListener(this)
                .create();

        mCommonPopupWindow.showAsDropDown(view, 0, -(mCommonPopupWindow.getHeight() + view.getMeasuredHeight()));
    }

    public void showReminderPopupWindow(View view) {
        if (mCommonPopupWindow != null && mCommonPopupWindow.isShowing()) {
            return;
        }

        mCommonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.pupup_window_reminder)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

        mCommonPopupWindow.showAsDropDown(view, (-mCommonPopupWindow.getWidth() + DensityUtils.dp2px(this, 20)), -(mCommonPopupWindow.getHeight() + view.getMeasuredHeight()));
    }

    @Override
    public void getView(View view, int layoutResId) {
        //获得PopupWindow布局里的View
        switch (layoutResId) {
            case R.layout.popup_window_down:
//                RecyclerView recycleView = (RecyclerView) view.findViewById(R.id.recycle_view);
                break;
            case R.layout.popup_window_up:
//                Button takePhoto = (Button) view.findViewById(R.id.btn_take_photo);
//                Button selectPhoto = (Button) view.findViewById(R.id.btn_select_photo);
//                Button cancel = (Button) view.findViewById(R.id.btn_cancel);
                break;
            case R.layout.popup_window_left_or_right:
//                TextView like = (TextView) view.findViewById(R.id.tv_like);
//                TextView hate = (TextView) view.findViewById(R.id.tv_hate);
                break;
        }
    }
}