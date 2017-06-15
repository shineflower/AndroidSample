package com.jackie.sample.utils;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by Jackie on 2017/6/14.
 */

public class PinyinUtils {
    public static String getPinyinUppercase(String text) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            //输出格式
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            //去掉音调
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            //转化成大写
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);

            if (!TextUtils.isEmpty(text)) {
                char[] chars = text.toCharArray();

                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];

                    //如果c是空格，直接跳过
                    if (Character.isWhitespace(c)) {
                        continue;
                    }

                    //如果是键盘上直接打出来的字符 A-Z 0-9
                    if (-128 < c && c <= 127) {
                        stringBuilder.append(c);
                    } else {
                        try {
                            if (isChinese(c)) {
                                String[] strings = PinyinHelper.toHanyuPinyinStringArray(c, format);
                                stringBuilder.append(strings[0]);
                            } else {
                                stringBuilder.append(String.valueOf(c));
                            }
                        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                            badHanyuPinyinOutputFormatCombination.printStackTrace();
                        }
                    }
                }
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getPinyinLowercase(String text) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            //输出格式
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            //去掉音调
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            //转化成小写
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

            if (!TextUtils.isEmpty(text)) {
                char[] chars = text.toCharArray();

                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];

                    //如果c是空格，直接跳过
                    if (Character.isWhitespace(c)) {
                        continue;
                    }

                    //如果是键盘上直接打出来的字符 A-Z 0-9
                    if (-128 < c && c <= 127) {
                        stringBuilder.append(c);
                    } else {
                        try {
                            if (isChinese(c)) {
                                String[] strings = PinyinHelper.toHanyuPinyinStringArray(c, format);
                                stringBuilder.append(strings[0]);
                            } else {
                                stringBuilder.append(String.valueOf(c));
                            }
                        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                            badHanyuPinyinOutputFormatCombination.printStackTrace();
                        }
                    }
                }
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isChinese(char c) {
        if ((c >= 0x4e00) && (c <= 0x9fbb)) {
            return true;
        }

        return false;
    }

    public static boolean containsChinese(String s) {
        if (TextUtils.isEmpty(s)){
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            if (isChinese(s.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}
