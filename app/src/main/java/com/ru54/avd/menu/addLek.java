package com.ru54.avd.menu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.util.ArrayMap;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class addLek extends AppCompatActivity {
    Button btnOk;
    ListView ViborLv;
    EditText nameLek;
    EditText doza;
    EditText interval;
    DBHelper dbHelper;
    SQLiteDatabase sqdb;
    Cursor idlek;
    ListView lvMain;
    int[] iid;
    String[] timer;//массив для времени режима
    int n;//count
    String[] names;
    private Cursor curs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lek);
        ////////////////////////////////////////для вызова таймера

        ////////////////////////////////////////////////////////
        btnOk = (Button) findViewById(R.id.buttonAdd);
        ViborLv = (ListView) findViewById(R.id.RezhimLek);
        nameLek =(EditText) findViewById(R.id.lekName);
        doza = (EditText) findViewById(R.id.doza);
        interval = (EditText) findViewById(R.id.interval);
        dbHelper = new DBHelper(this);
        sqdb = dbHelper.getWritableDatabase();
        lvMain=(ListView) findViewById(R.id.RezhimLek);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ///чтение данных режима
        curs= sqdb.query("rezhim",null, null, null, null, null, null);
        n= curs.getCount();
        names = new String[n];
        iid = new int[n];
        timer = new String[n];
        int i =0;
        while (curs.moveToNext())
        {
            int idId= curs.getColumnIndex("id");//получаем номер столбца
            int nameId = curs.getColumnIndex("name");
            int timeId = curs.getColumnIndex("time");

            //int id = curs.getInt(idId);//получаю значения
            String id = curs.getString(idId);
            String name = curs.getString(nameId);
            String time = curs.getString(timeId);
            timer[i]= time;
            iid[i] = curs.getInt(idId);//получаю ид .
            names[i] = id +" "+ name +" "+ time;
            //Log.d("P","IID*************************************************");
            //Log.d("IID",""+iid[i]);
            i=i+1;
        }
        //вывод списка
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, names);
        lvMain.setAdapter(adapter);
        ///////////////////////////////////////////////////исправить
        View.OnClickListener ClcbuttonOk = new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                long timeDatmil=0;
                long intervalmil=0;
                ///вставка лекарств в таблицу
                ContentValues value = new ContentValues();
                value.put("name",nameLek.getText().toString());
                value.put("doza",doza.getText().toString());
                value.put("interval",interval.getText().toString());
                sqdb.insert("lek",null,value);//вставка
                //получить ид строки
                //курсор
                idlek = sqdb.query("lek",new String[] {"id"},"name='"+nameLek.getText().toString()+"'",null,null,null,null);
                idlek.moveToFirst();
                int idstolb=idlek.getColumnIndex("id");//получаем номер столбца
                String idl= idlek.getString(idstolb);//получил ид строки лекарства
                //перевод интервала в миллисекунды
                SimpleDateFormat formatt = new SimpleDateFormat("HH:mm", Locale.ENGLISH);//формат для получения часов и минут
                SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);//формат день месяц год
                SimpleDateFormat formatAll = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);//формат день месяц год
               //////////////////////////////////////////////////
                String d=formatDate.format(System.currentTimeMillis());
                /////////////////////////////////////////////////////
                try {//требует обработку исключения
                    Log.d("MyLog","date"+d);
                 timeDatmil = formatDate.parse(d).getTime();//получение миллисекунд
                    Log.d("Mylog","---------------------------------------------------");
                    Log.d("Mylog","Date "+formatAll.format(timeDatmil));
                    Log.d("Mylog","---------------------------------------------------");
                //System.currentTimeMillis()
                } catch (ParseException ex) {
                    System.out.println("Это не должно произойти");
                    Log.d("Mylog","Это не должно произойти");

                }
                intervalmil = Integer.parseInt(interval.getText().toString())*60000;//получение миллисекунд из поля
                Log.d("intervalmil", "intervalmil_" + intervalmil);

                // добавление режима
                SparseBooleanArray sbArray = lvMain.getCheckedItemPositions();
                for ( int i = 0; i < sbArray.size(); i++) {
                    Log.d("iiii","*************************************************");
                    Log.d("i",""+i);
                    int key = sbArray.keyAt(i);
                    Log.d("key",""+sbArray.get(key));
                    if (sbArray.get(key))
                    {
                        //время сложить
                         //.parse( timer[key]);
                        ////////////////////////////////////
                        String t= d +" "+timer[key];
                        ///////////////////////////////////
                        long timerSignalmil=0;
                        try {//требует обработку исключения
                            long timemil = formatAll.parse(t).getTime();//получение миллисекунд
                            Log.d("P","*************************************************");
                            Log.d("T","t_"+timemil);
                            Log.d("TS","ts"+SystemClock.elapsedRealtime());
                            String timeTest= formatAll.format(new Date(timemil));
                            Log.d("timeTest","t_"+timeTest);


                            timerSignalmil=timemil+intervalmil;
                            timeTest= formatAll.format(new Date(timerSignalmil));
                            Log.d("timerSignalmil_time","t "+timeTest);
                           // Log.d("timerSignalmil","timerSignalmil_"+timerSignalmil);
                        } catch (ParseException ex) {
                            System.out.println("Это не должно произойти");
                            Log.d("CATCH","Это не должно произойти");
                        }
                        String timerSignal= formatt.format(new Date(timerSignalmil));////получаю время запуска
                        Log.d("P","*************************************************");
                        Log.d("T","t_"+t);
                        Log.d("T","interval_"+interval);
                        Log.d("TSys","tsys"+System.currentTimeMillis());
                        Log.d("mil","timersignalmil_"+timerSignalmil);
                        Log.d("T","timerSignal_"+timerSignal);
                        Log.d("T","tsys String_"+formatAll.format(new Date(System.currentTimeMillis())));
                        //////////////////////////////////////запись режима для лекарства
                        ContentValues value2 = new ContentValues();
                        value2.put("idlek",idl);
                        value2.put("time",timerSignal);
                        value2.put("idrezh",key);

                        sqdb.insert("lekrezhim",null,value2);//вставка
                        Cursor idlekrezhim=sqdb.query("lekrezhim",new String[] {"id"},"time='"+timerSignal+"'",null,null,null,null);//получение ид одного столбца сэтим временем
                        idlekrezhim.moveToFirst();
                        idstolb=idlekrezhim.getColumnIndex("id");
                        int idlr=idlekrezhim.getInt(idstolb);//ид лекарства
                        //будильник
                        AlarmManager    am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent intent2 = new Intent(addLek.this , TimeNotification.class);
                        //Intent intent2 = new Intent(addLek.this , Napomnil.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(addLek.this, idlr,
                                intent2, PendingIntent.FLAG_CANCEL_CURRENT );//
                        am.cancel(pendingIntent);//am.setInexactRepeating
                        //am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000,30*1000,pendingIntent);
                        //установил время запуска
                        Log.d("T","__________________");
                        Log.d("T","timerSignalmil "+timerSignalmil);
                        Log.d("T","timeDatmil "+timeDatmil);
                        //long st=timerSignalmil+timeDatmil;
                        //Log.d("T","SUM "+st);
                        Log.d("T","Время запуска "+formatAll.format(timerSignalmil));
                        am.setRepeating(AlarmManager.RTC_WAKEUP,timerSignalmil,24*60*60*1000,pendingIntent);//(AlarmManager.RTC_WAKEUP,timerSignal., pendingIntent);
                        //24*60*60*1000  AlarmManager.RTC_WAKEUP
                        // На случай, если мы ранее запускали активити, а потом поменяли время,
                        // откажемся от уведомления

                        // Устанавливаем разовое напоминание


                        /*myDatabase.delete(DATABASE_TABLE,
                                "NAME = ?",
                                new String[] {"Murzik"});*/
                        /*Toast toast = Toast.makeText(getApplicationContext(),
                                iid[i]  , Toast.LENGTH_SHORT);*/
                        //toast.show();
                       /* Log.d("P","*************************************************");
                        Log.d("I","I_"+i);
                        Log.d("id","id_"+iid[i]);
                        Log.d("P","*************************************************");*/
                        //sqdb.delete("rezhim", "id = " + iid[key], null);//key указывает позицию
                       /* sqdb.execSQL("INSERT INTO lekrezhim ("
                                + "id integer primary key autoincrement,"
                                + idlek.
                                + "idrezh"+ ");");*///откорректировать
                    }

                }
                //возврат в главное активити
                Intent intent = new Intent(addLek.this, MainActivity.class);
                startActivity(intent);//вызываю активити Main
            }
        };
        btnOk.setOnClickListener(ClcbuttonOk);



    }
}
