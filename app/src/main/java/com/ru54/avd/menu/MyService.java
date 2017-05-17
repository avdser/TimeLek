package com.ru54.avd.menu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Сергей on 16.12.2016.
 */

public class MyService extends Service {
    final String LOG_TAG = "myLogs";
    DBHelper dbHelper;
    SQLiteDatabase sqdb;
    Cursor curs;

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        someTask();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }


    void someTask() {
        new Thread(new Runnable() {//поток запускаю
            public void run() {
                //Intent intent2= new Intent(this,MainActivity.class);
                //startActivity(intent2);
                //int idstolb;
                dbHelper = new DBHelper(MyService.this);
                sqdb = dbHelper.getWritableDatabase();
                SimpleDateFormat formatt = new SimpleDateFormat("HH:mm", Locale.ENGLISH);//формат для получения часов и минут
                SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);//формат день месяц год
                SimpleDateFormat formatAll = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);//формат день месяц год
                String sqlQuery="select lr.time as time from lek as l inner join lekrezhim as lr on l.id=lr.idlek order by lr.time";
                curs=sqdb.rawQuery(sqlQuery,null);
                //curs = sqdb.query("lekrezhim", new String[]{"time"}, null, null, "time", null, null);
                String d = formatDate.format(System.currentTimeMillis());//текущая дата
                int nomer = 1000;//номер аларма
                while (curs.moveToNext()) {
                    long timeDatmil = 0;
                    int idstolb = curs.getColumnIndex("time");
                    String time = curs.getString(idstolb);//получил время из базы
                    String dt = d + " " + time;
                    try {//требует обработку исключения
                        Log.d("MyLog", "date" + d);
                        timeDatmil = formatAll.parse(dt).getTime();//получение миллисекунд
                        Log.d("Mylog", "--Дата и время запуска-------------------------------------------------");
                        Log.d("Mylog", "Date " + dt);
                        Log.d("Mylog", "---------------------------------------------------");
                        //System.currentTimeMillis()
                    } catch (ParseException ex) {
                        System.out.println("Это не должно произойти");
                        Log.d("Mylog", "Это не должно произойти");
                    }
                    //будильник
                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent2 = new Intent(MyService.this, TimeNotification.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MyService.this, nomer,
                            intent2, PendingIntent.FLAG_CANCEL_CURRENT);//
                    am.cancel(pendingIntent);//am.setInexactRepeating
                    am.setRepeating(AlarmManager.RTC_WAKEUP, timeDatmil, 24 * 60 * 60 * 1000, pendingIntent);
                    nomer = nomer + 1;
                }
                stopSelf();
            }
        }).start();
    }
}/////////

