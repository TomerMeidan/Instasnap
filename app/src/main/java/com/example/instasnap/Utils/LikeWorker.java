package com.example.instasnap.Utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class LikeWorker extends Worker {

    public LikeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Intent intent = new Intent();
        intent.setAction("com.example.instasnap.LIKE_NOTIFICATION");

        while(true){

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            this.getApplicationContext().sendBroadcast(intent);
        }
    }
}
