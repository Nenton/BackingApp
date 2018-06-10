package com.nenton.backingapp;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.nenton.backingapp.utils.MyFactory;
import com.nenton.backingapp.utils.MyService;

import static com.nenton.backingapp.utils.MyFactory.KEY_ID_RECIPE;
import static com.nenton.backingapp.utils.MyFactory.KEY_MODE;
import static com.nenton.backingapp.utils.MyFactory.MODE_INGREDIENTS;
import static com.nenton.backingapp.utils.MyFactory.MODE_RECIPES;

public class Widget extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        Log.e("Widget", "updateAppWidget");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        setUpdateRv(views, context, appWidgetId);
        setList(views, context, appWidgetId);
        setListClick(views, context, appWidgetId);
        setBtnClick(views, context, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
    }

    private static void setBtnClick(RemoteViews views, Context context, int appWidgetId) {
        Log.e("Widget", "setBtnClick");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int mode = sharedPreferences.getInt(KEY_MODE, MODE_RECIPES);
        switch (mode) {
            case MODE_RECIPES:
                views.setViewVisibility(R.id.widget_back_btn, View.GONE);
                break;
            case MODE_INGREDIENTS:
                views.setViewVisibility(R.id.widget_back_btn, View.VISIBLE);
                Intent intent = new Intent(context, Widget.class);
                intent.setAction("com.nenton.backingapp.updateviewrecipes");
                intent.putExtra(KEY_MODE, MODE_RECIPES);
                intent.putExtra(KEY_ID_RECIPE, 0);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                        intent, 0);
                views.setOnClickPendingIntent(R.id.widget_back_btn, pendingIntent);
                break;
            default:
                views.setViewVisibility(R.id.widget_back_btn, View.GONE);
                break;
        }
    }

    private static void setListClick(RemoteViews views, Context context, int appWidgetId) {
        Log.e("Widget", "setListClick");
        Intent intent = new Intent(context, Widget.class);
        intent.setAction("com.nenton.backingapp.updateview");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);
        views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);
    }

    private static void setList(RemoteViews views, Context context, int appWidgetId) {
        Log.e("Widget", "setList");
        Intent intent = new Intent(context, MyService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.widget_list, intent);
    }

    private static void setUpdateRv(RemoteViews views, Context context, int appWidgetId) {
        Log.e("Widget", "setUpdateRv");
        Intent intent = new Intent(context, Widget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_tv, pendingIntent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.e("Widget", "onUpdate");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        if (context != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences
                    .edit()
                    .putInt(KEY_MODE, MODE_RECIPES)
                    .putInt(KEY_ID_RECIPE, 0)
                    .apply();
        }
        // When the user deletes the widget, delete the preference associated with it.
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e("Widget", "onReceive");

        if (intent != null && intent.getAction() != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                int mode = extras.getInt(KEY_MODE);
                int id = extras.getInt(KEY_ID_RECIPE);
                if (mode != 0) {
                    sharedPreferences
                            .edit()
                            .putInt(KEY_MODE, mode)
                            .putInt(KEY_ID_RECIPE, id)
                            .apply();
                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, Widget.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
//                Widget.updateAppWidgets(context, appWidgetManager, appWidgetIds);
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }
    }

//    public static void updateAppWidgets(Context applicationContext, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        for (int id : appWidgetIds) {
//            updateAppWidget(applicationContext, appWidgetManager, id);
//        }
//    }
}

