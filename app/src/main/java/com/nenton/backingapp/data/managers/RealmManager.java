package com.nenton.backingapp.data.managers;

import com.nenton.backingapp.data.network.res.RecipeResponse;
import com.nenton.backingapp.data.storage.realm.IngredientRealm;
import com.nenton.backingapp.data.storage.realm.RecipeRealm;
import com.nenton.backingapp.data.storage.realm.StepRealm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmManager {
    private Realm mRealmInstance;

    public Realm getRealmInstance() {
        if (mRealmInstance == null || mRealmInstance.isClosed()) {
            mRealmInstance = Realm.getDefaultInstance();
        }
        return mRealmInstance;
    }

    public void deleteFromRealm(Class<? extends RealmObject> entityRealmClass, String id) {
        Realm realm = getRealmInstance();
        RealmObject entity = realm.where(entityRealmClass).equalTo("id", id).findFirst();
        if (entity != null) {
            realm.executeTransaction(realm1 -> entity.deleteFromRealm());
        }
        realm.close();
    }

    public void saveRecipesFromNet(List<RecipeResponse> recipes) {
        Realm realm = getRealmInstance();
        for (RecipeResponse recipe : recipes) {
            realm.executeTransaction(realm1 -> realm1.insertOrUpdate(new RecipeRealm(recipe)));
        }
        realm.close();
    }

    public RealmResults<RecipeRealm> getRecipes
            (RealmChangeListener<RealmResults<RecipeRealm>> callback) {
        RealmResults<RecipeRealm> results = getRealmInstance()
                .where(RecipeRealm.class)
                .findAllAsync();
        results.addChangeListener(callback);
        return results;
    }

    public RealmResults<RecipeRealm> getRecipes() {
        return getRealmInstance()
                .where(RecipeRealm.class)
                .findAllAsync();
    }

    public RealmResults<StepRealm> getSteps() {
        return getRealmInstance()
                .where(StepRealm.class)
                .findAllAsync();
    }

    public RecipeRealm getRecipeById(int id, RealmChangeListener<RecipeRealm> realmChangeListener) {
        RecipeRealm recipeRealm = getRealmInstance()
                .where(RecipeRealm.class)
                .equalTo("id", id)
                .findFirstAsync();
        recipeRealm.addChangeListener(realmChangeListener);
        return recipeRealm;
    }

    public IngredientRealm getIngredientById(int id) {
        return getRealmInstance()
                .where(IngredientRealm.class)
                .equalTo("id", id)
                .findFirstAsync();
    }

    public StepRealm getStepById(int id, RealmChangeListener<StepRealm> realmChangeListener) {
        StepRealm stepRealm = getRealmInstance()
                .where(StepRealm.class)
                .equalTo("id", id)
                .findFirstAsync();
        stepRealm.addChangeListener(realmChangeListener);
        return stepRealm;
    }

//    public int getLastIdRecipe() {
//        Number id = getRealmInstance()
//                .where(RecipeRealm.class)
//                .max("id");
//        return id == null ? 0 : id.intValue();
//    }
//
//    public int getLastIdStep() {
//        Number id = getRealmInstance()
//                .where(StepRealm.class)
//                .max("id");
//        return id == null ? 0 : id.intValue();
//    }
//
//    public int getLastIdIngredient() {
//        Number id = getRealmInstance()
//                .where(IngredientRealm.class)
//                .max("id");
//        return id == null ? 0 : id.intValue();
//    }
}
