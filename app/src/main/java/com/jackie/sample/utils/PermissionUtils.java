package com.jackie.sample.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AlertDialog;

import com.jackie.sample.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.support.v4.content.PermissionChecker.checkPermission;

/**
 * author ：minifly
 * date: 2017/6/5
 * time: 16:22
 * desc:权限管理相关
 */
public class PermissionUtils {
    public static String TAG = "permission";

    // Map of dangerous permissions introduced in later framework versions.
    // Used to conditionally bypass permission-hold checks on older devices.
    private static final SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<String, Integer>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }

    //回调监听
    public interface PermissionCallbacks extends ActivityCompat.OnRequestPermissionsResultCallback {

        void onPermissionsGranted(int requestCode, List<String> perms);

        void onPermissionsDenied(int requestCode, List<String> perms);

    }

    public static ComponentCallbacks requestPermissions(
            @NonNull ComponentCallbacks host, @NonNull String[] permissions,
            int requestCode) {

        if(host instanceof  Activity){
            if (Build.VERSION.SDK_INT >= 23) {
                if(!hasPermissions(((Activity)host),permissions)){
                    ((Activity)host).requestPermissions(permissions, requestCode);
                }else{
                    List<String > pers = new LinkedList<>();
                    for (String per : permissions) {
                        pers.add(per);
                    }
                    ((PermissionCallbacks) host).onPermissionsGranted(requestCode, pers);//权限通过
                }
            }else{//版本小于23，不用请求权限
                List<String > pers = new LinkedList<>();
                for (String per : permissions) {
                    pers.add(per);
                }
                ((PermissionCallbacks) host).onPermissionsGranted(requestCode, pers);//权限通过

            }
        }else if(host instanceof Fragment){
            if (Build.VERSION.SDK_INT >= 23) {

                if(!hasPermissions(((Fragment)host).getContext(),permissions)){
                    ((Fragment)host).requestPermissions(permissions, requestCode);
                }else{
                    List<String > pers = new LinkedList<>();
                    for (String per : permissions) {
                        pers.add(per);
                    }
                    ((PermissionCallbacks) host).onPermissionsGranted(requestCode, pers);//权限通过
                }
            }else{//版本小于23，不用请求权限
                List<String > pers = new LinkedList<>();
                for (String per : permissions) {
                    pers.add(per);
                }
                ((PermissionCallbacks) host).onPermissionsGranted(requestCode, pers);//权限通过

            }
        }


        return host;
    }



    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, @NonNull Object... receivers) {
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            LogUtils.showErrLog("sssss"+ perm);
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {//如果有不小心传进来的非运行时权限就这么干
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        for (Object object : receivers) {
            //记录同意的权限
            if (!granted.isEmpty()) {
                if (object instanceof PermissionCallbacks) {
                    ((PermissionCallbacks) object).onPermissionsGranted(requestCode, granted);//权限通过
                }
            }

            // 记录所有拒绝的权限
            if (!denied.isEmpty()) {
                if (object instanceof PermissionCallbacks) {
                    ((PermissionCallbacks) object).onPermissionsDenied(requestCode, denied);//权限拒绝
                }
            }
        }
    }

    //检测权限是否已全部拥有
    public static boolean hasPermissions(Context context, @NonNull String... perms) {
        for (String permission : perms) {
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;

    }


    private static boolean hasSelfPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
            return hasSelfPermissionForXiaomi(context, permission);
        }
        try {
            return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }
    private static boolean hasSelfPermissionForXiaomi(Context context, String permission) {
        String permissionToOp = AppOpsManagerCompat.permissionToOp(permission);
        if (permissionToOp == null) {
            // in case of normal permissions(e.g. INTERNET)
            return true;
        }
        int noteOp = AppOpsManagerCompat.noteOp(context, permissionToOp,  android.os.Process.myUid(), context.getPackageName());
        return noteOp == AppOpsManagerCompat.MODE_ALLOWED && checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static int checkSelfPermission(@NonNull Context context,
                                          @NonNull String permission) {
        return checkPermission(context, permission, android.os.Process.myPid(),
                android.os.Process.myUid(), context.getPackageName());
    }

    private static boolean permissionExists(String permission) {
        // Check if the permission could potentially be missing on this device
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        // If null was returned from the above call, there is no need for a device API level check for the permission;
        // otherwise, we check if its minimum API level requirement is met
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }

    //给用户显示该权限的作用
    public static void showPerDetailDialog(final Activity host){
        //用户不同意，向用户展示该权限作用
        if (!ActivityCompat.shouldShowRequestPermissionRationale(host, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertDialog dialog = new AlertDialog.Builder(host)
                    .setMessage(R.string.permission_message_txt)
                    .setPositiveButton(R.string.permission_sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(R.string.permission_cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
            dialog.show();
            return;
        }
    }


    //到系统权限管理页面设置相应的权限。
    public static void permissionDialog(final Activity host,List<String> perms,@Nullable final AlertDialog.OnClickListener listener){
        StringBuilder perNames = new StringBuilder("");
        for(String perm : perms){
            perNames.append(host.getString(mPermissionMap.get(perm)) + ",");
        }


        final AlertDialog.Builder builder = new AlertDialog.Builder(host);
        builder.setTitle(R.string.permission_promote_txt);
        builder.setMessage(host.getString(R.string.permission_apknohave_txt)+perNames.toString().substring(0,perNames.toString().length()-1)+host.getString(R.string.permission_openpermission));
        builder.setNegativeButton(host.getString(R.string.permission_cancle),listener == null?new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }:listener);

        builder.setPositiveButton(host.getString(R.string.permission_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", host.getPackageName(), null));
                host.startActivity(intent);
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * CALENDAR  日历
     READ_CALENDAR
     WRITE_CALENDAR

     CAMERA  相机
     CAMERA

     CONTACTS 联系人
     READ_CONTACTS
     WRITE_CONTACTS
     GET_ACCOUNTS

     LOCATION  位置
     ACCESS_FINE_LOCATION
     ACCESS_COARSE_LOCATION

     MICROPHONE  麦克风
     RECORD_AUDIO

     PHONE  手机
     READ_PHONE_STATE
     CALL_PHONE
     READ_CALL_LOG
     WRITE_CALL_LOG
     ADD_VOICEMAIL
     USE_SIP
     PROCESS_OUTGOING_CALLS

     SENSORS  传感器
     BODY_SENSORS

     SMS  短信
     SEND_SMS
     RECEIVE_SMS
     READ_SMS
     RECEIVE_WAP_PUSH
     RECEIVE_MMS

     STORAGE  存储
     READ_EXTERNAL_STORAGE
     WRITE_EXTERNAL_STORAGE
     */
    static Map<String,Integer > mPermissionMap = new HashMap<>();

    static{
        mPermissionMap.put("android.permission.READ_CALENDAR", R.string.permission_readcalendar_txt);
        mPermissionMap.put("android.permission.WRITE_CALENDAR",R.string.permission_write_calendar_txt);

        mPermissionMap.put("android.permission.CAMERA",R.string.permission_camera_txt);

        mPermissionMap.put("android.permission.READ_CONTACTS",R.string.permission_readcontacts_txt);
        mPermissionMap.put("android.permission.WRITE_CONTACTS",R.string.permission_writecontacts_txt);
        mPermissionMap.put("android.permission.GET_ACCOUNTS",R.string.permission_getaccounts_txt);

        mPermissionMap.put("android.permission.ACCESS_FINE_LOCATION",R.string.permission_fine_location_txt);
        mPermissionMap.put("android.permission.ACCESS_COARSE_LOCATION",R.string.permission_coarse_location_txt);

        mPermissionMap.put("android.permission.RECORD_AUDIO",R.string.permission_recordaudio_txt);

        mPermissionMap.put("android.permission.READ_PHONE_STATE",R.string.permission_readphone_state_txt);
        mPermissionMap.put("android.permission.CALL_PHONE",R.string.permission_callphone_txt);
        mPermissionMap.put("android.permission.READ_CALL_LOG",R.string.permission_calllog_txt);
        mPermissionMap.put("android.permission.WRITE_CALL_LOG",R.string.permission_write_call_log_txt);
        mPermissionMap.put("android.permission.USE_SIP",R.string.permission_use_sip_txt);
        mPermissionMap.put("android.permission.PROCESS_OUTGOING_CALLS",R.string.permission_process_calls_txt);

        mPermissionMap.put("android.permission.BODY_SENSORS",R.string.permission_body_sensors_txt);

        mPermissionMap.put("android.permission.SEND_SMS",R.string.permission_sendsms_txt);
        mPermissionMap.put("android.permission.RECEIVE_SMS",R.string.permission_receive_sms_txt);
        mPermissionMap.put("android.permission.READ_SMS",R.string.permission_read_sms_txt);
        mPermissionMap.put("android.permission.RECEIVE_WAP_PUSH",R.string.permission_receive_wap_push_txt);
        mPermissionMap.put("android.permission.RECEIVE_MMS",R.string.permission_receive_mms_txt);

        mPermissionMap.put("android.permission.READ_EXTERNAL_STORAGE",R.string.permission_read_storage_txt);
        mPermissionMap.put("android.permission.WRITE_EXTERNAL_STORAGE",R.string.permission_write_storage_txt);
    }
}
