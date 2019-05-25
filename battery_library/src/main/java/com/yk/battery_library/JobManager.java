package com.yk.battery_library;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;

import java.util.List;


public class JobManager {

    static JobManager instance;
    //把一些不是特别紧急(实时)的任务放到更合适的时机批量处理
    // 1、避免频繁的唤醒硬件模块
    // 2、避免在不合适的时候执行一些耗电的任务
    private JobScheduler jobScheduler;
    private Context context;

    private static final int jobId=0;

    public static JobManager getInstance() {
        if (null == instance)
            instance = new JobManager();
        return instance;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

    }

    /**
     * 添加一个任务
     * @param location
     */
    public void addJob(String location) {
        if (null == jobScheduler) {
            return;
        }
        JobInfo pendingJob = null;
        //整合多个job
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //查找id是0的job
            pendingJob = jobScheduler.getPendingJob(jobId);
        } else {
            List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
            for (JobInfo info : allPendingJobs) {
                if (info.getId() == jobId) {
                    pendingJob = info;
                    break;
                }
            }
        }
        //找到待执行的job
        if (null != pendingJob) {
            //多个坐标信息拼到一起 上传
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //数据 与Intent 一样
                PersistableBundle extras = pendingJob.getExtras();
                //获得上一次设置的location数据
                String data = extras.getString("DATA");
                //比如 多条坐标数据用@隔开
                location = data + "@" + location;
                jobScheduler.cancel(jobId);
            }
        }
        // jobid ：0
        PersistableBundle extras = new PersistableBundle();
        extras.putString("DATA",location);
        //创建一个job
        JobInfo jobInfo = new
                JobInfo.Builder(jobId,
                new ComponentName(context, MyJobService.class))
                //只在充电的时候
                .setRequiresCharging(true)
                //不是蜂窝网络
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setExtras(extras).build();

        //提交任务
        jobScheduler.schedule(jobInfo);
    }

}
