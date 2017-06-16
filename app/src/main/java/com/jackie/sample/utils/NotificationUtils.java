package com.jackie.sample.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.jackie.sample.R;
import com.jackie.sample.notification.RemoteActivity;

/**
 * 通知工具类
 * Created by Jackie on 2017/6/15.
 */

public class NotificationUtils {
    private static NotificationUtils mNotificationUtils;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private int mId = 0;
    private int mCurrentId;

    private NotificationUtils(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL);
    }

    public static NotificationUtils getInstance(Context context) {
        if (null == mNotificationUtils) {
            synchronized (NotificationUtils.class) {
                if (null == mNotificationUtils) {
                    mNotificationUtils = new NotificationUtils(context);
                }
            }
        }

        return mNotificationUtils;
    }

    /**
     * 发送通知
     * @param context context
     * */
    public void sendNotification(Context context) {
        Intent intent = new Intent(context, RemoteActivity.class);
        intent.putExtra("content", "通知过来的消息");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentTitle("通知")
                .setContentText("通知内容")
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, false);
        mNotificationManager.notify(1, mBuilder.build());
    }

    /**
     * 发送通知
     * @param context context
     * @param title   标题
     * @param content 内容
     */
    public void sendNotification(Context context, String title, String content) {
        Intent intent = new Intent(context, RemoteActivity.class);
        intent.putExtra("content", "通知过来的消息");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentTitle(title)
                .setContentText(content)
                .setFullScreenIntent(pendingIntent, false);
        mNotificationManager.notify(1, mBuilder.build());
    }


    /**
     * 自定义Notification
     * @param text    通知内容
     * @param clazz   跳转的页面
     */
    public void sendNotification(Context context, String text, Class clazz) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("content", "send custom notification");
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        mCurrentId = mId++;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, mCurrentId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_remote_view);
        remoteViews.setTextViewText(R.id.tv_text, text);
        remoteViews.setImageViewResource(R.id.iv_icon, R.mipmap.ic_launcher);
        mBuilder.setContent(remoteViews)
                .setAutoCancel(true);
//                .setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setFullScreenIntent(pendingIntent, true);  //5.0以上才有效
        } else {
            mBuilder.setContentIntent(pendingIntent);
        }

        mNotificationManager.notify(mCurrentId, mBuilder.build());
    }
}
