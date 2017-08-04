package com.jackie.sample.text_view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.jackie.sample.R;

public class TextViewActivity extends AppCompatActivity {
    private TextView mTextView1, mTextView2, mTextView3, mTextView4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);

//        TextView textView = new TextView(this);
//        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        textView.setMarqueeRepeatLimit(-1);
//        textView.setSingleLine();

        mTextView1 = (TextView) findViewById(R.id.text_view_1);
        mTextView2 = (TextView) findViewById(R.id.text_view_2);
        mTextView3 = (TextView) findViewById(R.id.text_view_3);
        mTextView4 = (TextView) findViewById(R.id.text_view_4);

        //两次加大字体，设置字体为红色(big会加大字号，font可以定义颜色)
        mTextView1.setText(Html.fromHtml("北京市发布霾黄色预警， <font color='#ff0000'><big><big>外出携带好</big></big></font>口罩"));

        //设置字体大小为3级标题，设置字体为红色
        mTextView2.setText(Html.fromHtml("北京市发布霾黄色预警，<h3><font color='#ff0000'>外出携带好</font></h3>口罩"));

        //设置字体大小为58(单位为物理像素)，设置字体为红色，字体背景为红色
        mTextView3.setText("北京市发布霾黄色预警，外出携带好口罩");
        Spannable spannable = new SpannableString(mTextView3.getText());
        spannable.setSpan(new AbsoluteSizeSpan(58), 11, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.RED), 11, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), 11, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView3.setText(spannable);

        //两次缩小字体，设置字体为红色(small可以减少字号)
        mTextView4.setText(Html.fromHtml("北京市发布霾黄色预警，<font color='#ff0000'><small><small>外出携带好</small></small></font>口罩"));
    }
}
