package com.nenton.backingapp.data.storage.realm;

import com.nenton.backingapp.data.network.res.RecipeResponse;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RecipeRealm extends RealmObject implements Serializable {
    @PrimaryKey
    private int id;
    private String name;
    private RealmList<IngredientRealm> ingredients = null;
    private RealmList<StepRealm> steps = null;
    private int servings;
    private String image;

    public RecipeRealm() {
    }

    public RecipeRealm(RecipeResponse recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        ingredients = new RealmList<>();
        for (RecipeResponse.Ingredient ingredient : recipe.getIngredients()) {
            ingredients.add(new IngredientRealm(ingredient));
        }

        steps = new RealmList<>();
        for (RecipeResponse.Step step : recipe.getSteps()) {
            steps.add(new StepRealm(step));
        }
        this.servings = recipe.getServings();
        this.image = recipe.getImage();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RealmList<IngredientRealm> getIngredients() {
        return ingredients;
    }

    public RealmList<StepRealm> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
