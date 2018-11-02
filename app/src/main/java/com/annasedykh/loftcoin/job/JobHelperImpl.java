package com.annasedykh.loftcoin.job;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class JobHelperImpl implements JobHelper {

    private static final int SYNC_RATE_JOB_ID  = 123;

    private Context context;

    public JobHelperImpl(Context context) {
        this.context = context;
    }

    @Override
    public void startSyncRateJob(String symbol) {

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if(jobScheduler == null){
            return;
        }

        ComponentName componentName = new ComponentName(context, SyncRateJobService.class);
        PersistableBundle extra = new PersistableBundle();
        extra.putString(SyncRateJobService.EXTRA_SYMBOL, symbol);

        JobInfo jobInfo = new JobInfo.Builder(SYNC_RATE_JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setPeriodic(TimeUnit.MINUTES.toMillis(10))
                .setExtras(extra)
                .setPersisted(true)
                .build();

        int result = jobScheduler.schedule(jobInfo);
        if(result == JobScheduler.RESULT_SUCCESS){
            Toast.makeText(context,"sync rate job for " + symbol + " started", Toast.LENGTH_LONG).show();
        }

    }
}
