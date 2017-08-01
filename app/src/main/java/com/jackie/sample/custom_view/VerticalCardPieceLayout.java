package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.jackie.sample.utils.CommonUtils;
import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/7/18.
 * 垂直拼卡布局
 */

public class VerticalCardPieceLayout extends LinearLayout {

    private float[] mFirstRatios = FIRST_RATIOS;
    private float[][] mSecondRatios = SECOND_RATIOS;

    private static final float[] FIRST_RATIOS = { 0.5f, 0.3f, 0.1f, 0.1f };
    private static final float[][] SECOND_RATIOS = {
            { 0.4f, 0.4f, 0.1f, 0.1f },
            { 0.4f, 0.4f, 0.2f },
            { 0.1f, 0.9f },
            { 1.0f }
    };

    public VerticalCardPieceLayout(Context context) {
        this(context, null);
    }

    public VerticalCardPieceLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalCardPieceLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);

        //第一层布局
        for (int i = 0; i < mFirstRatios.length; i++) {
            LinearLayout firstLayout = new LinearLayout(context);
            firstLayout.setOrientation(HORIZONTAL);
            addView(firstLayout);

            firstLayout.setBackgroundColor(Color.WHITE);

            LinearLayout.LayoutParams firstParams = (LayoutParams) firstLayout.getLayoutParams();
            firstParams.width = LayoutParams.MATCH_PARENT;
            firstParams.height = 0;
            firstParams.weight = mFirstRatios[i];

            if (i > 0) {
                firstParams.topMargin = DensityUtils.dp2px(context, 3);
            }

            firstLayout.setLayoutParams(firstParams);



            //第二层布局
            for (int j = 0; j < mSecondRatios[i].length; j++) {
                LinearLayout secondLayout = new LinearLayout(context);
                firstLayout.addView(secondLayout);

                secondLayout.setBackgroundColor(CommonUtils.getInstance().getRandomColor());

                LinearLayout.LayoutParams secondParams = (LayoutParams) secondLayout.getLayoutParams();
                secondParams.width = 0;
                secondParams.weight = mSecondRatios[i][j];
                secondParams.height = LayoutParams.MATCH_PARENT;

                if (j > 0) {
                    secondParams.leftMargin = DensityUtils.dp2px(context, 3);
                }

                secondLayout.setLayoutParams(secondParams);
            }
        }
    }

    public LinearLayout getChild(int i, int j) {
        LinearLayout linearLayout = (LinearLayout) ((LinearLayout) getChildAt(i)).getChildAt(j);

        return linearLayout;
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
