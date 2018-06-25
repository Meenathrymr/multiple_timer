package com.example.thrymr.multitimerapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.thrymr.multitimerapplication.R;
import com.example.thrymr.multitimerapplication.activity.model.SkuInfo;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("TIMER_PREFS", Context.MODE_PRIVATE);


        if (preferences.getBoolean("SKU_DOWNLOADED", true)) {
            prepareSkuData();
            preferences.edit().putBoolean("SKU_DOWNLOADED", false).apply();
        }

        findViewById(R.id.hello_word).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TimerActivity.class));
            }
        });
    }

    private void prepareSkuData() {

        SkuInfo.deleteAll(SkuInfo.class);


        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSkuId("123");
        skuInfo.setShipmentId("ABCD");
        skuInfo.save();

        SkuInfo skuInfo1 = new SkuInfo();
        skuInfo1.setSkuId("124");
        skuInfo1.setShipmentId("ABCD");
        skuInfo1.save();

        SkuInfo skuInfo2 = new SkuInfo();
        skuInfo2.setSkuId("125");
        skuInfo2.setShipmentId("ABCD");
        skuInfo2.save();


    }


}
