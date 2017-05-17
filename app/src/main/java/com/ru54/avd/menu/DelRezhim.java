package com.ru54.avd.menu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

//import java.sql.Time;

public class DelRezhim extends AppCompatActivity {
    Button buttonDel;
    ListView lvMain;
    private Cursor curs;
    SQLiteDatabase sqdb;
    DBHelper dbHelper;
    String[] names;
    int[] iid;
    int n;//count
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_rezhim);
        dbHelper = new DBHelper(this);
        buttonDel = (Button) findViewById(R.id.buttonDel);
        lvMain = (ListView) findViewById(R.id.lvMain);
        // устанавливаем режим выбора пунктов списка
        lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        sqdb = dbHelper.getReadableDatabase(); //getWritableDatabase();
        curs= sqdb.query("rezhim",null, null, null, null, null, null);
        n= curs.getCount();
        names = new String[n];
        iid = new int[n];
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
            iid[i] = curs.getInt(idId);
            names[i] = id +" "+ name +" "+ time;
            //Log.d("P","IID*************************************************");
            //Log.d("IID",""+iid[i]);
            i=i+1;
        }
        //simple_list_item_multiple_choice - позволяет множественный выбор
        //simple_list_item_single_choice-делает одиночный выбор
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, names);
        lvMain.setAdapter(adapter);
        /////////////
        View.OnClickListener ClcbuttonDel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // удаление строк
                SparseBooleanArray sbArray = lvMain.getCheckedItemPositions();
                for ( int i = 0; i < sbArray.size(); i++) {
                    Log.d("iiii","*************************************************");
                    Log.d("i",""+i);
                    int key = sbArray.keyAt(i);
                    Log.d("key",""+sbArray.get(key));
                    if (sbArray.get(key))
                    {
                        /*Toast toast = Toast.makeText(getApplicationContext(),
                                iid[i]  , Toast.LENGTH_SHORT);*/
                        //toast.show();
                       /* Log.d("P","*************************************************");
                        Log.d("I","I_"+i);
                        Log.d("id","id_"+iid[i]);
                        Log.d("P","*************************************************");*/
                        sqdb.beginTransaction();
                        try {
                            //удаляем
                            sqdb.delete("rezhim", "id = " + iid[key], null);
                            sqdb.setTransactionSuccessful();
                        } finally {
                            sqdb.endTransaction();
                        }

                        Cursor c=sqdb.query("lekrezhim",null,"idrezh = "+iid[key],null,null,null,null);
                        Log.d("count","count="+c.getCount());
                        if (c.getCount()>0) {
                            sqdb.beginTransaction();
                            try {
                                //Вставляем данные
                                sqdb.delete("lekrezhim", "idrezh = " + iid[key], null);
                                sqdb.setTransactionSuccessful();
                            } finally {
                                sqdb.endTransaction();
                            }
                        }

                    }

                }
                //возврат в главное активити
                Intent intent = new Intent(DelRezhim.this, MainActivity.class);
                startActivity(intent);//вызываю активити Main
            }
        };
        buttonDel.setOnClickListener(ClcbuttonDel);

    }

}
