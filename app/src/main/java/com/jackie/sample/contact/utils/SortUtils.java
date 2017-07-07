package com.jackie.sample.contact.utils;

import com.jackie.sample.bean.ContactBean;
import com.jackie.sample.utils.PinyinUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jackie on 2017/7/6.
 */
public class SortUtils {
    /**
     * 对数据进行排序
     * @param contactList 要进行排序的数据源
     */
    public static void sortData(List<ContactBean> contactList) {
        if (contactList == null || contactList.size() == 0) {
            return;
        }

        for (int i = 0; i < contactList.size(); i++) {
            ContactBean contactBean = contactList.get(i);
            String firstLetter = PinyinUtils.getPinyinUppercase(contactBean.getName().substring(0, 1)).substring(0, 1);
            if (firstLetter.matches("[A-Z]")) {
                contactBean.setFirstLetter(firstLetter);
            } else {
                contactBean.setFirstLetter("#");
            }
        }

        Collections.sort(contactList, new Comparator<ContactBean>() {
            @Override
            public int compare(ContactBean o1, ContactBean o2) {
                if ("#".equals(o1.getFirstLetter())) {
                    return 1;
                } else if ("#".equals(o2.getFirstLetter())) {
                    return -1;
                } else {
                    return o1.getFirstLetter().compareTo(o2.getFirstLetter());
                }
            }
        });
    }

    /**
     * @param contactList 数据源
     * @return 返回一个包含所有Tag字母在内的字符串
     */
    public static String getSortLetters(List<ContactBean> contactList) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < contactList.size(); i++) {
            if (!stringBuilder.toString().contains(contactList.get(i).getFirstLetter())) {
                stringBuilder.append(contactList.get(i).getFirstLetter());
            }
        }

        return stringBuilder.toString();
    }
}