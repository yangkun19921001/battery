package com.yk.battery_library;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import static com.yk.battery_library.BatteryUtils.ALARM_TIME_ID;
import static com.yk.battery_library.BatteryUtils.ALARM_ACTIOM;
import static com.yk.battery_library.BatteryUtils.ALARM_TIME_INTERVAL;

public class AlarmReceiver extends BroadcastReceiver {

    private  final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(ALARM_ACTIOM);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, ALARM_TIME_ID, i, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ALARM_TIME_INTERVAL, 100, sender);
        }
        Toast.makeText(context,"收到定时任务"+intent.getAction(),Toast.LENGTH_SHORT).show();
    }

}
