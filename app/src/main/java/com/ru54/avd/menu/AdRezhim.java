package com.ru54.avd.menu;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdRezhim extends AppCompatActivity {
    EditText punktname;
    EditText punkttime;
    Button button3;
    DBHelper dbHelper;
    SQLiteDatabase sqdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_rezhim);
        //tvOut = (TextView) findViewById(R.id.tvOut);
       /* Intent intent = new Intent(MainActivity.this,AdRezhim.class);
        startActivity(intent);//вызываю активити*/
        punktname = (EditText) findViewById(R.id.NamePunkt);
        punkttime = (EditText) findViewById(R.id.TimePunkt);
        button3 = (Button) findViewById(R.id.button3);
        dbHelper = new DBHelper(this);
        sqdb = dbHelper.getWritableDatabase();
        View.OnClickListener Clcbutton3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues value = new ContentValues();
                value.put("name",punktname.getText().toString());
                value.put("time",punkttime.getText().toString());
                sqdb.insert("rezhim",null,value);
                dbHelper.close();//закрыл
               // TODO Auto-generated method stub
                //возврат в главное активити
                Intent intent = new Intent(AdRezhim.this, MainActivity.class);
                startActivity(intent);//вызываю активити Main
            }
        };
        button3.setOnClickListener(Clcbutton3);

    }
}

