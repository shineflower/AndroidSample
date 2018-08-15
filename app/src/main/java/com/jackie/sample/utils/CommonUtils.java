package com.jackie.sample.utils;

import android.graphics.Color;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Random;

/**
 * Created by Jackie on2017/3/6
 */
public class CommonUtils {
    private static CommonUtils mInstance;

    public static CommonUtils getInstance() {
        if (mInstance == null) {
            synchronized (CommonUtils.class) {
                if (mInstance == null) {
                    mInstance = new CommonUtils();
                }
            }
        }

        return mInstance;
    }

    long mLastClickTime = 0;

    public boolean isNotFastClick() {
        long time = System.currentTimeMillis();
        if (time - mLastClickTime >= 300) {
            mLastClickTime = time;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 随机颜色
     * Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
     */
    public int getRandomColor() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        String hexString;
        for (int i = 0; i < 3; i++) {
            hexString = Integer.toHexString(random.nextInt(0xFF));
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }

            stringBuilder.append(hexString);
        }

        return Color.parseColor("#" + stringBuilder.toString());
    }

    /**
     * 深拷贝就是将A复制给B的同时，给B创建新的地址，再将地址A的内容传递到地址B。
     * ListA与ListB内容一致，但是由于所指向的地址不同，所以改变相互不受影响。
     * 使用序列化方法，必须保证List里面的实体实现了Serializable接口
     * https://blog.csdn.net/demonliuhui/article/details/54572908
     * @param src                       src
     * @param <T>                       dest
     * @return                          list
     * @throws IOException              IOException
     * @throws ClassNotFoundException   ClassNotFoundException
     */
    private <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }
}
