package com.jackie.sample.skin_support;

import android.content.Context;

import java.io.File;

import skin.support.load.SkinSDCardLoader;
import skin.support.utils.SkinFileUtils;

/**
 * 自定义策略
 */
public class CustomSDCardLoader extends SkinSDCardLoader {
    public static final int SKIN_LOADER_STRATEGY_SDCARD = Integer.MAX_VALUE;

    @Override
    protected String getSkinPath(Context context, String skinName) {
        //自定义目录
//        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "skins", skinName);
//        return file.getAbsolutePath();
        //默认目录
        return new File(SkinFileUtils.getSkinDir(context), skinName).getAbsolutePath();
    }

    @Override
    public int getType() {
        return SKIN_LOADER_STRATEGY_SDCARD;
    }
}