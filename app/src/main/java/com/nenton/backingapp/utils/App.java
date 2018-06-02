package com.nenton.backingapp.utils;

import android.app.Application;

import com.facebook.stetho.Stetho;

import io.realm.Realm;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Stetho.initializeWithDefaults(this);
    }
}
