package com.jackie.sample.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.net.NetworkInterface;
import java.net.SocketException;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Jackie on 2017/6/28.
 * 获取手机设备硬件信息的工具类
 */

public class DeviceUtils {
    /**
     * 获取设备号(非手机设备上可能会出问题)
     * @return
     */
    public static String getDeviceInfo(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int readPhoneStatePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);    //获取是否允许的状态
            if (readPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {    //如果是不允许
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, 0); //请求权限
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {   //如果允许了
                    TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
                    return telephonyManager.getDeviceId();
                } else {    //  如果还是不允许
                    return "000000";
                }
            } else {    //  如果是允许
                TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
                return telephonyManager.getDeviceId();
            }
        }

        // 6.0以下
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取设备名
     * @return 设备名
     */
    public String getDeviceName() {
        return Build.BRAND + Build.MODEL;
    }

    /**
     * 获取设备Mac地址
     * @return 设备Mac地址
     */
    public static String getDeviceMac() {
        /**
         * 获取mac地址，在Android 6.0版本后，以下注释方法不再适用，
         * 不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，
         * 这是google官方为了加强权限管理而禁用了getSystemService(Context.WIFI_SERVICE)方法来获得mac地址。
         * */

//        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        String macAddress = wifiInfo.getMacAddress();
//        return macAddress;

        String macAddress;
        StringBuffer stringBuffer = new StringBuffer();
        NetworkInterface networkInterface;

        try {
            networkInterface = NetworkInterface.getByName("eth1");

            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }

            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }

            byte[] hardwareAddress = networkInterface.getHardwareAddress();

            for (byte address : hardwareAddress) {
                stringBuffer.append(String.format("%02X:", address));
            }

            if (stringBuffer.length() > 0) {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            }

            macAddress = stringBuffer.toString();
        } catch (SocketException e) {
            e.printStackTrace();

            return "02:00:00:00:00:02";
        }

        return macAddress;
    }
}
