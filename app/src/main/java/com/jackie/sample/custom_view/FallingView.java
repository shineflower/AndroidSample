package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jackie.sample.R;
import com.jackie.sample.falling.Flake;

/**
 * Created by Jackie on 2017/7/5.
 */

public class FallingView extends RelativeLayout {
    private int mFlakeResId;
    private int mScale;
    private int mFlakeDensity;
    private int mDelay = DEFAULT_DELAY;

    private Paint mPaint;

    private int mWidth;
    private int mHeight;
    private int mRawWidth;

    private Flake[] mFlakes;
    private Bitmap mFlakeBitmap;

    private static final int DEFAULT_SCALE = 3;
    private static final int DEFAULT_FLAKE_DENSITY = 80;
    private static final int DEFAULT_DELAY = 10;

    public FallingView(Context context) {
        this(context, null);
    }

    public FallingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FallingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setBackgroundColor(Color.TRANSPARENT);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FallingView);

        mFlakeResId = ta.getResourceId(R.styleable.FallingView_flakeSrc, R.drawable.snow_flake);
        mScale = ta.getInt(R.styleable.FallingView_flakeScale, DEFAULT_SCALE);
        mFlakeDensity = ta.getInt(R.styleable.FallingView_flakeDensity, DEFAULT_FLAKE_DENSITY);
        mDelay = ta.getInt(R.styleable.FallingView_fallingDelay, DEFAULT_DELAY);

        ta.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w != oldw || h != oldh) {
            mWidth = w;
            mHeight = h;

            mRawWidth = initScale(mScale);
            initDensity(w, h, mRawWidth);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        for (Flake flake : mFlakes){
            flake.draw(canvas, mFlakeBitmap);
        }

        getHandler().postDelayed(mRunnable, mDelay);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    private void initDensity(int w, int h, int rawWidth) {
        mFlakes = new Flake[mFlakeDensity];

        for (int i = 0; i < mFlakeDensity; i++) {
            mFlakes[i] = Flake.create(w, h, mPaint, rawWidth / mScale);
        }
    }

    private int initScale(int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), mFlakeResId, options);
        int rawWidth = options.outWidth;
        mRawWidth = rawWidth;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        mFlakeBitmap = BitmapFactory.decodeResource(getResources(), mFlakeResId, options);

        return rawWidth;
    }

    public void setImageResource(int flakeResId) {
        this.mFlakeResId = flakeResId;

        initScale(mScale);
    }

    public void setScale(int scale){
        initScale(scale);
    }

    public void setDensity(int density){
        this.mFlakeDensity = density;

        if (mWidth > 0 && mHeight > 0) {
            initDensity(mWidth, mHeight, mRawWidth);
        }
    }

    public void setDelay(int delay){
        this.mDelay = delay;
    }
}
