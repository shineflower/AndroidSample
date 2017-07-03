package com.jackie.sample.utils;

import android.util.Base64;

public class Base64Utils {
    public static String encodeBase64(byte[] input) throws Exception {
        return Base64.encodeToString(input, Base64.DEFAULT);
    }

    /***
     * decode by Base64
     */
    public static byte[] decodeBase64(byte[] input) throws Exception {
        return  Base64.decode(input, Base64.DEFAULT);
    }
}
