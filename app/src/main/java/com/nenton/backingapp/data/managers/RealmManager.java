package com.nenton.backingapp.data.managers;

import com.nenton.backingapp.data.network.res.RecipeResponse;
import com.nenton.backingapp.data.storage.realm.IngredientRealm;
import com.nenton.backingapp.data.storage.realm.RecipeRealm;
import com.nenton.backingapp.data.storage.realm.StepRealm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

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

    public List<RecipeRealm> getRecipes() {
        return getRealmInstance()
                .where(RecipeRealm.class)
                .findAllAsync();
    }

    public List<IngredientRealm> getIngredients() {
        return getRealmInstance()
                .where(IngredientRealm.class)
                .findAllAsync();
    }

    public List<StepRealm> getSteps() {
        return getRealmInstance()
                .where(StepRealm.class)
                .findAllAsync();
    }

    public RecipeRealm getRecipeById(int id) {
        return getRealmInstance()
                .where(RecipeRealm.class)
                .equalTo("id", id)
                .findFirstAsync();
    }

    public IngredientRealm getIngredientById(int id) {
        return getRealmInstance()
                .where(IngredientRealm.class)
                .equalTo("id", id)
                .findFirstAsync();
    }

    public StepRealm getStepById(int id) {
        return getRealmInstance()
                .where(StepRealm.class)
                .equalTo("id", id)
                .findFirstAsync();
    }
}
