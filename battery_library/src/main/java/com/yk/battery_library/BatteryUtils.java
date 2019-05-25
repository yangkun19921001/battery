package com.yk.battery_library;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class BatteryUtils {

    /**
     * 闹钟 action
     */
    public static final String ALARM_ACTIOM = "com.yk.ALARM_TIME";

    /**
     * 闹钟 id
     */
    public static final int ALARM_TIME_ID = 0x520;

    /**
     * 闹钟 重复 4s 重复一次
     */
    public static final int ALARM_TIME_INTERVAL = 4 * 1000;


    /**
     * 加入电量白名单
     *
     * @param activity
     * @param type 1: 启动 设置页面 2：触发系统对话框
     */
    public static void addWhite(Activity activity, int type) {
        if (activity == null)return;
        WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(activity);
        PowerManager packageManager = (PowerManager) activityWeakReference.get().getApplication()
                .getSystemService(Context.POWER_SERVICE);
        //应用是否在 白名单中
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!packageManager.isIgnoringBatteryOptimizations(activityWeakReference.get().getApplication().getPackageName())) {
                if (type == 1) {
                    //方法1、启动一个  ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS Intent
                    Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    activityWeakReference.get().getApplication().startActivity(intent);
                } else {
                    //方法2、触发系统对话框
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + activityWeakReference.get().getApplication().getPackageName()));
                    activityWeakReference.get().getApplication().startActivity(intent);
                }
            }
        }
    }

    /**
     * 是否正在充电
     *
     * @return
     */
    public static boolean isPlugged(Context context) {
        //发送个包含充电状态的广播，并且是一个持续的广播
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = context.registerReceiver(null, filter);
        //获取充电状态
        int isPlugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean acPlugged = isPlugged == BatteryManager.BATTERY_PLUGGED_AC;
        boolean usbPlugged = isPlugged == BatteryManager.BATTERY_PLUGGED_USB;
        boolean wifiPlugged = isPlugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
        return acPlugged || usbPlugged || wifiPlugged;
    }


    /**
     * 开启一个闹钟
     * @param context
     * @param action
     * @param requestId
     * @param interval
     */
    public static void startTimer(Context context,String action,int requestId,int interval) {
        Intent intent = new Intent(action);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        //延迟一分钟执行
        int delay = 60 - second + 1;
        calendar.add(Calendar.SECOND, delay);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(calendar.getTimeInMillis());
            Log.d("下次闹钟执行的时间--》","delay: " + delay +", startMillis: " +format);
            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 100, sender);

        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, sender);
        }
    }

    /**
     * 是否正在使用wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //获得当前活动的网络信息
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (null != activeNetworkInfo && activeNetworkInfo.isConnected() &&
                activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
