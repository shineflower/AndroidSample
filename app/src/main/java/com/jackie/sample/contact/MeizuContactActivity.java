package com.jackie.sample.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.jackie.sample.R;
import com.jackie.sample.bean.ContactBean;
import com.jackie.sample.contact.adapter.ContactAdapter;
import com.jackie.sample.contact.anim.SlideInOutLeftItemAnimator;
import com.jackie.sample.contact.utils.ContactItemDecoration;
import com.jackie.sample.contact.utils.SortUtils;
import com.jackie.sample.contact.view.SideBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/7/6.
 */

public class MeizuContactActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private ContactItemDecoration mContactItemDecoration;
    private SideBar mSideBar;

    private ContactAdapter mAdapter;

    private List<ContactBean> mContactList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizu_contact);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //侧边导航栏
        mSideBar = (SideBar) findViewById(R.id.side_bar);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mContactItemDecoration = new ContactItemDecoration(this));
        mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));

        mAdapter = new ContactAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        String[] names = { "孙尚香", "安其拉", "白起", "不知火舞", "@小马快跑", "_德玛西亚之力_", "妲己", "狄仁杰", "典韦", "韩信",
                "老夫子", "刘邦", "刘禅", "鲁班七号", "墨子", "孙膑", "孙尚香", "孙悟空", "项羽", "亚瑟",
                "周瑜", "庄周", "蔡文姬", "甄姬", "廉颇", "程咬金", "后羿", "扁鹊", "钟无艳", "小乔", "王昭君", "虞姬",
                "李元芳", "张飞", "刘备", "牛魔", "张良", "兰陵王", "露娜", "貂蝉", "达摩", "曹操", "芈月", "荆轲", "高渐离",
                "钟馗", "花木兰", "关羽", "李白", "宫本武藏", "吕布", "嬴政", "娜可露露", "武则天", "赵云", "姜子牙" };

        for (String name : names) {
            ContactBean contactBean = new ContactBean();
            contactBean.setName(name);
            mContactList.add(contactBean);
        }

        //对数据源进行排序
        SortUtils.sortData(mContactList);

        //返回一个包含所有字母在内的字符串并赋值
        String sortLetters = SortUtils.getSortLetters(mContactList);
        mSideBar.setSortLetters(sortLetters);

        mContactItemDecoration.setData(mContactList, sortLetters);

        mAdapter.addAll(mContactList);
    }

    public void initEvent() {
        mSideBar.setOnIndexChangedListener(new SideBar.OnIndexChangedListener() {
            @Override
            public void onIndexChanged(String sortLetter) {
                if (TextUtils.isEmpty(sortLetter) || mContactList.size() <= 0) {
                    return;
                }

                for (int i = 0; i < mContactList.size(); i++) {
                    if (sortLetter.equals(mContactList.get(i).getFirstLetter())) {
                        mLayoutManager.scrollToPositionWithOffset(i, 0);
//                        mLayoutManager.scrollToPosition(i);
                        return;
                    }
                }
            }
        });
    }
}
