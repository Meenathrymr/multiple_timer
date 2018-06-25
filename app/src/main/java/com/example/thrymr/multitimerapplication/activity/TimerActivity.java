package com.example.thrymr.multitimerapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thrymr.multitimerapplication.R;
import com.example.thrymr.multitimerapplication.activity.model.SkuInfo;

import java.util.ArrayList;
import java.util.List;

public class TimerActivity extends AppCompatActivity {

    final static private List<TimerAdapter.TimerHolder> lstHolders = new ArrayList<>();

    private Intent serviceIntent;

    private String cTime = "";

    private TimerBroadCast myReceiver;

    public static String action_network = "action_network";

    private static Long pausedTime;


    private TimerAdapter timerAdapter;

    private List<SkuInfo> skuList;


    private RecyclerView timerRecyclerView;

    public TimerActivity() {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        skuList = SkuInfo.listAll(SkuInfo.class);

        timerRecyclerView = findViewById(R.id.recycler_view_timer);
        timerRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        timerAdapter = new TimerAdapter(skuList);
        timerRecyclerView.setAdapter(timerAdapter);

        serviceIntent = new Intent(TimerActivity.this, CounterService.class);

        myReceiver = new TimerBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action_network);
        registerReceiver(myReceiver, intentFilter);


    }

    private class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerHolder> {

        private List<SkuInfo> mSkuInfoList;

        TimerAdapter(List<SkuInfo> skuList) {
            mSkuInfoList = skuList;
            Log.d("TimerActivity", "Adapter");
        }

        @NonNull
        @Override
        public TimerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.item_timer_activity, parent, false);
            TimerHolder vh = new TimerHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final TimerHolder holder, int position) {

            final SkuInfo info = mSkuInfoList.get(position);
            Log.d("onBindViewHolder", "info" + info);
            holder.setData(System.currentTimeMillis(), info, holder);


            holder.ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ivPlay.setVisibility(View.GONE);
                    holder.ivPause.setVisibility(View.VISIBLE);

                    info.setStartedRunning(Boolean.TRUE);
                    info.setStartTimeInMillis(convertStringToMilliSeconds(info));
                    info.save();
                    Log.d("Clicked Item", "Play Start Time" + info);

                    serviceIntent.putExtra("start_time", System.currentTimeMillis());
                    startService(serviceIntent);
                }
            });
            holder.ivPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ivPause.setVisibility(View.GONE);
                    holder.ivPlay.setVisibility(View.VISIBLE);

                    savePausedTime(info);

                    info.setStartedRunning(Boolean.FALSE);
                    info.save();


                    stopService(serviceIntent);
                    stopTimer();
                    holder.setSkuData(info, holder);
                    Log.d("Clicked Item", "Pause Pause Time" + info);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mSkuInfoList.size();
        }

        public class TimerHolder extends RecyclerView.ViewHolder {

            TextView tvSkuId, tvCurrentTime, tvTotalTime;
            ImageView ivPlay, ivPause;

            SkuInfo mInfo;

            TimerHolder(View itemView) {
                super(itemView);

                tvSkuId = itemView.findViewById(R.id.sku_id);
                tvCurrentTime = itemView.findViewById(R.id.tv_current_time);
                tvTotalTime = itemView.findViewById(R.id.tv_total_time);
                ivPlay = itemView.findViewById(R.id.iv_play_icon);
                ivPause = itemView.findViewById(R.id.iv_pause_icon);

            }

            void setData(long time, SkuInfo info, TimerHolder holder) {
                mInfo = info;
                updateTimeRemaining(time);
                setSkuData(info, holder);
            }

            SkuInfo getData() {
                return mInfo;
            }

            public void updateTimeRemaining(long currentTime) {

                if (mInfo.getStartedRunning()) {

                    Long updatedTime = System.currentTimeMillis() - mInfo.getStartTimeInMillis();
                    int seconds = (int) (updatedTime / 1000) % 60;
                    int minutes = (int) ((updatedTime / (1000 * 60)) % 60);
                    int hours = (int) ((updatedTime / (1000 * 60 * 60)));

                    cTime = "" + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);

                    tvCurrentTime.setText(cTime);

                }
            }

            private void setSkuData(SkuInfo info, TimerHolder holder) {

                holder.tvSkuId.setText(info.getSkuId());
                holder.tvCurrentTime.setText(info.getPausedTime());
                holder.tvTotalTime.setText(info.getPausedTime());

                if (holder.mInfo.getStartedRunning()) {
                    holder.ivPlay.setVisibility(View.GONE);
                    holder.ivPause.setVisibility(View.VISIBLE);
                } else {
                    holder.ivPlay.setVisibility(View.VISIBLE);
                    holder.ivPause.setVisibility(View.GONE);
                }

                synchronized (lstHolders) {
                    lstHolders.add(holder);
                }

            }
        }
    }

    private void savePausedTime(SkuInfo info) {
        info.setPausedTime(cTime);
    }


    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {

            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (TimerAdapter.TimerHolder holder : lstHolders) {
                    holder.updateTimeRemaining(currentTime);

                }
            }
            mHandler.postDelayed(this, 1000);

        }
    };

    private static final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            final int what = msg.what;
            switch (what) {
                case 1:

                    synchronized (lstHolders) {
                        long currentTime = System.currentTimeMillis();
                        for (TimerAdapter.TimerHolder holder : lstHolders) {
                            holder.updateTimeRemaining(currentTime);


                            if (holder.mInfo.getStartedRunning()) {
                                holder.mInfo.setEndTimeInMillis(pausedTime);
                                holder.mInfo.save();
                                Log.d("TimerTAct", "what" + holder.mInfo);
                            }
                        }
                    }
                    break;
            }
        }
    };


    private void stopTimer() {
        mHandler.removeCallbacks(updateRemainingTimeRunnable);
    }

    public static class TimerBroadCast extends BroadcastReceiver {

        public TimerBroadCast() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            pausedTime = intent.getLongExtra("end_time", 0);
            mHandler.sendEmptyMessage(1);


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TimerActivity", "onDestroy");
        //unregisterReceiver(myReceiver);

       /* synchronized (lstHolders) {
            for (TimerAdapter.TimerHolder holder : lstHolders) {
                holder.mInfo.setStartedRunning(Boolean.FALSE);
                holder.mInfo.save();
            }
        }*/
    }

    Long convertStringToMilliSeconds(SkuInfo mInfo) {

        if (mInfo.getEndTimeInMillis() == 0) {
            return System.currentTimeMillis();
        } else {
            return System.currentTimeMillis() - (mInfo.getEndTimeInMillis() - mInfo.getStartTimeInMillis());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TimerActivity", "onResume");

        serviceIntent = new Intent(TimerActivity.this, CounterService.class);
        serviceIntent.putExtra("start_time", System.currentTimeMillis());
        startService(serviceIntent);

    }
}
