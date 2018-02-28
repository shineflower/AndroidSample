package com.jackie.sample.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2016/2/22.
 * 从数据中获取联系人
 *
 * ContactsContract.Contacts.CONTENT_URI  content://com.android.contacts/contacts
 */
public class ContactUtils {

    public static List<String> getDisplayName(Context context) {
        List<String> displayNameList = new ArrayList<>();
        //按照字母升序排列
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "sort_key COLLATE LOCALIZED asc");

        while (cursor.moveToNext()) {
            String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            displayNameList.add(displayName);
        }

        return displayNameList;
    }
}