package com.jackie.sample.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by Jackie on 2017/7/17.
 * 文件操作相关工具类
 */

public class FileUtils {
    private static String SDCARD_PATH =  Environment.getExternalStorageDirectory() + File.separator;

    private static String MAIN_DIR = "AndroidSample";

    private static String mApkPath = getSdcardPath() + MAIN_DIR + File.separator + "apks" + File.separator;
    private static String mImagePath =  getSdcardPath() + MAIN_DIR + File.separator + "images" + File.separator;
    private static String mRecordPath = getSdcardPath() + MAIN_DIR + File.separator + "records" + File.separator;
    private static String mVideoPath =  getSdcardPath() + MAIN_DIR + File.separator + "videos" + File.separator;

    // 得到当前SD卡的路径
    public  static String getSdcardPath() {
        return SDCARD_PATH;
    }

    // SD卡上创建一个文件夹
    public static File createSDDir(String dirName) {
        File dir = new File(SDCARD_PATH + dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    // 判断SD卡上文件或文件夹是否存在
    public static boolean isFileExist(String fileName) {
        File file = new File(SDCARD_PATH + fileName);
        return file.exists();
    }

    // 得到文件
    public static File getFile(String fileName) {
        return new File(SDCARD_PATH + fileName);
    }

    public static File getApkPath() {
        File file = new File(mApkPath);

        if (file.exists()) {
            return file;
        } else {
            file.mkdirs();
            return file;
        }
    }

    /**
     * 录音文件夹
     */
    public static File getRecordPath(){
        File file = new File(mRecordPath);

        if (file.exists()) {
            return file;
        } else {
            file.mkdirs();
            return file;
        }
    }

    public File getApkFile(String name) {
        File file = new File(mApkPath,name);

        if (file.exists()) {
            return file;
        } else {
            file.getParentFile().mkdirs();
            return file;
        }
    }

    public File getImagesFile(String fileName) {
        File file = new File(mImagePath,fileName);

        if (file.exists()) {
            return file;
        }else {
            file.getParentFile().mkdirs();
            return file;
        }

    }

    public File getRecordFile(String name){
        File file = new File(mRecordPath, name);

        if (file.exists()) {
            return file;
        } else {
            file.getParentFile().mkdirs();
            return file;
        }
    }
    /**
     * 获取视频的存储位置
     * @param name
     * @return
     */
    public static File getVideoFile(String name){
        File file = new File(mVideoPath , name);

        if (file.exists()) {
            return file;
        } else {
            file.getParentFile().mkdirs();
            return file;
        }
    }

    public static boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
        File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(destFileName);

        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteRead; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteRead);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void nioTransferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;

        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                in.close();
                outStream.close();
                out.close();
            } catch (Exception e) {

            }
        }
    }

    // 传入一个大小，判断在sdcard上是否放得下
    @TargetApi(18)
    @SuppressLint("NewApi")
    public boolean isAvailableSize(String size) {
        boolean flag = true;

        try {
            File file = new File(SDCARD_PATH);
            StatFs statfs = new StatFs(file.getPath());
            long nBlocSize;
            long nAvailableBlock;

            int version = android.os.Build.VERSION.SDK_INT;

            if (version < 18) {
                nBlocSize = statfs.getBlockSize();
                nAvailableBlock = statfs.getAvailableBlocks();
            } else {
                nBlocSize = statfs.getBlockSizeLong();
                nAvailableBlock = statfs.getAvailableBlocksLong();
            }

            long leftSize = nBlocSize * nAvailableBlock / 1024 / 1024;  // mb

            size = size.replace("MB", "").replace("mb", "").replace("Mb", "").replace("mB", "");

            if (size.contains(".")) {
                size = size.substring(0, size.indexOf("."));
            }

            int s = (Integer.parseInt(size) + 1) * 2;

            flag = leftSize - s > 0;
        } catch (Exception e) {
        }

        return flag;
    }

    // 递归删除文件夹里面的东西
    public void deleteDirs(File file) {
        if(file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();

            for(File f : files) {
                deleteDirs(f); //递归删除每一个文件
                f.delete(); //删除该文件夹
            }
        }
    }

    // 清除缓存，但不删除文件夹(四个文件夹的清空)
    public void cleanCache() {
        try {
            File file = new File(mApkPath);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }

            file = new File(mImagePath);
            files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }

            file = new File(mRecordPath);
            files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }

            file = new File(mVideoPath);
            files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        } catch (Exception e) {

        }
    }

    /**
     * save image to gallery
     * @param context
     * @param bitmap
     */
    public  static void saveImageToGallery(Context context, Bitmap bitmap) {
        // 首先保存图片
        File appDir = new File(mImagePath);

        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String path = mImagePath + fileName;

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }

    /** Gets the corresponding path from content:// URI
     * @param context     content
     * @param contentUri  content uri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String result = null;

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(column_index);
        }

        cursor.close();

        return result;
    }

    /**
     * Gets the content:// URI from the given corresponding path to a file
     * @param context       content
     * @param imageFile     imageFile
     * @return              content uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, id + "");
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
