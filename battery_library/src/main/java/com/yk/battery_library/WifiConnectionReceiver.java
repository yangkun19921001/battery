package com.yk.battery_library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * 被动获取是否wifi
 */

public class WifiConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (BatteryUtils.isWifi(context)){
            Log.i("WIFI","当前正在使用wifi");
            Toast.makeText(context,"当前正在使用wifi",Toast.LENGTH_SHORT).show();
        } else{
            Log.i("WIFI","当前不正在使用wifi");
            Toast.makeText(context,"当前不在使用wifi",Toast.LENGTH_SHORT).show();
        }
    }
}
