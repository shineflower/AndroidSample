package com.jackie.sample.falling;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.jackie.sample.utils.RandomUtils;


/**
 * Created by Jackie on 2017/7/5.
 * 碎片
 */

public class Flake {
    private static RandomUtils mRandomUtils = new RandomUtils();

    private Point mPoint;
    private float mAngle;
    private float mIncrement;
    private float mFlakeSize;
    private Paint mPaint;

    private static final float ANGLE_RANGE = 0.1f;
    private static final float HALF_ANGLE_RANGE = ANGLE_RANGE / 2f;
    private static final float HALF_PI = (float) Math.PI / 2f;
    private static final float ANGLE_SEED = 25f;
    private static final float ANGLE_DIVISOR = 10000f;
    private static final float INCREMENT_LOWER = 2f;
    private static final float INCREMENT_UPPER = 4f;

    public Flake(Point position, float angle, float increment, int flakeSize, Paint paint) {
        this.mPoint = position;
        this.mAngle = angle;
        this.mIncrement = increment;
        this.mFlakeSize = flakeSize;
        this.mPaint = paint;
    }

    public static Flake create(int width, int height, Paint paint, int flakeSize){
        int x = mRandomUtils.getRandom(width);
        int y = mRandomUtils.getRandom(height);

        Point position = new Point(x, y);
        float angle = mRandomUtils.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGLE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
        float increment = mRandomUtils.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);

        return new Flake(position, angle, increment, flakeSize, paint);
    }

    public void draw(Canvas canvas, Bitmap flakeBitmap) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        move(width, height);
        canvas.drawBitmap(flakeBitmap, mPoint.x, mPoint.y, mPaint);
    }

    private void move(int width, int height) {
        double x = mPoint.x + (mIncrement * Math.cos(mAngle));
        double y = mPoint.y + (mIncrement * Math.sin(mAngle));

        mAngle += mRandomUtils.getRandom(-ANGLE_SEED, ANGLE_SEED) / ANGLE_DIVISOR;
        mPoint.set((int) x, (int) y);

        if (!isInside(width, height)) {
            reset(width);
        }
    }

    private boolean isInside(int width, int height) {
        int x = mPoint.x;
        int y = mPoint.y;

        return x >= -mFlakeSize - 1 && x + mFlakeSize <= width && y >= -mFlakeSize - 1 && y - mFlakeSize < height;
    }

    private void reset(int width) {
        mPoint.x = mRandomUtils.getRandom(width);
        mPoint.y = (int) (-mFlakeSize - 1);

        mAngle = mRandomUtils.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGLE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
    }
}
