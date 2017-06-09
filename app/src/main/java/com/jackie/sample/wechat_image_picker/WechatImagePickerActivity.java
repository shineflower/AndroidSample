package com.jackie.sample.wechat_image_picker;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.adapter.ImageAdapter;
import com.jackie.sample.bean.ImageFolder;
import com.jackie.sample.custom_view.DirectoryPopupWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WechatImagePickerActivity extends AppCompatActivity {
    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    private List<String> mImagePathList;

    private RelativeLayout mBottomLayout;
    private TextView mDirectoryName;
    private TextView mDirectoryCount;

    private File mCurrentDirectory;
    private int mMaxCount;

    private List<ImageFolder> mImageFolderList = new ArrayList<>();

    private ProgressDialog mProgressDialog;

    private DirectoryPopupWindow mPopupWindow;

    private static final int MESSAGE_SCAN_SUCCEED = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCAN_SUCCEED:
                    mProgressDialog.dismiss();
                    //为View绑定数据
                    data2View();

                    initPopupWindow();
                    break;
            }
        }
    };

    private void initPopupWindow() {
        mPopupWindow = new DirectoryPopupWindow(this, mImageFolderList);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mPopupWindow.setOnDirectorySelectedListener(new DirectoryPopupWindow.OnDirectorySelectedListener() {
            @Override
            public void onSelected(ImageFolder imageFolder) {

                mCurrentDirectory = new File(imageFolder.getDirectory());
                int imageCount  = mCurrentDirectory.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".png") || filename.toLowerCase().endsWith(".jpeg"))
                            return true;
                        return false;
                    }
                }).length;

                /**
                   1、数据源没有更新，调用notifyDataSetChanged无效。
                   2、数据源更新了，但是它指向新的引用，调用notifyDataSetChanged无效。
                   3、数据源更新了，但是adapter没有收到消息通知，无法动态更新列表。
                 */
                //调用Arrays.asList相当重新创建了一个List，引用已经变了，所以没有刷新
                //mImagePathList = Arrays.asList(mCurrentDirectory.list());
                //将所有mCurrentDirectory目录下的所有元素都添加到mImagePathList中
                /**
                 * Arrays.asList() 返回java.util.Arrays.ArrayList，而不是java.util.ArrayList。
                 * Arrays.ArrayList和ArrayList都是继承AbstractList。
                 * remove和add等method在AbstractList中是默认throw UnsupportedOperationException而且不作任何操作。
                 * ArrayList override这些method来对list进行操作。
                 * 但是Arrays.ArrayList没有override remove(int)，add(int)等，所以throw UnsupportedOperationException。
                 */

                mImagePathList.clear();
                mImageAdapter.setDirectoryPath(mCurrentDirectory.getAbsolutePath());
                mImagePathList.addAll(Arrays.asList(mCurrentDirectory.list()));
                mImageAdapter.notifyDataSetChanged();

                mDirectoryName.setText(imageFolder.getName());
                mDirectoryCount.setText(imageCount + "");

                mPopupWindow.dismiss();
            }
        });
    }

    /**
     * 将内容区域变亮
     */
    private void lightOn() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1.0f;
        getWindow().setAttributes(params);
    }

    /**
     * 将内容区域变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.3f;
        getWindow().setAttributes(params);
    }

    private void data2View() {
        if (mCurrentDirectory == null) {
            Toast.makeText(this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        }

        //将Arrays.ArrayList转化成ArrayList
        mImagePathList = new ArrayList<>(Arrays.asList(mCurrentDirectory.list()));
        mImageAdapter = new ImageAdapter(this, mImagePathList, mCurrentDirectory.getAbsolutePath());
        mGridView.setAdapter(mImageAdapter);

        mDirectoryName.setText(mCurrentDirectory.getName());
        mDirectoryCount.setText(mMaxCount + "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_image_picker);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.gridview_image);
        mBottomLayout = (RelativeLayout) findViewById(R.id.layout_bottom);
        mDirectoryName = (TextView) findViewById(R.id.directory_name);
        mDirectoryCount = (TextView) findViewById(R.id.directory_count);
    }

    /**
     * 利用ContentProvider扫描手机中的所有图片
     */
    private void initData() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "存储卡不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread() {
            @Override
            public void run() {
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor = getContentResolver().query(imageUri, null, MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> directorySet = new HashSet<>();
                while(cursor.moveToNext()) {
                    //获取图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;

                    String parentPath = parentFile.getAbsolutePath();

                    ImageFolder imageFolder;
                    if (directorySet.contains(parentPath)) {
                        continue;
                    } else {
                        directorySet.add(parentPath);

                        imageFolder = new ImageFolder();
                        imageFolder.setDirectory(parentPath);
                        imageFolder.setFirstImagePath(path);
                    }

                    if (parentFile.list() == null) {
                        continue;
                    } else {
                        int imageCount  = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".png") || filename.toLowerCase().endsWith(".jpeg"))
                                    return true;
                                return false;
                            }
                        }).length;

                        imageFolder.setCount(imageCount);
                        mImageFolderList.add(imageFolder);

                        //默认显示的是图片数量最多的文件夹
                        if (imageCount > mMaxCount) {
                            mMaxCount = imageCount;
                            mCurrentDirectory = parentFile;
                        }
                    }
                }

                cursor.close();

                mHandler.sendEmptyMessage(MESSAGE_SCAN_SUCCEED);
            }
        }.start();
    }

    private void initEvent() {
        mBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
                mPopupWindow.showAsDropDown(mBottomLayout, 0, 0);
                lightOff();
            }
        });
    }
}
