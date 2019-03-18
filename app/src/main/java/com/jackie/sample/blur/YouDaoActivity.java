package com.jackie.sample.blur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.CircleDrawable;
import com.jackie.sample.utils.BitmapUtils;

/**
 * Created by Jackie on 2019/3/18.
 */
public class YouDaoActivity extends AppCompatActivity {
    private FrameLayout mHeadLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_you_dao);

        mHeadLayout = (FrameLayout) findViewById(R.id.fl_head);
        ImageView headImg = (ImageView) findViewById(R.id.iv_head);

        // 耗时较长，最好放在子线程中
        initAvatarBackground();

        headImg.setImageDrawable(new CircleDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.small)));
    }

    private void initAvatarBackground() {
        Drawable drawable = getResources().getDrawable(R.drawable.rocko);
        Bitmap srcBitmap = BitmapUtils.drawable2Bitmap(drawable);

        /*先黑白图片*/
        float[] src = new float[]{
                0.28F, 0.60F, 0.40F, 0, 0,
                0.28F, 0.60F, 0.40F, 0, 0,
                0.28F, 0.60F, 0.40F, 0, 0,
                0, 0, 0, 1, 0,
        };

        ColorMatrix colorMatrix = new ColorMatrix(src);
//        colorMatrix.setSaturation(0.0f);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);

        Bitmap resultBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(resultBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(100);
        paint.setColorFilter(colorMatrixColorFilter);

        canvas.drawBitmap(srcBitmap, 0, 0, paint);

        /*后模糊图片*/
        Bitmap blurBitmap = BitmapUtils.blurBitmap(getApplicationContext(), resultBitmap, 15.5f);

        mHeadLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), blurBitmap));
    }
}
