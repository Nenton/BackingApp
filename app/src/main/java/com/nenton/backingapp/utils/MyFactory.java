package com.nenton.backingapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nenton.backingapp.R;
import com.nenton.backingapp.data.storage.dto.WidgetDto;
import com.nenton.backingapp.data.storage.realm.IngredientRealm;
import com.nenton.backingapp.data.storage.realm.RecipeRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyFactory implements RemoteViewsService.RemoteViewsFactory {
    public static final int MODE_RECIPES = 12434;
    public static final int MODE_INGREDIENTS = 124;
    public static final String KEY_MODE = "KEY_MODE";
    public static final String KEY_ID_RECIPE = "KEY_ID_RECIPE";

    private List<WidgetDto> data;
    private Context context;
    private int mode;

    public MyFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        Realm.init(context);
        data = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mode = sharedPreferences.getInt(KEY_MODE, MODE_RECIPES);
        int id = sharedPreferences.getInt(KEY_ID_RECIPE, -1);
        data.clear();
        switch (mode) {
            case MODE_RECIPES:
                Log.e("MyFactory", "onDataSetChanged " + "MODE_RECIPES");
                RealmResults<RecipeRealm> all = Realm.getDefaultInstance().where(RecipeRealm.class).findAll();
                for (RecipeRealm recipeRealm : all) {
                    data.add(new WidgetDto(recipeRealm.getId(), recipeRealm.getName()));
                }
                break;
            case MODE_INGREDIENTS:
                Log.e("MyFactory", "onDataSetChanged " + "MODE_INGREDIENTS");
                RecipeRealm recipe = Realm.getDefaultInstance().where(RecipeRealm.class).equalTo("id", id).findFirst();
                for (IngredientRealm ingredient : recipe.getIngredients()) {
                    String ingredientString = ingredient.getIngredient() + " " +
                            ingredient.getQuantity() + " " + ingredient.getMeasure();
                    data.add(new WidgetDto(ingredient.getId(), ingredientString));
                }
                break;
            default:
                Log.e("MyFactory", "onDataSetChanged " + "DEFAULT");
                RealmResults<RecipeRealm> alls = Realm.getDefaultInstance().where(RecipeRealm.class).findAll();
                for (RecipeRealm recipeRealm : alls) {
                    data.add(new WidgetDto(recipeRealm.getId(), recipeRealm.getName()));
                }
                break;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        view.setTextViewText(R.id.widget_tv, data.get(i).getName());
        Log.e("getViewAt", "VIEW " + data.get(i).getName());
        Log.e("getViewAt", "MODE " + mode);
        if (mode == MODE_RECIPES || mode == 0) {
            Intent clickIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_MODE, MODE_INGREDIENTS);
            bundle.putInt(KEY_ID_RECIPE, data.get(i).getId());
            clickIntent.putExtras(bundle);
            view.setOnClickFillInIntent(R.id.widget_item_wrap, clickIntent);
        } else {
            Intent clickIntent = new Intent();
            view.setOnClickFillInIntent(R.id.widget_item_wrap, clickIntent);
        }
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
