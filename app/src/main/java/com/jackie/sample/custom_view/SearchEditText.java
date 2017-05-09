package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.utils.Common;

/**
 * Created by Jackie on 2017/2/21.
 * 用于搜索的标签添加和管理控件
 */
public class SearchEditText extends RelativeLayout {
    private Context mContext;
    private LayoutInflater mInflater = null;
    private View mView = null;
    private LinearLayout mSearchLayout = null;
    private EditText mEditText = null;

    public SearchEditText(Context context) {
        this(context, null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        initView();
    }
    private OnSearchChangeListener mSearchChangeListener;

    public interface OnSearchChangeListener {
        void onSearchChange(String s);
        void onRemoveView(int position);
        void onSearchResult();
    }

    public void setOnSearchChangeListener(OnSearchChangeListener searchChangeListener) {
        mSearchChangeListener = searchChangeListener;
    }

    private void initView() {
        mInflater = LayoutInflater.from(mContext);
        mView = mInflater.inflate(R.layout.item_search_result, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = 15;
        params.rightMargin = 15;
        addView(mView, params);

        mSearchLayout = (LinearLayout) mView.findViewById(R.id.search_layout);
        mEditText = (EditText) mView.findViewById(R.id.edit_text);

        mEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (Common.getInstance().isNotFastClick()) {
                        if (mEditText.getText().toString().length() > 0) {
                            String str = mEditText.getText().toString();
                            str = str.substring(0, str.length() - 1);
                            mEditText.setText(str);
                            mEditText.setSelection(str.length());
                        } else {
                            if (mSearchLayout.getChildCount() > 0) {
                                if (mSearchChangeListener != null) {
                                    mSearchChangeListener.onRemoveView(mSearchLayout.getChildCount() - 1);
                                }

                                mSearchLayout.removeViewAt(mSearchLayout.getChildCount() - 1);
                            }
                        }
                    }
                }

                return true;
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //录入了数据，输入框为空，执行搜索功能
                    if (mEditText.getText().toString().trim().equals("")) {
                        if (mSearchLayout.getChildCount() > 0){
                            if (mSearchChangeListener != null) {
                                mSearchChangeListener.onSearchResult();
                            }
                        }

                        return true;
                    }

                    TextView textView = new TextView(mContext);
                    textView.setText(mEditText.getText().toString().trim());
                    textView.setTextSize(14);
                    textView.setTextColor(Color.parseColor("#dfe0e0"));
                    textView.setPadding(10, 0, 10, 0);
                    textView.setBackgroundResource(R.drawable.shape_edit_text_round_bg);
                    textView.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = 10;
                    textView.setLayoutParams(params);

                    if (mSearchChangeListener != null) {
                        mSearchChangeListener.onSearchChange(mEditText.getText().toString().trim());
                    }

                    mEditText.setText("");
                    mSearchLayout.addView(textView);
                }
                return true;
            }
        });
    }

    public EditText getEditText() {
        return mEditText;
    }

    public LinearLayout getContainer() {
        return mSearchLayout;
    }
}
