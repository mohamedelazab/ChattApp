package com.example.mohamed.chattapp.utils;

import android.app.Application;

import io.realm.Realm;

public class ChattApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
