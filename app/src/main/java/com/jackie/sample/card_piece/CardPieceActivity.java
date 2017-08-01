package com.jackie.sample.card_piece;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.VerticalCardPieceLayout;
import com.jackie.sample.utils.CommonUtils;
import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/7/18.
 */

public class CardPieceActivity extends AppCompatActivity implements View.OnClickListener {
    private VerticalCardPieceLayout mCardPieceLayout;
    private Button mSplitMergeRightCorner, mSplitMergeMiddle;

    private boolean mIsSplit = false; //是否已经拆分

    private static final float[] SECOND_RATIOS = { 0.3f, 0.7f };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_piece);

        initView();
        initEvent();
    }

    private void initView() {
        mCardPieceLayout = (VerticalCardPieceLayout) findViewById(R.id.card_piece_layout);
        mSplitMergeRightCorner = (Button) findViewById(R.id.split_merge_right_corner);
        mSplitMergeMiddle = (Button) findViewById(R.id.split_merge_middle);
    }

    private void initEvent() {
        mSplitMergeRightCorner.setOnClickListener(this);
        mSplitMergeMiddle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LinearLayout firstLayout;

        switch (v.getId()) {
            case R.id.split_merge_right_corner:
                firstLayout = mCardPieceLayout.getChild(0, 3);
                firstLayout.setBackgroundColor(Color.WHITE);    //保证间距是白色的

                if (!mIsSplit) {
                    split(firstLayout);
                } else {
                    merge(firstLayout);
                }
                break;
            case R.id.split_merge_middle:
                firstLayout = mCardPieceLayout.getChild(1, 1);

                if (!mIsSplit) {
                    split(firstLayout, 0.3f);
                } else {
                    merge(firstLayout, 0.3f);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 拆分
     * @param firstLayout
     */
    private void split(LinearLayout firstLayout) {
        //垂直裂变
        firstLayout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < SECOND_RATIOS.length; i++) {
            LinearLayout secondLayout = new LinearLayout(this);
            firstLayout.addView(secondLayout);

            secondLayout.setBackgroundColor(CommonUtils.getInstance().getRandomColor());

            LinearLayout.LayoutParams secondParams = (LinearLayout.LayoutParams) secondLayout.getLayoutParams();
            secondParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            secondParams.height = 0;
            secondParams.weight = SECOND_RATIOS[i];

            if (i > 0) {
                secondParams.topMargin = DensityUtils.dp2px(this, 3);
            }

            secondLayout.setLayoutParams(secondParams);
        }

        mIsSplit = true;
    }

    /**
     * 拆分，合并
     * @param firstLayout
     * @param radio
     */
    private void split(LinearLayout firstLayout, float radio) {
        LinearLayout leftLayout = mCardPieceLayout.getChild(1, 0);
        LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) leftLayout.getLayoutParams();
        leftParams.weight -= radio;
        leftLayout.setLayoutParams(leftParams);

        LinearLayout.LayoutParams firstParams = (LinearLayout.LayoutParams) firstLayout.getLayoutParams();
        firstParams.weight += radio;
        firstLayout.setLayoutParams(firstParams);

        mIsSplit = true;
    }

    private void merge(LinearLayout firstLayout) {
        firstLayout.removeAllViews();

        firstLayout.setBackgroundColor(CommonUtils.getInstance().getRandomColor());

        mIsSplit = false;
    }

    private void merge(LinearLayout firstLayout, float radio) {
        LinearLayout leftLayout = mCardPieceLayout.getChild(1, 0);
        LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) leftLayout.getLayoutParams();
        leftParams.weight += radio;
        leftLayout.setLayoutParams(leftParams);

        LinearLayout.LayoutParams firstParams = (LinearLayout.LayoutParams) firstLayout.getLayoutParams();
        firstParams.weight -= radio;
        firstLayout.setLayoutParams(firstParams);

        mIsSplit = false;
    }
}
