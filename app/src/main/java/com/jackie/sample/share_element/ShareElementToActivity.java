package com.jackie.sample.share_element;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.view.Window;

import com.jackie.sample.R;

public class ShareElementToActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);


        getWindow().setSharedElementEnterTransition(new ChangeImageTransform(this, null));
        getWindow().setSharedElementExitTransition(new ChangeImageTransform(this, null));

        getWindow().setExitTransition(new Explode());
//        getWindow().setExitTransition(new Slide());
//        getWindow().setExitTransition(new Fade());
        setContentView(R.layout.activity_share_element_to);
    }
}
