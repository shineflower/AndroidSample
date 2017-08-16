package com.jackie.sample.newer_guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jackie.sample.R;
import com.jackie.sample.newer_guide.view.Guide;
import com.jackie.sample.newer_guide.view.GuideBuilder;

/**
 * Created by Jackie on 2017/8/14.
 */

public class NewerGuideActivity extends AppCompatActivity {
    private Button mHeaderImgBtn;
    private Guide mGuide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newer_guide);

        mHeaderImgBtn = (Button) findViewById(R.id.header_img_btn);

        mHeaderImgBtn.post(new Runnable() {
            @Override
            public void run() {
                showGuideView();
            }
        });
    }

    private void showGuideView() {
        GuideBuilder guideBuilder = new GuideBuilder();
        guideBuilder.setTargetView(mHeaderImgBtn)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);

        guideBuilder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override public void onShown() {
            }

            @Override public void onDismiss() {
            }
        });

        guideBuilder.addComponent(new SimpleComponent());

        mGuide = guideBuilder.createGuide();
        mGuide.setShouldCheckLocInWindow(true);
        mGuide.show(this);
    }
}
