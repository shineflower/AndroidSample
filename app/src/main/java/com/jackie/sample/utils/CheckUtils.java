package com.jackie.sample.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 根据字节判断长度，1个中文字符 = 2个英文字符
 * 用法：setFilters(new InputFilter[] { UserCenterCheckUtils.limitLength(20) });
 */

public class CheckUtils {

    public static CharSequence mCharSequence;

    public static InputFilter limitLength(final int maxLength) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
                int dindex = 0;
                int count = 0;

                while (count <= maxLength && dindex < dest.length()) {
                    char c = dest.charAt(dindex++);
                    if (c < 128) {
                        count = count + 1;
                    } else {
                        count = count + 2;
                    }
                }

                if (count > maxLength) {
                    return dest.subSequence(0, dindex - 1);
                }

                int sindex = 0;

                while (count <= maxLength && sindex < src.length()) {
                    char c = src.charAt(sindex++);
                    if (c < 128) {
                        count = count + 1;
                    } else {
                        count = count + 2;
                    }
                }

                if (count > maxLength) {
                    sindex--;
                }

                mCharSequence = src.subSequence(0, sindex);

                return mCharSequence;
            }
        };

        return filter;
    }
}
