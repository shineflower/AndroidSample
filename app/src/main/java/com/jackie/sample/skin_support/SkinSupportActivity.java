package com.jackie.sample.skin_support;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jackie.sample.R;

import skin.support.SkinCompatManager;

/**
 * Created by Jackie on 2017/12/7.
 * 换肤
 * https://github.com/ximsfei/Android-skin-support
 */

public class SkinSupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_support);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, 1, "插件式换肤");
        menu.add(Menu.NONE, Menu.FIRST + 1, 2, "自定义换肤");
        menu.add(Menu.NONE, Menu.FIRST + 2, 3, "恢复");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                // 插件加载策略  night.skin必须放在assets/skin目录下，所用的颜色，必须与skin的命名保持完全一致
                SkinCompatManager.getInstance().loadSkin("night.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                break;
            case Menu.FIRST + 1:
                /**
                 * 如果assets/skin有皮肤文件
                 * 在assets/skins的皮肤文件在换肤成功后，会自动复制到/storage/emulated/0/Android/data/com.jackie.sample(包名)/cache/skins下
                 * 如果assets/skin没有皮肤文件
                 * /storage/emulated/0/Android/data/com.jackie.sample(包名)/cache/skins 就是个空目录
                 *
                 * 所以，如果要自定义目录，assets/skin下不能有皮肤文件，皮肤文件可以放在除了cache目录下的任何目录
                 * 相应需要改下CustomSDCardLoader的代码
                 *
                 * SDCard加载策略 可以自定义路径
                 * 所用的颜色，必须与skin的命名保持完全一致
                 */
                SkinCompatManager.getInstance().loadSkin("night.skin", null, CustomSDCardLoader.SKIN_LOADER_STRATEGY_SDCARD);
                break;
            case Menu.FIRST + 2:
                // 恢复应用默认皮肤
                SkinCompatManager.getInstance().restoreDefaultTheme();
            default:
                break;
        }

        return true;
    }
}
