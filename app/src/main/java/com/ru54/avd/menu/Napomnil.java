package com.ru54.avd.menu;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class Napomnil extends AppCompatActivity {
    int soundIdShot;
    int soundIdExplosion;
    final String LOG_TAG = "myLogs";
    final int MAX_STREAMS = 5;
    SoundPool sp;
    int streamIDShot;
    int streamIDExplosion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_napomnil);
        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
       // sp.setOnLoadCompleteListener(this);

        soundIdShot = sp.load(this, R.raw.shot, 1);
        Log.d(LOG_TAG, "soundIdShot = " + soundIdShot);

        try {
            soundIdExplosion = sp.load(getAssets().openFd("explosion.ogg"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "soundIdExplosion = " + soundIdExplosion);
    }
}
