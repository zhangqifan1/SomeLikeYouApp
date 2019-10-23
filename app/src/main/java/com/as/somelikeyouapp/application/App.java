package com.as.somelikeyouapp.application;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.as.base_lib_sly.base.application.BaseApplication;

/**
 * -----------------------------
 * Created by zqf on 2019/10/16.
 * ---------------------------
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void initThirdParty() {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
