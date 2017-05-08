package com.jackie.sample.image_processing;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ImageMatrixView;

/**
 * Created by Jackie on 2016/2/26.
 */
public class MatrixActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageMatrixView mMatrixView;
    private GridLayout mGridLayout;
    private Button mChangeButton;
    private Button mResetButton;

    private int mWidth, mHeight;
    private EditText[] mEditTexts = new EditText[9];
    private float[] mMatrix = new float[9];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);

        initView();
        initEvent();

        mGridLayout.post(new Runnable() {
            @Override
            public void run() {
                mWidth = mGridLayout.getWidth() / 3;
                mHeight = mGridLayout.getHeight() / 3;

                initGridLayout();
                initMatrix();
            }
        });
    }

    private void initView() {
        mMatrixView = (ImageMatrixView) findViewById(R.id.matrix_view);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);
        mChangeButton = (Button) findViewById(R.id.change);
        mResetButton = (Button) findViewById(R.id.reset);
    }

    private void initEvent() {
        mChangeButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
    }

    private void initGridLayout() {
        for (int i = 0; i < 9; i++) {
            EditText editText = new EditText(this);
            editText.setGravity(Gravity.CENTER);
            editText.setSingleLine(true);
            mEditTexts[i] = editText;
            mGridLayout.addView(editText, mWidth, mHeight);
        }
    }

    private void initMatrix() {
        for (int i = 0; i < 9; i++) {
            if (i % 4 == 0) {
                mEditTexts[i].setText(String.valueOf(1));
            } else {
                mEditTexts[i].setText(String.valueOf(0));
            }
        }
    }

    private void getMatrix() {
        for (int i = 0; i < 9; i++) {
            mMatrix[i] = Float.valueOf(mEditTexts[i].getText().toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change:
                getMatrix();
                break;
            case R.id.reset:
                initMatrix();
                getMatrix();
                break;
        }

        Matrix matrix = new Matrix();
        matrix.setValues(mMatrix);
//        matrix.setTranslate(100, 100);
//        matrix.setRotate(60);
//        matrix.setScale(2, 2);
//        matrix.setSkew();
//        matrix.postTranslate(200, 200);

        mMatrixView.setImageMatrix(matrix);
    }
}
