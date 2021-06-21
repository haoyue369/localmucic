package com.dyql.media.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public abstract class BaseService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    protected void init() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

}
