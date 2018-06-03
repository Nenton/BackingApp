package com.nenton.backingapp.data.storage.realm;

import com.nenton.backingapp.data.network.res.RecipeResponse.Ingredient;
import com.nenton.backingapp.data.storage.dto.IngredientDto;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class IngredientRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private double quantity;
    private String measure;
    private String ingredient;

    public IngredientRealm() {
    }

    public IngredientRealm(Ingredient ingredient) {
        this.quantity = ingredient.getQuantity();
        this.measure = ingredient.getMeasure();
        this.ingredient = ingredient.getIngredient();
    }

    public IngredientRealm(IngredientDto ingredient) {
        this.quantity = ingredient.getQuantity();
        this.measure = ingredient.getMeasure();
        this.ingredient = ingredient.getIngredient();
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
