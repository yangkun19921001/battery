package com.yk.battery_library;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.util.Log;


public class MyJobService extends JobService {

    public static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        //如果返回值是false,这个方法返回时任务已经执行完毕。
        //如果返回值是true,那么这个任务正要被执行，我们就需要开始执行任务。
        //当任务执行完毕时你需要调用jobFinished(JobParameters params, boolean needsRescheduled)来通知系统
        new MyAsyncTask().execute(params);
        return true;
    }

    //当系统接收到一个取消请求时
    @Override
    public boolean onStopJob(JobParameters params) {
        //如果onStartJob返回false,那么onStopJob不会被调用
        // 返回 true 则会重新计划这个job
        return false;
    }


    /**
     * Params:启动任务时输入的参数类型.
     * <p>
     * Progress:后台任务执行中返回进度值的类型.
     * <p>
     * Result:后台任务执行完成后返回结果的类型.
     */
    class MyAsyncTask extends AsyncTask<JobParameters, Void, Void> {
        JobParameters jobParameters;

        @Override
        protected Void doInBackground(JobParameters[] objects) {
            jobParameters = objects[0];
            Log.i(TAG, jobParameters.getJobId() + " 任务开始执行......");
            PersistableBundle extras = jobParameters.getExtras();
            String location = extras.getString("DATA");
            Log.i(TAG, jobParameters.getJobId() + " 网络请求上传:" + location);
            return null;
        }

        /**
         * doInBackground:必须重写,异步执行后台线程要完成的任务,耗时操作将在此方法中完成.
         * <p>
         * onPreExecute:执行后台耗时操作前被调用,通常用于进行初始化操作.
         * <p>
         * onPostExecute:当doInBackground方法完成后,系统将自动调用此方法,并将doInBackground方法返回的值传入此方法.通过此方法进行UI的更新.
         * <p>
         * onProgressUpdate:当在doInBackground方法中调用publishProgress方法更新任务执行进度后,将调用此方法.通过此方法我们可以知晓任务的完成进度.
         */
        @Override
        protected void onPostExecute(Void s) {
            //当任务执行完毕之后，需要调用jobFinished来让系统知道这个任务已经结束，
            //系统可以将下一个任务添加到队列中
            //true表示需要重复执行
            //false反之
            jobFinished(jobParameters, false);
            Log.i(TAG, jobParameters.getJobId() + "任务执行完成......");
        }
    }
}
