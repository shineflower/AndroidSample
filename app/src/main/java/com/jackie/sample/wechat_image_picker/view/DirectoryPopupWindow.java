package com.jackie.sample.wechat_image_picker.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jackie.sample.R;
import com.jackie.sample.wechat_image_picker.adapter.PopupWindowAdapter;
import com.jackie.sample.wechat_image_picker.bean.ImageFolder;

import java.util.List;

/**
 * Created by Jackie on 2015/12/28.
 * 选择目录的PopupWindow
 */
public class DirectoryPopupWindow extends PopupWindow {
    private View mContentView;
    private int mWidth;
    private int mHeight;
    private List<ImageFolder> mList;
    private ListView mListView;

    public interface OnDirectorySelectedListener{
        void onSelected(ImageFolder imageFolder);
    };

    private OnDirectorySelectedListener mOnDirectorySelectedListener;

    public void setOnDirectorySelectedListener(OnDirectorySelectedListener onDirectorySelectedListener) {
        this.mOnDirectorySelectedListener = onDirectorySelectedListener;
    }

    public DirectoryPopupWindow(Context context, List<ImageFolder> list) {
        calculateWidthAndHeight(context);

        mContentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_main, null);
        setContentView(mContentView);

        this.mList = list;

        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initView(context);
        initEvent();
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnDirectorySelectedListener != null) {
                    mOnDirectorySelectedListener.onSelected(mList.get(position));
                }
            }
        });
    }

    private void initView(Context context) {
        mListView = (ListView) mContentView.findViewById(R.id.list_directory);
        PopupWindowAdapter adapter = new PopupWindowAdapter(context, mList);
        mListView.setAdapter(adapter);
    }

    /**
     * 计算PopupWindow的宽和高
     * @param context
     */
    private void calculateWidthAndHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        mWidth = displayMetrics.widthPixels;
        mHeight = (int) (displayMetrics.heightPixels * 0.7);
    }
}