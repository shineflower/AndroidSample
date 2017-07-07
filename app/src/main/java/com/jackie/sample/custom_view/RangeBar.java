package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/6/23.
 * 带圆圈的范围选择
 */

public class RangeBar extends ProgressBar {
    private Context mContext;
    private int mTextStyle;

    private Paint mBackgroundPaint;
    private Paint mReachPaint;
    private Paint mBlueBallPaint;
    private Paint mWhiteBallPaint;
    private Paint mBlueTextPaint;
    private Paint mGreyTextPaint;

    // 线的总长度
    private float mLineWidth;
    // 默认线的高度是3dp
    private int mLineHeight;

    // 蓝字14sp
    private int mBlueTextSize = 14;
    // 灰字12sp
    private int mGreyTextSize = 12;

    //默认线离左边的距离是44dp
    private float mPaddingLeft;
    private float mReachWidth;

    // 默认蓝色大圆的半径是14dp
    private float mBlueRadius;
    private float mWhiteRadius;

    private float mDownX;

    public static final int NORMAL = 0;
    public static final int TEXT = 1;
    private static final String ZERO = "0";

    private int mGreyColor = Color.parseColor("#666666");

    private final int BACKGROUND_COLOR = Color.parseColor("#ececec");
    private final int REACH_COLOR = Color.parseColor("#0971ce");
    private final int WHITE_BALL_COLOR = getResources().getColor(android.R.color.white);

    private OnRangeBarChangeListener mOnRangeBarChangeListener;

    public interface OnRangeBarChangeListener {
        void onProgressChanged(int progress);
    }

    public void setOnRangeBarChangeListener(OnRangeBarChangeListener onRangeBarChangeListener) {
        mOnRangeBarChangeListener = onRangeBarChangeListener;
    }

    public RangeBar(Context context) {
        this(context, null);
    }

    public RangeBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;

        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RangeBar);
        mTextStyle = ta.getInt(R.styleable.RangeBar_textStyle, NORMAL);
        ta.recycle();

        mLineHeight = DensityUtils.dp2px(context, 3);
        mPaddingLeft = DensityUtils.dp2px(context, 44);
        mBlueRadius = DensityUtils.dp2px(context, 14);
        mWhiteRadius = DensityUtils.dp2px(context, 13);

        mReachWidth = mPaddingLeft + mWhiteRadius;

        mBackgroundPaint = createLinePaint(BACKGROUND_COLOR, Paint.Style.FILL, mLineHeight);
        mReachPaint = createLinePaint(REACH_COLOR, Paint.Style.FILL, mLineHeight);
        mBlueBallPaint = createBlueBallPaint(REACH_COLOR, Paint.Style.FILL);
        mWhiteBallPaint = createWhiteBallPaint(WHITE_BALL_COLOR, Paint.Style.FILL);
        mBlueTextPaint = createTextPaint(REACH_COLOR, DensityUtils.sp2px(context, mBlueTextSize));
        mGreyTextPaint = createTextPaint(mGreyColor, DensityUtils.sp2px(context, mGreyTextSize));
    }

    private Paint createLinePaint(int color, Paint.Style style, int width) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(style);
        paint.setStrokeWidth(width);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        return paint;
    }

    private Paint createBlueBallPaint(int color, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(style);
        paint.setStrokeJoin(Paint.Join.ROUND);

        return paint;
    }

    private Paint createWhiteBallPaint(int color, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(style);
        paint.setStrokeJoin(Paint.Join.ROUND);

        return paint;
    }

    private Paint createTextPaint(int color, int textSize) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(color);
        paint.setTextSize(textSize);

        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mLineWidth = w - 2 * mPaddingLeft;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        drawRangeBar(canvas);
        canvas.restore();
    }

    private void drawRangeBar(Canvas canvas) {
        canvas.translate(0, getHeight() / 2);
        canvas.drawLine(mPaddingLeft, 0, mPaddingLeft + mLineWidth, 0, mBackgroundPaint);
        canvas.drawLine(mPaddingLeft, 0, mReachWidth, 0, mReachPaint);

        String text = String.valueOf(getProgress());
        float textWidth =  mBlueTextPaint.measureText(text);
        float textHeight = mBlueTextPaint.descent() + mBlueTextPaint.ascent();
        canvas.drawText(text, mReachWidth - textWidth / 2, -mBlueRadius - DensityUtils.dp2px(mContext, 10), mBlueTextPaint);

        switch (mTextStyle) {
            case TEXT:
                String number = ZERO;
                float numberWidth = mGreyTextPaint.measureText(number);
                float numberHeight =  mGreyTextPaint.descent() + mGreyTextPaint.ascent();
                canvas.drawText(number, mPaddingLeft - DensityUtils.dp2px(mContext, 5) - numberWidth, 0 - numberHeight / 2, mGreyTextPaint);
                canvas.drawText(getMax() + "", mPaddingLeft + mLineWidth + DensityUtils.dp2px(mContext, 5), 0 - numberHeight/2, mGreyTextPaint);
                break;
            default:
                break;
        }

        canvas.drawCircle(mReachWidth, 0, mBlueRadius, mBlueBallPaint);
        canvas.drawCircle(mReachWidth, 0, mWhiteRadius, mWhiteBallPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float reachWidth;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();

                if (mDownX >= mPaddingLeft + mWhiteRadius && mDownX <= mPaddingLeft + mLineWidth - mWhiteRadius) {
                    reachWidth = mDownX;
                } else if (mDownX < mPaddingLeft + mWhiteRadius) {
                    reachWidth = mPaddingLeft + mWhiteRadius;
                } else {
                    reachWidth = mPaddingLeft + mLineWidth - mWhiteRadius;
                }

                setReachWidth(reachWidth);

                if (mOnRangeBarChangeListener != null) {
                    mOnRangeBarChangeListener.onProgressChanged(getProgress());
                }

                return true;
            case MotionEvent.ACTION_MOVE:
//                if (event.getY() > getHeight()) {
//                    return super.onTouchEvent(event);
//                }

                mDownX = event.getX();

                if (mDownX >= mPaddingLeft + mWhiteRadius && mDownX <= mPaddingLeft + mLineWidth - mWhiteRadius) {
                    reachWidth = mDownX;
                } else if (mDownX < mPaddingLeft + mWhiteRadius) {
                    reachWidth = mPaddingLeft + mWhiteRadius;
                } else {
                    reachWidth = mPaddingLeft + mLineWidth - mWhiteRadius;
                }

                setReachWidth(reachWidth);

                if (mOnRangeBarChangeListener != null) {
                    mOnRangeBarChangeListener.onProgressChanged(getProgress());
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    private void setReachWidth(float width) {
        mReachWidth = width;
        invalidate();
    }

    @Override
    public synchronized int getProgress() {
        int progress = (int) (((mReachWidth - mPaddingLeft - mWhiteRadius) / (mLineWidth - 2 * mWhiteRadius)) * getMax());
        return progress;
    }

    @Override
    public synchronized void setProgress(int progress) {
        float reachWidth = mPaddingLeft +  mWhiteRadius + (mLineWidth - 2 * mWhiteRadius) * progress / getMax();
        setReachWidth(reachWidth);
    }
}
