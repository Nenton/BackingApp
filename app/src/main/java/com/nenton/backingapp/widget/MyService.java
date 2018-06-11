package com.nenton.backingapp.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class MyService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.e("MyService", "onGetViewFactory");
        return new MyFactory(getApplicationContext(), intent);
    }
}
