package com.ihandy.n2013010952;

import android.app.Application;
import android.content.Context;

/**
 * Created by neozero on 8/26/16.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}