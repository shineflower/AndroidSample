package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2016/1/25.
 * 刮刮卡
 */
public class ScratchView extends View {
    private int mWidth;
    private int mHeight;

    private Paint mOuterPaint;
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private float mLastX;
    private float mLastY;

    private Bitmap mOuterBitmap;

    //---------------------------
    private Paint mTextPaint;
    private String mText;
    private int mTextSize;
    private int mTextColor;

    /**
     * 记录刮奖信息文本的宽和高
     */
    private Rect mTextBound;
    //判断遮盖层区域消除是否达到阀值
    private volatile boolean mIsSwipeCompleted = false;

    public interface OnSwipeCompletedListener{
        void onSwipeCompleted();
    }

    private OnSwipeCompletedListener mOnSwipeCompletedListener;

    public void setOnSwipeCompletedListener(OnSwipeCompletedListener onSwipeCompletedListener) {
        this.mOnSwipeCompletedListener = onSwipeCompletedListener;
    }

    public ScratchView(Context context) {
        this(context, null);
    }

    public ScratchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScratchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = null;
        try {
            ta  = context.obtainStyledAttributes(attrs, R.styleable.ScratchView);
            mText = ta.getString(R.styleable.ScratchView_text);
            mTextSize = (int) ta.getDimension(R.styleable.ScratchView_textSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
            mTextColor = ta.getColor(R.styleable.ScratchView_textColor, Color.DKGRAY);
        } finally {
            ta.recycle();
        }

        init();
    }

    public void setText(String text) {
        this.mText = text;
        //重新设置字体的宽高
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        setMeasuredDimension(mWidth, mHeight);

        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        setupOuterPaint();
        setupBackTextPaint();

        //绘制灰色遮盖层
//        mCanvas.drawColor(Color.parseColor("#C0C0C0"));
        //绘制圆角图片
        mCanvas.drawRoundRect(new RectF(0, 0, mWidth, mHeight), 30, 30, mOuterPaint);
        mCanvas.drawBitmap(mOuterBitmap, null, new Rect(0, 0, mWidth, mHeight), null);
    }

    /**
     * 设置绘制Path的画笔的属性
     */
    private void setupOuterPaint() {
        mOuterPaint.setColor(Color.parseColor("#C0C0C0"));
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setDither(true);
        mOuterPaint.setStrokeJoin(Paint.Join.ROUND);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStyle(Paint.Style.FILL);
        mOuterPaint.setStrokeWidth(20);
    }

    /**
     * 设置绘制文本的画笔的属性
     */
    private void setupBackTextPaint() {
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(mTextSize);

        //获取当前文本的宽和高
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    /**
     * 初始化
     */
    private void init() {
        mOuterPaint = new Paint();
        mPath = new Path();

        mOuterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fg_guaguaka);

        mTextPaint = new Paint();
        mTextBound = new Rect();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;

                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = Math.abs(x - mLastX);
                float deltaY = Math.abs(y - mLastY);

                if (deltaX > 3 || deltaY > 3) {
                    mPath.lineTo(x, y);
                }

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                new Thread(mRunnable).start();
                break;
            default:
                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText(mText, mWidth / 2 - mTextBound.width() / 2, mHeight / 2 + mTextBound.height() / 2, mTextPaint);

        if (mIsSwipeCompleted) {
            if (mOnSwipeCompletedListener != null) {
                mOnSwipeCompletedListener.onSwipeCompleted();
            }
        } else {
            drawPath();
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    private void drawPath() {
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, mOuterPaint);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            float totalArea = mWidth * mHeight;
            float swipeArea = 0;

            int[] pixels = new int[mWidth * mHeight];
            mBitmap.getPixels(pixels, 0, mWidth, 0, 0, mWidth, mHeight);

            for (int i = 0; i < mWidth; i++) {
                for (int j = 0; j < mHeight; j++) {
                    int index = i + j * mWidth;

                    if (pixels[index] == 0) {
                        swipeArea++;
                    }
                }
            }

            if (swipeArea > 0 && totalArea > 0) {
                int percent = (int) (swipeArea * 100 / totalArea);

                if (percent > 60) {
                    //清掉图层区域
                    mIsSwipeCompleted = true;
                    postInvalidate();
                }
            }
        }
    };
}
