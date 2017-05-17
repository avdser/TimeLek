package com.ru54.avd.menu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.ru54.avd.menu.R.raw.shot;

/**
 * Created by Сергей on 19.11.2016.
 */

public class TimeNotification extends BroadcastReceiver {
    final String LOG_TAG = "myLogs";
    SimpleDateFormat formatt = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    //приемник
    public void onReceive(Context context, Intent intent) {
        // Этот метод будет вызываться по событию, сочиним его позже
        String timerSignal= formatt.format(new Date(System.currentTimeMillis()));

        Log.d(LOG_TAG,"sysTime "+timerSignal);
        SoundPool sp;
        Log.d(LOG_TAG, "onReceive");
        Log.d(LOG_TAG, "action = " + intent.getAction());
        Log.d(LOG_TAG, "extra = " + intent.getStringExtra("extra"));
        ////////////////////////////////////////////уведомление
        //Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notifications_black_24dp);
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);//

        PendingIntent contentIntent = PendingIntent.getActivity (context,
                0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);//context
        builder.setContentIntent(contentIntent)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
               // .setLargeIcon(largeIcon)
                .setTicker("Последнее китайское предупреждение!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Напоминание")
                .setContentText("Пора принять лекарство"); // Текст уведомлени
        //builder.setSound(R.raw.shot);

        try {
            Uri uri = Uri.parse("android.resource://"+"com.ru54.avd.menu" + "/"+R.raw.shot);//получение ресурса звука
            builder.setSound(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Notification n = builder.build();//getNotification();
        n.flags = Notification.FLAG_INSISTENT;//настойчивое сообщение

        nm.notify(111, n);
        //context.startService(new Intent(context, MyService.class));


    }
}
