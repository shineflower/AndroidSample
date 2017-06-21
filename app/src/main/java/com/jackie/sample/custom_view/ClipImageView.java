package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Jackie on 2016/3/16.
 * 裁剪
 */
public class ClipImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private boolean mIsInit = false;
    private Matrix mImageMatrix;
    private ScaleGestureDetector mScaleGestureDetector;

    /**
     * 初始化时的缩放值
     */
    private float mInitScale;
    /**
     * 双击时的缩放值
     */
    private float mMidScale;

    /**
     * 缩放的最大值
     */
    private float mMaxScale;


    //-------------------自由移动
    /**
     * 记录上一次多点触控的数量
     */
    private int mLastPointerCount;
    private float mLastX, mLastY;
    private boolean mIsCanDrag;
    private int mTouchSlop;

    //------------------双击放大和缩小
    private GestureDetector mGestureDetector;
    private boolean mIsAutoScale;

    public ClipImageView(Context context) {
        this(context, null);
    }

    public ClipImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mImageMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mIsAutoScale) {
                    return true;
                }

                float x = e.getX();
                float y = e.getY();

                float scale = getScale();  //获得当前的放大缩小比例
                if (scale < mMidScale) {
//                    mImageMatrix.postScale(mInitScale / scale, mInitScale / scale, x, y);
//                    checkBorderAndCenterWhenScale();
//                    setImageMatrix(mImageMatrix);

                    postDelayed(new AutoScaleRunnable(mMidScale, x, y), 16);
                    mIsAutoScale = true;
                } else {
//                    mImageMatrix.postScale(mInitScale / scale, mInitScale / scale, x, y);
//                    checkBorderAndCenterWhenScale();
//                    setImageMatrix(mImageMatrix);

                    postDelayed(new AutoScaleRunnable(mInitScale, x, y), 16);
                    mIsAutoScale = true;
                }
                return true;
            }
        });
    }

    /**
     * 自动缩放
     */
    private class AutoScaleRunnable implements Runnable {
        /**
         * 缩放的目标值
         */
        float targetScale;
        float x;
        float y;

        private final float BIGGER = 1.07f;
        private final float SMALLER = 0.93f;

        float tempScale;

        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.targetScale = targetScale;
            this.x = x;
            this.y = y;

            if (getScale() < this.targetScale) {
                tempScale = BIGGER;
            }

            if (getScale() > this.targetScale) {
                tempScale = SMALLER;
            }
        }

        @Override
        public void run() {
            /**
             * 进行缩放
             */
            mImageMatrix.postScale(tempScale, tempScale, x, y);
            checkBorder();
            setImageMatrix(mImageMatrix);

            float currentScale = getScale();

            if ((tempScale > 1.0f && currentScale < targetScale) || (tempScale < 1.0f && currentScale > targetScale)) {
                postDelayed(this, 16);
            } else {
                //设置成目标值
                float scale = targetScale / currentScale;
                mImageMatrix.postScale(scale, scale, x, y);
                checkBorder();
                setImageMatrix(mImageMatrix);

                mIsAutoScale = false;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding = 20;
    /**
     * 垂直方向与View的边距
     */
    private int mVerticalPadding;

    @Override
    public void onGlobalLayout() {
        if (!mIsInit) {
            //获取控件的宽和高
            int width = getWidth();
            int height = getHeight();

            //获取图片的宽和高
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }

            //计算padding的px
            mHorizontalPadding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding,
                    getResources().getDisplayMetrics());
            // 垂直方向的边距
            mVerticalPadding = (height - (width - 2 * mHorizontalPadding)) / 2;

            //拿到图片的宽和高
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();

            float scale = 1.0f;

            if (dw < width - mHorizontalPadding * 2
                    && dh > height - mVerticalPadding * 2) {
                scale = (width * 1.0f - mHorizontalPadding * 2) / dw;
            }

            if (dh < height - mVerticalPadding * 2
                    && dw > width - mHorizontalPadding * 2) {
                scale = (height * 1.0f - mVerticalPadding * 2) / dh;
            }

            if (dw < width - mHorizontalPadding * 2
                    && dh < height - mVerticalPadding * 2) {
                float scaleW = (width * 1.0f - mHorizontalPadding * 2) / dw;
                float scaleH = (height * 1.0f - mVerticalPadding * 2) / dh;

                scale = Math.max(scaleW, scaleH);
            }

            mInitScale = scale;
            mMidScale = scale * 2;
            mMaxScale = scale * 4;

            mImageMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
            mImageMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mImageMatrix);

            mIsInit = true;
        }
    }

    private float getScale() {
        float[] values = new float[9];
        mImageMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        Drawable drawable = getDrawable();
        if (drawable == null) {
            return true;
        }

        //控制缩放范围
        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }

            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }

            mImageMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorder();
            setImageMatrix(mImageMatrix);
        }

        return true;
    }

    /**
     * 获得图片放大和缩小的宽和高，以及left, top, right, bottom
     * @return  图片矩阵
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mImageMatrix;

        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }

        return rectF;
    }

    private void checkBorder() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        // 如果宽或高大于屏幕，则控制范围
        if (rect.width() >= width - 2 * mHorizontalPadding) {
            if (rect.left > mHorizontalPadding) {
                deltaX = -rect.left + mHorizontalPadding;
            }

            if (rect.right < width - mHorizontalPadding) {
                deltaX = width - mHorizontalPadding - rect.right;
            }
        }

        if (rect.height() >= height - 2 * mVerticalPadding) {
            if (rect.top > mVerticalPadding) {
                deltaY = -rect.top + mVerticalPadding;
            }

            if (rect.bottom < height - mVerticalPadding) {
                deltaY = height - mVerticalPadding - rect.bottom;
            }
        }

        mImageMatrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        mScaleGestureDetector.onTouchEvent(event);

        float x = 0;
        float y = 0;
        //拿到多点触控的数量
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i ++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        //拿到多点触控的中心点的坐标
        x /= pointerCount;
        y /= pointerCount;

        if (mLastPointerCount != pointerCount) {
            mIsCanDrag = false;
            mLastX = x;
            mLastY = y;
        }

        mLastPointerCount = pointerCount;

        RectF rectF = getMatrixRectF();
        int width = getWidth();
        int height = getHeight();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > width + 0.01f || rectF.height() > height + 0.01f) {
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > width + 0.01f || rectF.height() > height + 0.01f) {
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!mIsCanDrag) {
                    mIsCanDrag = isMoveAction(dx, dy);
                }

                if (mIsCanDrag) {
                    Drawable drawable = getDrawable();
                    if (drawable != null) {
                        if (rectF.width() < width) {
                            dx = 0;
                        }

                        if (rectF.height() < height) {
                            dy = 0;
                        }

                        mImageMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mImageMatrix);
                    }
                }

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }


        return true;
    }

    /**
     * 移动的时候，进行边界检测
     */
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        int width = getWidth();
        int height = getHeight();

        float dx = 0;
        float dy = 0;

        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                dx = -rectF.left;
            }

            if (rectF.right < width) {
                dx = width - rectF.right;
            }
        }

        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                dy = -rectF.top;
            }

            if (rectF.bottom < height) {
                dy = height - rectF.bottom;
            }
        }

        mImageMatrix.postTranslate(dx, dy);
    }

    private boolean isMoveAction(float x, float y) {
        return Math.sqrt(x * x + y * y) > mTouchSlop;
    }

    /**
     * 剪切图片，返回剪切后的bitmap对象
     * @return Bitmap
     */
    public Bitmap clip() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);

        return Bitmap.createBitmap(bitmap, mHorizontalPadding,
                mVerticalPadding, getWidth() - 2 * mHorizontalPadding,
                getWidth() - 2 * mHorizontalPadding);
    }

    public void setHorizontalPadding(int horizontalPadding) {
        this.mHorizontalPadding = horizontalPadding;
    }
}
