package com.jackie.sample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by Jackie on 2017/1/21.
 * 高斯模糊工具类
 */

public class BlurUtils {
    /**
     * 图片缩放比例
     */
    private static final float SCALE_DEGREE = 0.4f;
    /**
     * 最大模糊度（在0.0到25.0之间）
     */
    private static final float BLUR_RADIUS = 25f;

    /**
     * 模糊图片
     * @param context   上下文
     * @param bitmap    需要模糊的图片
     * @return          模糊处理后的图片
     */
    public static Bitmap blur(Context context,Bitmap bitmap) {
        //计算图片缩小的长宽
        int width = Math.round(bitmap.getWidth() * SCALE_DEGREE);
        int height = Math.round(bitmap.getHeight() * SCALE_DEGREE);

        //将缩小后的图片作为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        //创建一张渲染后的输入图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        //创建RenderScript内核对象
        RenderScript renderScript = RenderScript.create(context);
        //创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        /**
         * 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
         * 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
         */
        Allocation inputAllocation = Allocation.createFromBitmap(renderScript, inputBitmap);
        Allocation outputAllocation = Allocation.createFromBitmap(renderScript, outputBitmap);

        //设置渲染的模糊程度，25f是最大模糊度
        scriptIntrinsicBlur.setRadius(BLUR_RADIUS);
        //设置ScriptIntrinsicBlur对象的输入内存
        scriptIntrinsicBlur.setInput(inputAllocation);
        //将ScriptIntrinsicBlur输出数据保存到输出内存中
        scriptIntrinsicBlur.forEach(outputAllocation);

        //将数据填充到Allocation中
        outputAllocation.copyTo(outputBitmap);

        return outputBitmap;
    }
}