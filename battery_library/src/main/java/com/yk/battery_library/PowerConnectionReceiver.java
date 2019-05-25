package com.yk.battery_library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

/**
 *  被动接收充电状态
 */
public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action,Intent.ACTION_POWER_CONNECTED)){
            Toast.makeText(context,"当前正在充电",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.equals(action,Intent.ACTION_POWER_DISCONNECTED)){
            Toast.makeText(context,"当前不在充电",Toast.LENGTH_SHORT).show();
        }
    }
}
