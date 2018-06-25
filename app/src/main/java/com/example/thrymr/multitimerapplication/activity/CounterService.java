package com.example.thrymr.multitimerapplication.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class CounterService extends Service {

    private Handler handler = new Handler();
    Long startTime = 0L;
    Long endTime;

    public CounterService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {

            if (startTime != 0L) {
                endTime = System.currentTimeMillis();

                DisplayLoggingInfo();
                handler.postDelayed(this, 0);
            }
        }
    };

    private void DisplayLoggingInfo() {

        Intent intent = new Intent(this, TimerActivity.TimerBroadCast.class);
        intent.putExtra("end_time", endTime);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("CounterService", "onDestroy");
        super.onDestroy();
        handler.removeCallbacks(sendUpdatesToUI);
        startTime = 0L;

    }


    @Override
    public ComponentName startService(Intent service) {

        return super.startService(service);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTime = (intent.getExtras().getLong("start_time"));
        Log.d("Service", "service");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}