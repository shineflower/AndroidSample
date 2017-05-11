package com.jackie.sample.flow_layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.FlowLayout;

/**
 * Created by Jackie on 2017/5/11.
 * 流式布局
 */

public class FlowLayoutActivity extends Activity {
    private FlowLayout mFlowLayout;

    String[] mTexts = { "小米4s小米电源", "Button ImageView", "TextView", "HelloWorld", "割双眼皮", "Welcome Hi ",
            "好一个单身汪", "TextView", "多彩多姿", "旁流水处理器", " 股票周末开户", "Button ImageView",
            "TextView", "HelloWorld", "Button ImageView", "TextView", "HelloWorld",
            " 内衣加盟李宁T恤", "Button ImageView", "TextView", "HelloWorld", "双眼皮埋线不雅视频",
            "Button Text", "吴莫愁捉妖记彩蛋", "令计划之弟潜逃美国", "哈林指甲发黑掉落~#$%$%^*" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flow_layout);

        mFlowLayout = (FlowLayout) findViewById(R.id.flow_layout);

        Button button;
        for (int i = 0; i < mTexts.length; i++) {
            button = new Button(this);

            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 15;
            params.topMargin = 15;
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_flow_text_view_bg));
            button.setLayoutParams(params);
            button.setText(mTexts[i]);
            mFlowLayout.addView(button);
        }
    }
}
