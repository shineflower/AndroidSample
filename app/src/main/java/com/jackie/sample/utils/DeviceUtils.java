package com.jackie.sample.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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

    /**
     * 获取手机端ip地址
     * @param context  上下文
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements(); ) {
                        NetworkInterface networkInterface = networkInterfaces.nextElement();
                        for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddress.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址

                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }

        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip  ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
