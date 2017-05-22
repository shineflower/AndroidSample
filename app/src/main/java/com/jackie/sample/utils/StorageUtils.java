package com.jackie.sample.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Jackie on 2017/5/22.
 * 文件存储的工具类
 */

public class StorageUtils {
    /**
     *文件存储根目录
     */
    public static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = context.getExternalFilesDir(null);
            if (file != null) {
                return file.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }
}
