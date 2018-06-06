package com.nenton.backingapp.data.storage.realm;

import com.nenton.backingapp.data.network.res.RecipeResponse.Ingredient;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class IngredientRealm extends RealmObject implements Serializable{
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
        this.id = ((int) quantity) + (measure + ingredient).hashCode();
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

    public int getId() {
        return id;
    }
}
