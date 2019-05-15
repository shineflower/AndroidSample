package com.jackie.sample.tick_view.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.jackie.sample.utils.DensityUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Jackie on 2019/5/14.
 * 控件配置
 */
public class TickViewConfig {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ ANIM_ALPHA, ANIM_DYNAMIC })
    public @interface TickAnimType {

    }

    public static final int ANIM_ALPHA = 0;
    public static final int ANIM_DYNAMIC = 1;

    private int mTickAnim = ANIM_ALPHA;

    private volatile boolean mIsNeedToReApply;

    private boolean mClickable = true;

    private int mUncheckBaseColor;
    private int mCheckBaseColor;
    private int mCheckTickColor;
    private int mRadius;

    // 勾的半径
    private float mTickRadius;
    // 勾的偏移
    private float mTickRadiusOffset;

    private OnCheckedChangeListener mOnCheckedChangeListener;
    private TickAnimatorListener mTickAnimatorListener;

    public TickViewConfig(Context context) {
        this(context, null);
    }

    public TickViewConfig(Context context, TickViewConfig config) {
        if (config != null) {
            setConfig(config);
        } else {
            setNeedToReApply(true);
            setupDefaultValue(context);
        }
    }

    private void setupDefaultValue(Context context) {
        this.setUnCheckBaseColor(Color.parseColor("#ffeaeaea"))
                .setCheckBaseColor(Color.parseColor("#fff5d747"))
                .setCheckTickColor(Color.WHITE)
                .setRadius(DensityUtils.dp2px(context, 30))
                .setClickable(true)
                .setTickRadius(DensityUtils.dp2px(context, 12))
                .setTickRadiusOffset(DensityUtils.dp2px(context, 4))
                .setTickAnim(ANIM_ALPHA);
    }

    boolean isNeedToReApply() {
        return mIsNeedToReApply;
    }

    TickViewConfig setNeedToReApply(boolean isNeedToReApply) {
        this.mIsNeedToReApply = isNeedToReApply;
        return this;
    }


    public boolean isClickable() {
        return mClickable;
    }

    public TickViewConfig setClickable(boolean clickable) {
        this.mClickable = clickable;
        return this;
    }

    public int getUnCheckBaseColor() {
        return mUncheckBaseColor;
    }

    public TickViewConfig setUnCheckBaseColor(int uncheckBaseColor) {
        this.mUncheckBaseColor = uncheckBaseColor;
        return setNeedToReApply(true);
    }

    public int getCheckBaseColor() {
        return mCheckBaseColor;
    }

    public TickViewConfig setCheckBaseColor(int checkBaseColor) {
        this.mCheckBaseColor = checkBaseColor;
        return setNeedToReApply(true);
    }

    public int getCheckTickColor() {
        return mCheckTickColor;
    }

    public TickViewConfig setCheckTickColor(int checkTickColor) {
        this.mCheckTickColor = checkTickColor;
        return setNeedToReApply(true);
    }

    public int getRadius() {
        return mRadius;
    }

    public TickViewConfig setRadius(int radius) {
        this.mRadius = radius;
        return setNeedToReApply(true);
    }

    public float getTickRadius() {
        return mTickRadius;
    }

    public TickViewConfig setTickRadius(float tickRadius) {
        this.mTickRadius = tickRadius;
        return setNeedToReApply(true);
    }

    public float getTickRadiusOffset() {
        return mTickRadiusOffset;
    }

    public TickViewConfig setTickRadiusOffset(float tickRadiusOffset) {
        this.mTickRadiusOffset = tickRadiusOffset;
        return setNeedToReApply(true);
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public TickViewConfig setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
        return this;
    }

    public TickAnimatorListener getTickAnimatorListener() {
        return mTickAnimatorListener;
    }

    public TickViewConfig setTickAnimatorListener(TickAnimatorListener tickAnimatorListener) {
        mTickAnimatorListener = tickAnimatorListener;
        return this;
    }

    public int getTickAnim() {
        return mTickAnim;
    }

    public TickViewConfig setTickAnim(@TickAnimType int tickAnim) {
        this.mTickAnim = tickAnim;
        return setNeedToReApply(true);
    }

    public TickViewConfig setConfig(@NonNull TickViewConfig config) {
        if (config == null) return this;

        return setClickable(config.isClickable())
                .setUnCheckBaseColor(config.getUnCheckBaseColor())
                .setCheckBaseColor(config.getCheckBaseColor())
                .setCheckTickColor(config.getCheckTickColor())
                .setOnCheckedChangeListener(config.getOnCheckedChangeListener())
                .setTickAnimatorListener(config.getTickAnimatorListener())
                .setTickRadius(config.getTickRadius())
                .setTickRadiusOffset(config.getTickRadiusOffset())
                .setTickAnim(config.getTickAnim());

    }
}
