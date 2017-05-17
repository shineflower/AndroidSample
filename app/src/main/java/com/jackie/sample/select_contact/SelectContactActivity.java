package com.jackie.sample.select_contact;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.jackie.sample.R;
import com.jackie.sample.adapter.ContactAdapter;
import com.jackie.sample.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SelectContactActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEditText;
    private ImageButton mImageButton;
    private PopupWindow mPopupWindow;
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private List<String> mNumberList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        //初始化数据
        mNumberList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mNumberList.add("1000000" + i);
        }

        mEditText = (EditText) findViewById(R.id.et_number);
        mImageButton = (ImageButton) findViewById(R.id.ib_number);
        mImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_number:
                showSelectNumberPopupWindow();
                break;
        }
    }

    /**
     * 弹出选择号码的对话框
     */
    private void showSelectNumberPopupWindow() {
        initRecyclerView();

        mPopupWindow = new PopupWindow(mRecyclerView, mEditText.getWidth() - 4, 500);
        mPopupWindow.setOutsideTouchable(true);		// 设置外部可以被点击
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        mPopupWindow.setFocusable(true);		// 使PopupWindow可以获得焦点
        // 显示在输入框的左下角
        mPopupWindow.showAsDropDown(mEditText, 2, -5);
    }

    /**
     * 初始化RecyclerView，模仿ListView下拉列表的效果
     */
    private void initRecyclerView(){
        mRecyclerView = new RecyclerView(this);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setBackgroundResource(R.drawable.bg_select_contact);

        //设置Adapter
        mAdapter = new ContactAdapter(this, mNumberList);
        mRecyclerView.setAdapter(mAdapter);

        //设置点击事件
        mAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mEditText.setText(mNumberList.get(position));
                mEditText.setSelection(mEditText.getText().toString().length());
                mPopupWindow.dismiss();
            }
        });

        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }
}
