package com.jackie.sample.wechat_camera.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jackie on 2017/6/8.
 */

public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final File mParentPath = Environment.getExternalStorageDirectory();
    private static String mStoragePath = "";
    private static String DST_FOLDER_NAME = "JCamera";

    private static String initPath() {
        if (mStoragePath.equals("")) {
            mStoragePath = mParentPath.getAbsolutePath() + File.separator + DST_FOLDER_NAME;
            File f = new File(mStoragePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }

        return mStoragePath;
    }

    public static String saveBitmap(Bitmap b) {
        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + File.separator + "picture_" + dataTake + ".jpg";

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return jpegName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }
}
