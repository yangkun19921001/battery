package com.yk.battery;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yk.battery_library.AlarmReceiver;
import com.yk.battery_library.BatteryUtils;
import com.yk.battery_library.JobManager;

import static com.yk.battery_library.BatteryUtils.ALARM_ACTIOM;
import static com.yk.battery_library.BatteryUtils.ALARM_TIME_ID;
import static com.yk.battery_library.BatteryUtils.ALARM_TIME_INTERVAL;

public class MainActivity extends AppCompatActivity {

    private AlarmReceiver mAlarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //优化方案 一 ：加入电量优化白名单
        BatteryUtils.addWhite(this,2);

        //优化方案 二 ：网络数据切换
        if (!BatteryUtils.isPlugged(getApplicationContext())){
            //如果没有充电，提醒用户是否有 wiff 连接
        }

        //优化方案三 ：亮屏和唤醒 CPU 替换者
        //1.亮屏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //2. CPU 替换者 间断式唤醒 CPU
        mAlarmReceiver = new AlarmReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ALARM_ACTIOM);
        registerReceiver(mAlarmReceiver,intentFilter);
        BatteryUtils.startTimer(getApplicationContext(),ALARM_ACTIOM,ALARM_TIME_ID,ALARM_TIME_INTERVAL);

        // 优化方案 四 ：jobScheduler
        JobManager.getInstance().init(getApplicationContext());

        JobManager.getInstance().addJob("对于实时性要求不那么高的，" +
                "比如上传日志，请求版本更新！\n\n\n\n");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlarmReceiver != null)
            unregisterReceiver(mAlarmReceiver);
    }
}
