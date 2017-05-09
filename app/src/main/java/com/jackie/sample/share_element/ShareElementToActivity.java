package com.jackie.sample.share_element;

import android.app.Activity;
import android.os.Bundle;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.view.Window;

import com.jackie.sample.R;

public class ShareElementToActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
