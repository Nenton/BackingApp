package com.nenton.backingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.nenton.backingapp.R;
import com.nenton.backingapp.data.storage.dto.WidgetDto;
import com.nenton.backingapp.data.storage.realm.IngredientRealm;
import com.nenton.backingapp.data.storage.realm.RecipeRealm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

public class IngredientsConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.nenton.backingapp.widget.Ingredients";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private RecyclerView mRecyclerView;

    public IngredientsConfigureActivity() {
        super();
    }

    static void saveTitlePref(Context context, int appWidgetId, Set<String> text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putStringSet(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    static Set<String> loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Set<String> titleValue = prefs.getStringSet(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return Collections.emptySet();
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Realm.init(this);
        setResult(RESULT_CANCELED);
        RealmResults<RecipeRealm> all = Realm.getDefaultInstance().where(RecipeRealm.class).findAll();
        List<WidgetDto> list = new ArrayList<>();
        for (RecipeRealm recipeRealm : all) {
            list.add(new WidgetDto(recipeRealm.getId(), recipeRealm.getName()));
        }
        setContentView(R.layout.ingredients_configure);
        mRecyclerView = findViewById(R.id.widget_config_rv);
        WidgetConfigAdapter widgetConfigAdapter = new WidgetConfigAdapter(list, new Widgetable() {
            @Override
            public void clickEvent(int id) {
                final Context context = IngredientsConfigureActivity.this;
                RecipeRealm recipe = Realm.getDefaultInstance()
                        .where(RecipeRealm.class)
                        .equalTo("id", id)
                        .findFirst();
                Set<String> strings = new HashSet<>();
                for (IngredientRealm ingredient : recipe.getIngredients()) {
                    strings.add(ingredient.getIngredient() + " " +
                            ingredient.getQuantity() + " " +
                            ingredient.getMeasure());
                }
                saveTitlePref(context, mAppWidgetId, strings);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                Widget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
        mRecyclerView.setAdapter(widgetConfigAdapter);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    public interface Widgetable {
        void clickEvent(int id);
    }
}

