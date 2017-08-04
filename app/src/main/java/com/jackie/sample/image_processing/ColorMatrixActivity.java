package com.jackie.sample.image_processing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2016/2/25.
 */
public class ColorMatrixActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageView;
    private Bitmap mBitmap;

    private GridLayout mGridLayout;
    private Button mChangeButton;
    private Button mResetButton;

    private int mWidth, mHeight;
    private EditText[] mEditTexts = new EditText[20];
    private float[] mColorMatrix = new float[20];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_matrix);

        initView();
        initEvent();

        mGridLayout.post(new Runnable() {
            @Override
            public void run() {
                mWidth = mGridLayout.getWidth() /  5;
                mHeight = mGridLayout.getHeight() / 4;

                initGridLayout();
                initMatrix();
            }
        });
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.source);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        mImageView.setImageBitmap(mBitmap);

        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        mChangeButton = (Button) findViewById(R.id.change);
        mResetButton = (Button) findViewById(R.id.reset);
    }

    private void initEvent() {
        mChangeButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
    }

    private void initGridLayout() {
        for (int i = 0; i < 20; i++) {
            EditText editText = new EditText(this);
            editText.setGravity(Gravity.CENTER);
            editText.setSingleLine(true);
            mEditTexts[i] = editText;
            mGridLayout.addView(editText, mWidth, mHeight);
        }
    }

    //初始化初始矩阵
    private void initMatrix() {
        for (int i = 0; i < 20; i++) {
            if (i % 6 == 0) {
                mEditTexts[i].setText(String.valueOf(1));
            } else {
                mEditTexts[i].setText(String.valueOf(0));
            }
        }
    }

    private void getMatrix() {
        for (int i = 0; i < 20; i++) {
            mColorMatrix[i] = Float.valueOf(mEditTexts[i].getText().toString());
        }
    }

    private void setMatrix() {
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change:
                getMatrix();
                setMatrix();
                break;
            case R.id.reset:
                initMatrix();
                getMatrix();
                setMatrix();
                break;
        }
    }
}
