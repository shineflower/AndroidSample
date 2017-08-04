package com.jackie.sample.framework.fresco;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/5/24.
 */

public class FrescoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mShowButton;
    private SimpleDraweeView mSimpleDraweeView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco);

        mShowButton = (Button) findViewById(R.id.show_image);
        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.image_view);

        mShowButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_image:

                /*
                   显示占位图直到加载完成；
                   下载图片；
                   缓存图片；
                   图片不再显示时，从内存中移除；
                */
                Uri uri = Uri.parse("http://pic.toomao.com/bd90075f0e05997ac305d0e358125be344b64a47");
//                Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/fresco-logo.png");
                // SimpleDraweeView必须设置图片的宽高，不能是wrap_content，不然加载不出来，这里有点坑
                mSimpleDraweeView.setImageURI(uri);
                break;
        }
    }
}
