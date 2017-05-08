package com.jackie.sample.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by Jackie on 2016/2/25.
 * 图像处理类
 */
public class ImageHelper {

    //色光三原色 色相、饱和度、亮度调节
    public static Bitmap handleImageEffect(Bitmap source, float hue, float saturation, float lum) {
        Bitmap destination = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(destination);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        ColorMatrix hueColorMatrix = new ColorMatrix();
        hueColorMatrix.setRotate(0, hue);
        hueColorMatrix.setRotate(1, hue);
        hueColorMatrix.setRotate(2, hue);

        ColorMatrix saturationColorMatrix = new ColorMatrix();
        saturationColorMatrix.setSaturation(saturation);

        ColorMatrix lumColorMatrix = new ColorMatrix();
        lumColorMatrix.setScale(lum, lum, lum, 1);

        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueColorMatrix);
        imageMatrix.postConcat(saturationColorMatrix);
        imageMatrix.postConcat(lumColorMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(source, 0, 0, paint);

        return destination;
    }

    //底片效果
    public static Bitmap handleImagePixelsNegative(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap destination = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int[] srcPixels = new int[width * height];
        source.getPixels(srcPixels, 0, width, 0, 0, width, height);

        int[] dstPixels = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int pixel = srcPixels[i];

            int a = Color.alpha(pixel);
            int r = Color.red(pixel);
            int g = Color.green(pixel);
            int b = Color.blue(pixel);

            //底片效果算法
            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }

            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }

            //将r g  b 组合成新的颜色
            dstPixels[i] = Color.argb(a, r, g, b);
        }

        destination.setPixels(dstPixels, 0, width, 0, 0, width, height);
        return destination;
    }

    //老照片怀旧效果
    public static Bitmap handleImagePixelsOldPhoto(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap destination = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int[] srcPixels = new int[width * height];
        source.getPixels(srcPixels, 0, width, 0, 0, width, height);

        int[] dstPixels = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int pixel = srcPixels[i];

            int a = Color.alpha(pixel);
            int r = Color.red(pixel);
            int g = Color.green(pixel);
            int b = Color.blue(pixel);

            //底片效果算法
            r = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            g = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            b = (int) (0.272 * r + 0.534 * g + 0.131 * b);

            if (r > 255) {
                r = 255;
            }

            if (g > 255) {
                g = 255;
            }

            if (b > 255) {
                b = 255;
            }

            //将r g  b 组合成新的颜色
            dstPixels[i] = Color.argb(a, r, g, b);
        }

        destination.setPixels(dstPixels, 0, width, 0, 0, width, height);
        return destination;
    }

    //浮雕效果
    public static Bitmap handleImagePixelsRelief(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap destination = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int[] srcPixels = new int[width * height];
        source.getPixels(srcPixels, 0, width, 0, 0, width, height);

        int[] dstPixels = new int[width * height];
        for (int i = 1; i < width * height; i++) {
            int beforePixel = srcPixels[i - 1];
            int currentPixel = srcPixels[i];

            int beforeR = Color.red(beforePixel);
            int beforeG = Color.green(beforePixel);
            int beforeB = Color.blue(beforePixel);

            int currentA = Color.alpha(currentPixel);
            int currentR = Color.red(currentPixel);
            int currentG = Color.green(currentPixel);
            int currentB = Color.blue(currentPixel);

            //浮雕效果算法
            currentR =  currentR - beforeR + 127;
            currentG = currentG - beforeG + 127;
            currentB = currentB - beforeB + 127;

            if (currentR > 255) {
                currentR = 255;
            }

            if (currentG > 255) {
                currentG = 255;
            }

            if (currentB > 255) {
                currentB = 255;
            }

            //将r g  b 组合成新的颜色
            dstPixels[i] = Color.argb(currentA, currentR, currentG, currentB);
        }

        destination.setPixels(dstPixels, 0, width, 0, 0, width, height);
        return destination;
    }
}
