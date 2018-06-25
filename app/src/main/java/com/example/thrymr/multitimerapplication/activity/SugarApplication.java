package com.example.thrymr.multitimerapplication.activity;

import android.content.Context;

import com.orm.SugarApp;

/**
 * Created by thrymr.
 * <p>
 * This class to enable the Multidex functionality in the application to run multiple process at a time.
 */
public class SugarApplication extends SugarApp {


    private static SugarApplication mInstance;

    public static synchronized SugarApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

}
