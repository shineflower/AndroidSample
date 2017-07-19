package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jackie.sample.utils.CommonUtils;
import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/7/18.
 * 水平拼卡布局
 */

public class HorizontalCardPieceLayout extends LinearLayout {
    private float[] mFirstRatios = FIRST_RATIOS;
    private float[][] mSecondRatios = SECOND_RATIOS;

    private static final float[] FIRST_RATIOS = { 0.5f, 0.3f, 0.1f, 0.1f };
    private static final float[][] SECOND_RATIOS = {
            { 0.4f, 0.4f, 0.1f, 0.1f },
            { 0.3f, 0.2f, 0.5f },
            { 0.1f, 0.9f },
            { 1.0f }
    };

    public HorizontalCardPieceLayout(Context context) {
        this(context, null);
    }

    public HorizontalCardPieceLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalCardPieceLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);

        for (int i = 0; i < mFirstRatios.length; i++) {
            LinearLayout firstLayout = new LinearLayout(context);
            firstLayout.setOrientation(VERTICAL);
            addView(firstLayout);

            firstLayout.setBackgroundColor(Color.WHITE);

            LayoutParams firstParams = (LayoutParams) firstLayout.getLayoutParams();
            firstParams.width = 0;
            firstParams.weight = mFirstRatios[i];
            firstParams.height = LayoutParams.MATCH_PARENT;

            if (i > 0) {
                firstParams.leftMargin = DensityUtils.dp2px(context, 3);
            }

            firstLayout.setLayoutParams(firstParams);



            for (int j = 0; j < mSecondRatios[i].length; j++) {
                View secondView = new View(context);
                firstLayout.addView(secondView);

                secondView.setBackgroundColor(CommonUtils.getInstance().getRandomColor());

                LayoutParams secondParams = (LayoutParams) secondView.getLayoutParams();
                secondParams.width = LayoutParams.MATCH_PARENT;
                secondParams.height = 0;
                secondParams.weight = mSecondRatios[i][j];

                if (j > 0) {
                    secondParams.topMargin = DensityUtils.dp2px(context, 3);
                }

                secondView.setLayoutParams(secondParams);
            }
        }
    }

    /**
     * 设置比例
     * @param firstRatios
     * @param secondRatios
     */
    public void setRatios(float[] firstRatios, float[][] secondRatios) {
        this.mFirstRatios = firstRatios;
        this.mSecondRatios = secondRatios;
    }
}
