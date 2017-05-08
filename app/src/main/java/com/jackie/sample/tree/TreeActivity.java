package com.jackie.sample.tree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.adapter.SimpleTreeListViewAdapter;
import com.jackie.sample.adapter.TreeListViewAdapter;
import com.jackie.sample.bean.FileBean;
import com.jackie.sample.bean.Node;

import java.util.ArrayList;
import java.util.List;

public class TreeActivity extends Activity {
    private ListView mListView;
    private SimpleTreeListViewAdapter mSimpleTreeListViewAdapter;
    private List<FileBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
    }

    private void initData() {
        FileBean fileBean = new FileBean(1, 0, "目录1");
        mList.add(fileBean);
        fileBean = new FileBean(2, 0, "目录2");
        mList.add(fileBean);
        fileBean = new FileBean(3, 0, "目录3");
        mList.add(fileBean);
        fileBean = new FileBean(4, 1, "目录1-1");
        mList.add(fileBean);
        fileBean = new FileBean(5, 4, "目录1-1-1");
        mList.add(fileBean);
        fileBean = new FileBean(6, 4, "目录1-1-2");
        mList.add(fileBean);
        fileBean = new FileBean(7, 2, "目录2-1");
        mList.add(fileBean);
        fileBean = new FileBean(8, 2, "目录2-2");
        mList.add(fileBean);
        fileBean = new FileBean(9, 3, "目录3-1");
        mList.add(fileBean);
        fileBean = new FileBean(10, 3, "目录3-2");
        mList.add(fileBean);

        try {
            mSimpleTreeListViewAdapter = new SimpleTreeListViewAdapter(this, mListView, mList, 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mListView.setAdapter(mSimpleTreeListViewAdapter);
    }

    private void initEvent() {
        mSimpleTreeListViewAdapter.setTreeNoteClickListener(new TreeListViewAdapter.OnTreeNoteClickListener() {
            @Override
            public void onClick(Node node, int position) {
                if (node.isLeaf()) {
                    Toast.makeText(TreeActivity.this, node.getName(), Toast.LENGTH_LONG).show();
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText editText = new EditText(TreeActivity.this);
                new AlertDialog.Builder(TreeActivity.this).setTitle("Add Node").setView(editText).setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            return;
                        }
                        mSimpleTreeListViewAdapter.addExtraNode(position, editText.getText().toString());
                    }
                }).setNegativeButton("Cancel", null).show();
                return true;
            }
        });
    }
}
