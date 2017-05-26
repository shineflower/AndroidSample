package com.jackie.sample.custom_view;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.float_window.FloatWindowManager;
import com.jackie.sample.utils.ScreenUtils;
import com.jackie.sample.utils.SystemUtils;

/**
 * Created by Jackie on 2017/5/25.
 */

public class FloatWindowSmallView extends LinearLayout {
    //记录小悬浮窗的宽度
    private int mWindowWidth;
    //记录小悬浮窗的高度
    private int mWindowHeight;
    //记录系统状态栏的高度
    private int mStatusBarHeight;
    //用于更新小悬浮窗的高度
    private WindowManager mWindowManager;

    //小悬浮窗的布局
    private LinearLayout mWindowSmallLayout;
    //小火箭控件
    private ImageView mRocketImage;
    //小悬浮窗的参数
    private WindowManager.LayoutParams mParams;

    //记录手机按下时在小悬浮窗的View上的横纵坐标
    private float mDownX;
    private float mDownY;

    //记录手指按下位置在屏幕上的横纵坐标值
    private float mDownRawX;
    private float mDownRawY;

    //记录当前手指位置在屏幕上的横纵坐标值
    private float mCurrentX;
    private float mCurrentY;

    //记录小火箭的宽度和高度
    private int mRocketWidth;
    private int mRocketHeight;

    //记录当前手指是否按下
    private boolean mIsPressed;

    private Context mContext;

    private FloatWindowManager mFloatWindowManager;

    public FloatWindowSmallView(Context context) {
        this(context, null);
    }

    public FloatWindowSmallView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatWindowSmallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);

        mWindowSmallLayout = (LinearLayout) findViewById(R.id.float_window_small_layout);
        mWindowWidth = mWindowSmallLayout.getLayoutParams().width;
        mWindowHeight = mWindowSmallLayout.getLayoutParams().height;

        mRocketImage = (ImageView) findViewById(R.id.rocket_image);
        mRocketWidth = mRocketImage.getLayoutParams().width;
        mRocketHeight = mRocketImage.getLayoutParams().height;

        mContext = context;

        mFloatWindowManager = FloatWindowManager.getInstance(context);

        TextView percentView = (TextView) findViewById(R.id.percent);
        percentView.setText(SystemUtils.getUsedMemoryPercentValue(context));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsPressed = true;
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                mDownX = event.getX();
                mDownY = event.getY();

                mDownRawX = event.getRawX();
                mDownRawY = event.getRawY();

                mCurrentX = event.getRawX();
                mCurrentY = event.getRawY() - ScreenUtils.getStatusBarHeight(mContext);
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getRawX();
                mCurrentY = event.getRawY() - ScreenUtils.getStatusBarHeight(mContext);

                //手指移动的时候更新小悬浮窗的状态和位置
                updateStatus();
                updatePosition(event);
                break;
            case MotionEvent.ACTION_UP:
                mIsPressed = false;

                if (mFloatWindowManager.isReadyToLaunch()) {
                    launchRocket();
                } else {
                    updateStatus();
                    updatePosition(event);

                    //如果手指离开屏幕时，mDownXInScreen和xInScreen相等，且mDownYInScreen和mYInScreen相等，则视为触发了单击事件。
                    if (mDownRawX == mCurrentX && mDownRawY == mCurrentY) {
                        openBigWindow();
                    }
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params  悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 用于发射小火箭
     */
    private void launchRocket() {
        mFloatWindowManager.removeLauncher();
        new LaunchTask().execute();
    }

    /**
     * 更新View的显示状态，判断显示悬浮窗还是小火箭
     */
    private void updateStatus() {
        if(mIsPressed && mRocketImage.getVisibility() != View.VISIBLE) {
            mParams.width = mRocketWidth;
            mParams.height = mRocketHeight;

            mWindowManager.updateViewLayout(this, mParams);

            mWindowSmallLayout.setVisibility(View.GONE);
            mRocketImage.setVisibility(View.VISIBLE);
            mFloatWindowManager.createLauncher(mContext);
        } else if (!mIsPressed) {
            mParams.width = mWindowWidth;
            mParams.height = mWindowHeight;

            mWindowManager.updateViewLayout(this, mParams);

            mWindowSmallLayout.setVisibility(View.VISIBLE);
            mRocketImage.setVisibility(View.GONE);
            mFloatWindowManager.removeLauncher();
        }
    }

    /**
     * 更新小悬浮窗在屏幕中的位置
     */
    private void updatePosition(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mParams.x = (int) (mCurrentX - mDownX);
                break;
            case MotionEvent.ACTION_UP:
                //保证小悬浮窗只能停留在手机屏幕的左右两边
                int deltaX = (int) (mCurrentX - mDownX);
                if (deltaX < ScreenUtils.getScreenWidth(mContext) / 2) {
                    mParams.x = 0;
                } else {
                    mParams.x = ScreenUtils.getScreenWidth(mContext);
                }
                break;
        }

        mParams.y = (int) (mCurrentY - mDownY);

        mWindowManager.updateViewLayout(this, mParams);
        mFloatWindowManager.updateLauncher();
    }

    /**
     * 打开大悬浮窗，同时关闭小悬浮窗
     */
    private void openBigWindow() {
        mFloatWindowManager.createBigWindow(mContext);
        mFloatWindowManager.removeSmallWindow();
    }

    /**
     * 开始执行发射小火箭的任务。
     */
    private class LaunchTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // 在这里对小火箭的位置进行改变，从而产生火箭升空的效果
            while (mParams.y > 0) {
                mParams.y = mParams.y - 10;
                publishProgress();

                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            mWindowManager.updateViewLayout(FloatWindowSmallView.this, mParams);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 火箭升空结束后，回归到悬浮窗状态
            updateStatus();

            mParams.x = (int) (mDownRawX - mDownX);
            mParams.y = (int) (mDownRawY - mDownY);
            mWindowManager.updateViewLayout(FloatWindowSmallView.this, mParams);
        }
    }

    public int getWindowWidth() {
        return mWindowWidth;
    }

    public int getWindowHeight() {
        return mWindowHeight;
    }
}
