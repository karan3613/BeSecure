package com.example.womensafety;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class runServiceAgain extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.d("Check: ","Receiver Started");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, SensorsService.class));
        } else {
            context.startService(new Intent(context, SensorsService.class));
        }
    }
    }

