package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2016/2/29.
 */
public class MeshView extends View {
    private Bitmap mBitmap;

    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    private static final int COUNT = (WIDTH + 1) * (HEIGHT + 1);

    private float[] verts = new float[COUNT * 2];
    private float[] origs = new float[COUNT * 2];
    private float k = 1;

    public MeshView(Context context) {
        this(context, null);
    }

    public MeshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test4);

        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        //初始化
        int index = 0;
        for (int i = 0; i < HEIGHT + 1; i++) {
            int py = height * i / HEIGHT;
            for (int j = 0; j < WIDTH + 1; j++) {
                int px = width * j / WIDTH;
                //用一维数组来存储一个点的坐标
                verts[index * 2 + 0] = origs[index * 2 + 0] = px;
                verts[index * 2 + 1] = origs[index * 2 + 1] = py + 300;
                index++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int index = 0;
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {
//                verts[(i * (WIDTH + 1) + j) * 2 + 0] += 0;
                verts[index * 2 + 0] += 0;
                float offsetY = (float) Math.sin((float) j / WIDTH * 2 * Math.PI + k * Math.PI);
//                verts[(i * (WIDTH + 1) + j) * 2 + 1] = origs[(i * (WIDTH + 1) + j) * 2 + 1] + offsetY * 50;
                verts[index * 2 + 1] = origs[index * 2 + 1] + offsetY * 50;
                index++;
            }
        }

        k += 0.1f;
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        invalidate();
    }
}
