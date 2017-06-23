package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackie.sample.R;

import java.util.List;

/**
 * Created by Jackie on 2017/6/22.
 * 自定义虚拟键盘
 */

public class VirtualKeyboardView extends RelativeLayout implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context mContext;

    private VirtualKeyView mVirtualKeyView;
    private ImageView mDeleteView;
    private TextView mConfirmView;

    private GridView mGridView;
    private List<String> mKeyList;

    private EditText mEditText;
    private OnConfirmListener mOnConfirmListener;

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.mOnConfirmListener = onConfirmListener;
    }

    public interface OnConfirmListener {
        void onConfirm();
    }

    public VirtualKeyboardView(Context context) {
        this(context, null);
    }

    public VirtualKeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirtualKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        initView(attrs);
        initEvent();
    }

    private void initView(AttributeSet attrs) {
        View view = View.inflate(mContext, R.layout.virtual_keyboard_view, null);
        mVirtualKeyView = (VirtualKeyView) view.findViewById(R.id.virtual_key_view);

        mDeleteView = (ImageView) view.findViewById(R.id.delete);
        mConfirmView = (TextView) view.findViewById(R.id.confirm);

        mGridView = mVirtualKeyView.getGridView();
        mKeyList = mVirtualKeyView.getKeyList();

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.VirtualKeyboardView);
        if (typedArray != null) {
            int type = typedArray.getInteger(R.styleable.VirtualKeyboardView_type, 0);
            switch (type) {
                case 0:
                    //身份证键盘
                    mVirtualKeyView.initData(VirtualKeyView.ID_KEYBOARD);
                    break;
                case 1:
                    //小数键盘
                    mVirtualKeyView.initData(VirtualKeyView.FLOAT_KEYBOARD);
                    break;
                case 2:
                    //数字键盘
                    mVirtualKeyView.initData(VirtualKeyView.NUMBER_KEYBOARD);
                    break;
            }

            mVirtualKeyView.invalidate();

            typedArray.recycle();
        }

        addView(view);
    }

    public void setEditText(@NonNull EditText editText) {
        this.mEditText = editText;
    }

    private void initEvent() {
        mDeleteView.setOnClickListener(this);
        mConfirmView.setOnClickListener(this);

        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                Editable editable = mEditText.getText();

                int start = mEditText.getSelectionStart();
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
                break;
            case R.id.confirm:
                if (mOnConfirmListener != null) {
                    mOnConfirmListener.onConfirm();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Editable editable = mEditText.getText();

        int start = mEditText.getSelectionStart();

        if (!"".equals(mKeyList.get(position))) {
            editable.insert(start, mKeyList.get(position));
        }
    }
}
