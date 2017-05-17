package com.ru54.avd.menu;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ListView listLekRezh;
    Cursor c;
    String[] spisok;
    int n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase sqdb;
        dbHelper = new DBHelper(this);
        listLekRezh = (ListView) findViewById(R.id.listLekRezh);
        //listLekRezh.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//режим выбора
        sqdb = dbHelper.getWritableDatabase();
       //Cursor curs= sqdb.query("lekrezhim",new String[] {"time"},null,null,null,null,null);
        //Log.d("Curs","count="+curs.getCount());
        ///вывод расписания приема
        //запрос
        String sqlQuery="select l.name as name,l.doza as doza,lr.time as time from lek as l inner join lekrezhim as lr on l.id=lr.idlek order by lr.time";
        c=sqdb.rawQuery(sqlQuery,null);
        n=c.getCount();
        spisok = new String[n];
        int i = 0;
        while (c.moveToNext()) {
            int idname = c.getColumnIndex("name");//получаем номер столбца
            int idtime = c.getColumnIndex("time");//получаем номер столбца
            int iddoza = c.getColumnIndex("doza");//получаем номер столбца
            String name = c.getString(idname);
            String time = c.getString(idtime);
            String doza = c.getString(iddoza);
            spisok[i]=name +" "+doza+" "+time;
            i=i+1;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, spisok);//simple_list_item_multiple_choice
        listLekRezh.setAdapter(adapter);
        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
            }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();

        TextView infoTextView = (TextView) findViewById(R.id.textView);

        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.rezhimAd:
                dbHelper.close();//закрываю
                infoTextView.setText("Добавь пункт режима");
                Intent intent = new Intent(MainActivity.this,AdRezhim.class);
                startActivity(intent);//вызываю активити
                return true;
            case R.id.rezhimDel:
                infoTextView.setText("Удалить пункт режима");
                Intent intent2 = new Intent(MainActivity.this,DelRezhim.class);
                startActivity(intent2);//вызываю активити
                return true;
            case R.id.rezhimRed:
                infoTextView.setText("Редактировать пункт режима");
                return true;
            case R.id.spisokLekarstvAd:
                infoTextView.setText("Добавь лекарство");
                Intent intent4 = new Intent(MainActivity.this,addLek.class);
                startActivity(intent4);//вызываю активити
                return true;

            case R.id.spisokLekarstvDel:
                infoTextView.setText("Удали лекарство");
                Intent intent5 = new Intent(MainActivity.this,delLek.class);
                startActivity(intent5);//вызываю активити
                return true;
            case R.id.spisokLekarstvRed:
                infoTextView.setText("Редактируй лекарства");
                return true;
            case R.id.Exit:
                infoTextView.setText("Выход");
                //Application;
                return true;
            case R.id.ServerStart:
                infoTextView.setText("Запуск сервера");
                startService(new Intent(this, MyService.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        
    }
}

class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "db", null, 15);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table rezhim ("
                + "id integer primary key autoincrement,"
                + "name,"
                + "time" + ");");
        db.execSQL("create table lek ("
                + "id integer primary key autoincrement,"
                + "name,"
                + "doza,"
                + "interval" + ");");
        db.execSQL("create table lekrezhim ("
                + "id integer primary key autoincrement,"
                + "idlek,"
                + "idrezh," + "time" + ");");//таблица с идами лекарств, выбранного режима, время према

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {//обновляю базу
            db.execSQL("create table lekrezhim ("
                    + "id integer primary key autoincrement,"
                    + "idlek,"
                    + "idrezh" + ");");//создаю таблицу
            db.execSQL("drop table lek;");//создаю таблицу
            db.execSQL("create table lek ("
                    + "id integer primary key autoincrement,"
                    + "name,"
                    + "doza,"
                    + "interval" + ");");

        }
        if (newVersion == 15) {
             db.execSQL("drop table lekrezhim;");
            db.execSQL("create table lekrezhim ("
                    + "id integer primary key autoincrement,"
                    + "idlek,"
                    + "idrezh," + "time" + ");");


        }
    }
}


