package com.ru54.avd.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Сергей on 23.12.2016.
 */

public class MyBroadReceiv extends BroadcastReceiver {

    final String LOG_TAG = "myLogs";

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {//проверка
            Log.d(LOG_TAG, "onReceive " + intent.getAction());
            context.startService(new Intent(context, MyService.class));
        }
    }
}
