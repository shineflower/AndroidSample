package com.jackie.sample.time_picker.picker_view;

import android.view.View;

import com.jackie.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 0.1 king 2015-11
 */
final class WheelOptions {
    private final CharacterPickerView view;
    private SingleChooseLoopView wv_option1;

    private List<String> mOptions1Items;

    private OnOptionChangedListener mOnOptionChangedListener;

    public View getView() {
        return view;
    }

    public WheelOptions(CharacterPickerView view) {
        super();
        this.view = view;
    }

    public void setOnOptionChangedListener(OnOptionChangedListener listener) {
        this.mOnOptionChangedListener = listener;
    }

    public void setPicker(ArrayList<String> optionsItems) {
        setPicker(optionsItems, null, null);
    }

    public void setPicker(List<String> options1Items,
                          List<List<String>> options2Items) {
        setPicker(options1Items, options2Items, null);
    }

    public void setPicker(List<String> options1Items,
                          List<List<String>> options2Items,
                          List<List<List<String>>> options3Items) {
        this.mOptions1Items = options1Items == null ? new ArrayList<String>() : options1Items;

        // 选项1
        wv_option1 = (SingleChooseLoopView) view.findViewById(R.id.j_options1);
        wv_option1.setItems(mOptions1Items);// 设置显示数据
        wv_option1.setCurrentItem(0);// 初始化时显示的数据
        //设置是否循环播放
        wv_option1.setNotLoop();

        //滚动监听
        wv_option1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (index == -1) {
                    return;
                }
            }
        });


        setCurrentItems(0);
    }

    /**
     * 选中项改变
     */
    private void doItemChange() {
        if (mOnOptionChangedListener != null) {
            int option1 = wv_option1.getSelectedItem();
            mOnOptionChangedListener.onOptionChanged(option1);
        }
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_option1.setLoop(cyclic);
    }

    /**
     * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
     *
     * @return
     */
    public int getCurrentItems() {
        return wv_option1.getSelectedItem();
    }

    public void setCurrentItems(int option1) {
        wv_option1.setCurrentItem(option1);

    }
}
