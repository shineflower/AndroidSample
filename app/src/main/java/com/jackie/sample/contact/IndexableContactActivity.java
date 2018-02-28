package com.jackie.sample.contact;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.IndexableListView;
import com.jackie.sample.utils.ContactUtils;
import com.jackie.sample.utils.PinyinUtils;

import java.util.List;

public class IndexableContactActivity extends AppCompatActivity {
    private IndexableListView mListView;

    private List<String> mList;

    private static final String SECTIONS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indexable_contact);

        initView();
    }

    private void initView() {
        mListView = (IndexableListView) findViewById(R.id.contact_list_view);
        mList = ContactUtils.getDisplayName(this);

        ContactAdapter contactAdapter = new ContactAdapter(this, android.R.layout.simple_expandable_list_item_1, mList);
        mListView.setAdapter(contactAdapter);
        mListView.setFastScrollEnabled(true);
    }

    private class ContactAdapter extends ArrayAdapter<String> implements SectionIndexer {

        public ContactAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        //返回一个代表sections列表的非空的数组对象
        //当滑动的时候，这个ListView可以调用toString()来显示预览文本。
        //例如，一个adapter可以返回代表字母表中字母的字符串数组，或者返回section titles的对象数组
        @Override
        public Object[] getSections() {
            String[] sections = new String[SECTIONS.length()];

            for (int i = 0; i < SECTIONS.length(); i++) {
                sections[i] = String.valueOf(SECTIONS.charAt(i));
            }

            return sections;
        }

        //提供section索引，通过section数组对象，返回adapter中section开始的位置
        @Override
        public int getPositionForSection(int sectionIndex) {
            for (int i = 0; i < mList.size(); i++) {
                String name = PinyinUtils.getPinyinUppercase(mList.get(i).charAt(0) + "").charAt(0) + "";   // 获取首字母
                String section = getSections()[sectionIndex] + "";

                if (name.equals(section)) {
                    return i;
                }
            }

            return 0;
        }

        //提供adapter中的位置，在section数组对象中返回相应的section索引
        //如果position位置在索引开始位置之前，则返回0
        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
    }
}
