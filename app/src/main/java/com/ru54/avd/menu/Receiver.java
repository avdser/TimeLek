package com.ru54.avd.menu;

/**
 * Created by Сергей on 25.11.2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.media.SoundPool.OnLoadCompleteListener;
import java.io.IOException;

public class Receiver extends BroadcastReceiver{


    int soundIdShot;
    int soundIdExplosion;
    final String LOG_TAG = "myLogs";
    final int MAX_STREAMS = 5;

    int streamIDShot;
    int streamIDExplosion;
    @Override
    public void onReceive(Context ctx, Intent intent) {
        SoundPool sp;
        Log.d(LOG_TAG, "onReceive");
        Log.d(LOG_TAG, "action = " + intent.getAction());
        Log.d(LOG_TAG, "extra = " + intent.getStringExtra("extra"));

        //startActivity(intent);//вызываю активити
        /////////////////////////////////////
    }
}
