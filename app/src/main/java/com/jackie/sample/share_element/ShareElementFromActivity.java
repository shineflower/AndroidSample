package com.jackie.sample.share_element;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.transition.Explode;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.jackie.sample.R;

/**
 * 共享元素的界面跳转
 */
public class ShareElementFromActivity extends Activity {
    private ImageView mImageView;
    private Context mContext;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_element_from);
        mImageView = (ImageView)findViewById(R.id.image_element);

        mContext = this;
        mActivity = this;

        ViewCompat.setTransitionName(mImageView, "_XXX");

        getWindow().setEnterTransition(new Explode());
//        getWindow().setEnterTransition(new Slide());
//        getWindow().setEnterTransition(new Fade());

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, ShareElementToActivity.class);
                Pair pair = new Pair(mImageView, "XXX");
                if (android.os.Build.VERSION.SDK_INT > 20) {//手机版本是20以上的时候。
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mActivity, pair).toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
    }
}
