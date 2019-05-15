package com.jackie.sample.tick_view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by Jackie on 2019/5/14.
 * 自定义view 一个打钩的小动画
 */
public class TickView extends View {
    // 内圆的画笔
    private Paint mPaintCircle;
    // 外层圆环的画笔
    private Paint mPaintRing;
    // 打钩的画笔
    private Paint mPaintTick;

    // 整个圆外切的矩形
    private RectF mRectF = new RectF();

    // 控件中心的x, y坐标
    private int mCenterX;
    private int mCenterY;

    // 计算器
    private int mCircleRadius = -1;
    private int mRingProgress = 0;

    // 是否被点亮
    private boolean mIsChecked = false;
    // 是否处于动画中
    private boolean mIsAnimationRunning = false;

    // 最后扩大缩小动画中，画笔的宽度和最大倍数
    private static final int SCALE_TIMES = 6;

    private AnimatorSet mFinalAnimatorSet;

    private int mRingAnimatorDuration;
    private int mCircleAnimatorDuration;
    private int mScaleAnimatorDuration;

    private TickViewConfig mConfig;

    private Path mTickPath;
    private Path mTickPathMeasureDst;
    private PathMeasure mPathMeasure;

    public TickView(Context context) {
        this(context, null);
    }

    public TickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(attrs);
        initPaint();
        initAnimatorCounter();
        setupEvent();
    }

    private void initAttrs(AttributeSet attrs) {
        if (mConfig == null) {
            mConfig = new TickViewConfig(getContext());
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TickView);
        mConfig.setUnCheckBaseColor(typedArray.getColor(R.styleable.TickView_uncheck_base_color,
                getResources().getColor(R.color.tick_gray)))
                .setCheckBaseColor(typedArray.getColor(R.styleable.TickView_check_base_color,
                        getResources().getColor(R.color.tick_yellow)))
                .setCheckTickColor(typedArray.getColor(R.styleable.TickView_check_tick_color,
                        getResources().getColor(R.color.tick_white)))
                .setRadius(typedArray.getDimensionPixelOffset(R.styleable.TickView_radius,
                        DensityUtils.dp2px(getContext(),30)))
                .setClickable(typedArray.getBoolean(R.styleable.TickView_clickable, true))
                .setTickRadius(DensityUtils.dp2px(getContext(),12))
                .setTickRadiusOffset(DensityUtils.dp2px(getContext(),4));

        int rateMode = typedArray.getInt(R.styleable.TickView_rate, 1);
        TickRateEnum mTickRateEnum = TickRateEnum.getRateEnum(rateMode);
        mRingAnimatorDuration = mTickRateEnum.getmRingAnimatorDuration();
        mCircleAnimatorDuration = mTickRateEnum.getmCircleAnimatorDuration();
        mScaleAnimatorDuration = mTickRateEnum.getmScaleAnimatorDuration();
        typedArray.recycle();
        applyConfig(mConfig);
        setupEvent();
        if (mTickPath == null) mTickPath = new Path();
        if (mTickPathMeasureDst == null) mTickPathMeasureDst = new Path();
        if (mPathMeasure == null) mPathMeasure = new PathMeasure();
    }

    private void applyConfig(TickViewConfig config) {
        assert mConfig != null : "TickView Config must not be null";
        mConfig.setConfig(config);
        if (mConfig.isNeedToReApply()) {
            initPaint();
            initAnimatorCounter();
            mConfig.setNeedToReApply(false);
        }
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        if (mPaintRing == null) mPaintRing = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintRing.setStyle(Paint.Style.STROKE);
        mPaintRing.setColor(mIsChecked ? mConfig.getCheckBaseColor() : mConfig.getUnCheckBaseColor());
        mPaintRing.setStrokeCap(Paint.Cap.ROUND);
        mPaintRing.setStrokeWidth(DensityUtils.dp2px(getContext(), 2.5f));

        if (mPaintCircle == null) mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setColor(mConfig.getCheckBaseColor());

        if (mPaintTick == null) mPaintTick = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTick.setColor(mIsChecked ? mConfig.getCheckTickColor() : mConfig.getUnCheckBaseColor());
        mPaintTick.setStyle(Paint.Style.STROKE);
        mPaintTick.setStrokeCap(Paint.Cap.ROUND);
        mPaintTick.setStrokeWidth(DensityUtils.dp2px(getContext(), 2.5f));
    }

    /**
     * 用ObjectAnimator初始化一些计数器
     */
    private void initAnimatorCounter() {
        // 圆环进度
        ObjectAnimator ringAnimator = ObjectAnimator.ofInt(this, "ringProgress", 0, 360);
        ringAnimator.setDuration(mRingAnimatorDuration);
        ringAnimator.setInterpolator(null);
        // 收缩动画
        ObjectAnimator circleAnimator = ObjectAnimator.ofInt(this, "circleRadius", mConfig.getRadius() - 5, 0);
        circleAnimator.setDuration(mCircleAnimatorDuration);
        circleAnimator.setInterpolator(new DecelerateInterpolator());

        Animator tickAnimator;

        // 勾勾的alpha动画
        if (mConfig.getTickAnim() == TickViewConfig.ANIM_ALPHA) {
            // 勾出来的透明渐变
            tickAnimator = ObjectAnimator.ofInt(this, "tickAlpha", 0, 255);
            tickAnimator.setDuration(200);
        } else {
            // 勾勾采取动态画出
            tickAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            tickAnimator.setDuration(400);
            tickAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);

                    setTickProgress(0);
                    mPathMeasure.nextContour();
                    mPathMeasure.setPath(mTickPath, false);
                    mTickPathMeasureDst.reset();
                }
            });

            ((ValueAnimator) tickAnimator).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setTickProgress((Float) animation.getAnimatedValue());
                }
            });
            tickAnimator.setInterpolator(new DecelerateInterpolator());
        }

        //最后的放大再回弹的动画，改变画笔的宽度来实现
        ObjectAnimator mScaleAnimator = ObjectAnimator.ofFloat(this, "ringStrokeWidth", mPaintRing.getStrokeWidth(), mPaintRing.getStrokeWidth() * SCALE_TIMES, mPaintRing.getStrokeWidth() / SCALE_TIMES);
        mScaleAnimator.setInterpolator(null);
        mScaleAnimator.setDuration(mScaleAnimatorDuration);

        //打钩和放大回弹的动画一起执行
        AnimatorSet alphaScaleAnimatorSet = new AnimatorSet();
        alphaScaleAnimatorSet.playTogether(tickAnimator, mScaleAnimator);

        mFinalAnimatorSet = new AnimatorSet();
        mFinalAnimatorSet.playSequentially(ringAnimator, circleAnimator, alphaScaleAnimatorSet);
        mFinalAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mConfig.getTickAnimatorListener() != null) {
                    mConfig.getTickAnimatorListener().onAnimationEnd(TickView.this);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (mConfig.getTickAnimatorListener() != null) {
                    mConfig.getTickAnimatorListener().onAnimationStart(TickView.this);
                }
            }
        });
    }

    /**
     * 设置点击事件
     */
    private void setupEvent() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mConfig.isClickable()) {
                    toggle();
                    if (mConfig.getOnCheckedChangeListener() != null) {
                        mConfig.getOnCheckedChangeListener().onCheckedChanged((TickView) view, mIsChecked);
                    }
                }
            }
        });
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                mySize = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                mySize = size;
                break;
            default:
                break;
        }
        return mySize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int radius = mConfig.getRadius();
        final float tickRadius = mConfig.getTickRadius();
        final float tickRadiusOffset = mConfig.getTickRadiusOffset();

        //控件的宽度等于动画最后的扩大范围的半径
        int width = getMySize((radius + DensityUtils.dp2px(getContext(), 2.5f) * SCALE_TIMES) * 2, widthMeasureSpec);
        int height = getMySize((radius + DensityUtils.dp2px(getContext(), 2.5f) * SCALE_TIMES) * 2, heightMeasureSpec);

        height = width = Math.max(width, height);

        setMeasuredDimension(width, height);

        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;

        //设置圆圈的外切矩形
        mRectF.set(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);

        //设置打钩的几个点坐标
        final float startX = mCenterX - tickRadius + tickRadiusOffset;
        final float startY = (float) mCenterY;

        final float middleX = mCenterX - tickRadius / 2 + tickRadiusOffset;
        final float middleY = mCenterY + tickRadius / 2;

        final float endX = mCenterX + tickRadius * 2 / 4 + tickRadiusOffset;
        final float endY = mCenterY - tickRadius * 2 / 4;

        mTickPath.reset();
        mTickPath.moveTo(startX, startY);
        mTickPath.lineTo(middleX, middleY);
        mTickPath.lineTo(endX, endY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mConfig.isNeedToReApply()) {
            applyConfig(mConfig);
        }
        super.onDraw(canvas);
        if (!mIsChecked) {
            canvas.drawArc(mRectF, 90, 360, false, mPaintRing);
            canvas.drawPath(mTickPath, mPaintTick);
            return;
        }
        //画圆弧进度
        canvas.drawArc(mRectF, 90, mRingProgress, false, mPaintRing);
        //画黄色的背景
        mPaintCircle.setColor(mConfig.getCheckBaseColor());
        canvas.drawCircle(mCenterX, mCenterY, mRingProgress == 360 ? mConfig.getRadius() : 0, mPaintCircle);
        //画收缩的白色圆
        if (mRingProgress == 360) {
            mPaintCircle.setColor(mConfig.getCheckTickColor());
            canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mPaintCircle);
        }
        //画勾,以及放大收缩的动画
        if (mCircleRadius == 0) {
            if (mConfig.getTickAnim() == TickViewConfig.ANIM_DYNAMIC) {
                mPaintTick.setAlpha((int) (255 * tickProgress));
                mPathMeasure.getSegment(0, tickProgress * mPathMeasure.getLength(), mTickPathMeasureDst, true);
                canvas.drawPath(mTickPathMeasureDst, mPaintTick);
            } else {
                canvas.drawPath(mTickPath, mPaintTick);
            }
            canvas.drawArc(mRectF, 0, 360, false, mPaintRing);
        }
        //ObjectAnimator动画替换计数器
        if (!mIsAnimationRunning) {
            mIsAnimationRunning = true;
            mFinalAnimatorSet.start();
        }
    }

    /*--------------属性动画---getter/setter begin---------------*/

    private int getRingProgress() {
        return mRingProgress;
    }

    private void setRingProgress(int ringProgress) {
        this.mRingProgress = ringProgress;
        postInvalidate();
    }

    private int getCircleRadius() {
        return mCircleRadius;
    }

    private void setCircleRadius(int circleRadius) {
        this.mCircleRadius = circleRadius;
        postInvalidate();
    }

    private float tickProgress = 0.0f;

    private float getTickProgress() {
        return tickProgress;
    }

    private void setTickProgress(float tickProgress) {
        this.tickProgress = tickProgress;
        invalidate();
    }

    private void setTickAlpha(int tickAlpha) {
        mPaintTick.setAlpha(tickAlpha);
        postInvalidate();
    }

    private float getRingStrokeWidth() {
        return mPaintRing.getStrokeWidth();
    }

    private void setRingStrokeWidth(float strokeWidth) {
        mPaintRing.setStrokeWidth(strokeWidth);
        postInvalidate();
    }

    /*--------------属性动画---getter/setter end---------------*/

    /**
     * 改变状态
     *
     * @param checked 选中还是未选中
     */
    public void setChecked(boolean checked) {
        if (this.mIsChecked != checked) {
            mIsChecked = checked;
            reset();
        }
    }

    /**
     * @return 当前状态是否选中
     */
    public boolean isChecked() {
        return mIsChecked;
    }

    /**
     * 改变当前的状态
     */
    public void toggle() {
        setChecked(!mIsChecked);
    }

    /**
     * 重置
     */
    private void reset() {
        initPaint();
        mFinalAnimatorSet.cancel();
        mRingProgress = 0;
        mCircleRadius = -1;
        mIsAnimationRunning = false;
        final int radius = mConfig.getRadius();
        mRectF.set(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);
        invalidate();
    }

    public TickViewConfig getConfig() {
        return mConfig;
    }

    public void setConfig(TickViewConfig tickViewConfig) {
        if (tickViewConfig == null) return;
        applyConfig(tickViewConfig);
    }
}
