package com.ru54.avd.menu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class delLek extends AppCompatActivity {
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
        setContentView(R.layout.activity_del_lek);
        dbHelper = new DBHelper(this);
        buttonDel = (Button) findViewById(R.id.blekDel);
        lvMain = (ListView) findViewById(R.id.listDel);
        // устанавливаем режим выбора пунктов списка
        lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        sqdb = dbHelper.getReadableDatabase(); //getWritableDatabase();
        curs = sqdb.query("lek", null, null, null, null, null, null);
        n = curs.getCount();
        names = new String[n];
        iid = new int[n];
        int i = 0;
        while (curs.moveToNext()) {
            int idId = curs.getColumnIndex("id");//получаем номер столбца
            int nameId = curs.getColumnIndex("name");
            int dozaId = curs.getColumnIndex("doza");

            //int id = curs.getInt(idId);//получаю значения
            String id = curs.getString(idId);
            String name = curs.getString(nameId);
            String doza = curs.getString(dozaId);
            iid[i] = curs.getInt(idId);
            names[i] = id + " " + name + " " + doza;
            //Log.d("P","IID*************************************************");
            //Log.d("IID",""+iid[i]);
            i = i + 1;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, names);
        lvMain.setAdapter(adapter);
        View.OnClickListener ClcbuttonDel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // удаление строк
                SparseBooleanArray sbArray = lvMain.getCheckedItemPositions();
                for (int i = 0; i < sbArray.size(); i++) {
                    Log.d("iiii", "*************************************************");
                    Log.d("i", "" + i);
                    int key = sbArray.keyAt(i);
                    Log.d("key", "" + sbArray.get(key));
                    if (sbArray.get(key)) {
                        /*Toast toast = Toast.makeText(getApplicationContext(),
                                iid[i]  , Toast.LENGTH_SHORT);*/
                        //toast.show();
                       /* Log.d("P","*************************************************");
                        Log.d("I","I_"+i);
                        Log.d("id","id_"+iid[i]);
                        Log.d("P","*************************************************");*/
                        sqdb.beginTransaction();
                        try {
                            //Вставляем данные
                            sqdb.delete("lek", "id = " + iid[key], null);
                            sqdb.setTransactionSuccessful();
                        } finally {
                            sqdb.endTransaction();
                        }
                        //проверка наличая записи в таблице
                        Cursor c=sqdb.query("lekrezhim",null,"idlek = "+iid[key],null,null,null,null);
                        if (c.getCount()>0) {
                            sqdb.beginTransaction();
                            try {
                                //Вставляем данные
                                sqdb.delete("lekrezhim", "idlek = " + iid[key], null);
                                sqdb.setTransactionSuccessful();
                            } finally {
                                sqdb.endTransaction();
                            }
                        }

                    }

                }
                //возврат в главное активити
                Intent intent2 = new Intent(delLek.this, MainActivity.class);
                startActivity(intent2);//вызываю активити Main
            }

        };
        buttonDel.setOnClickListener(ClcbuttonDel);
    }
}
