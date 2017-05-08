package com.jackie.sample.indexable_contact;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import com.jackie.sample.R;
import com.jackie.sample.utils.ContactUtils;
import com.jackie.sample.custom_view.IndexableListView;
import com.jackie.sample.utils.StringMatcher;

import java.util.List;

public class IndexableContactActivity extends AppCompatActivity {
    private IndexableListView mListView;

    private List<String> mList;

    private static final String SECTIONS = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indexable_contact);

        initView();
    }

    private void initView() {
        mListView = (IndexableListView) findViewById(R.id.contacts_list_view);
        mList = ContactUtils.getDisplayName(this);

        ContactAdapter contactAdapter = new ContactAdapter(this, android.R.layout.simple_expandable_list_item_1, mList);
        mListView.setAdapter(contactAdapter);
        mListView.setFastScrollEnabled(true);
    }

    private class ContactAdapter extends ArrayAdapter<String> implements SectionIndexer {

        public ContactAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public Object[] getSections() {
            String[] sections = new String[SECTIONS.length()];
            for (int i = 0; i < SECTIONS.length(); i++) {
                sections[i] = String.valueOf(SECTIONS.charAt(i));
            }
            return sections;
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            // If there is no item for current section, previous section will be selected
            for (int i = sectionIndex; i >= 0; i--) {
                for (int j = 0; j < getCount(); j++) {
                    if (i == 0) {
                        // For numeric section
                        for (int k = 0; k <= 9; k++) {
                            if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(k))) {
                                return j;
                            }
                        }
                    } else {
                        // For letter section
                        if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(SECTIONS.charAt(i)))) {
                            return j;
                        }
                    }
                }
            }

            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
    }
}
