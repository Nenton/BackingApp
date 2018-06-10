package com.nenton.backingapp.data.storage.dto;

import com.nenton.backingapp.data.network.res.RecipeResponse;
import com.nenton.backingapp.data.storage.realm.IngredientRealm;
import com.nenton.backingapp.data.storage.realm.RecipeRealm;
import com.nenton.backingapp.data.storage.realm.StepRealm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeDto implements Serializable{
    private int id;
    private String name;
    private List<IngredientDto> ingredients = null;
    private List<StepDto> steps = null;
    private int servings;
    private String image;

    public RecipeDto(RecipeResponse ingredientsNet) {
        this.id = ingredientsNet.getId();
        this.name = ingredientsNet.getName();
        List<IngredientDto> ingredients = new ArrayList<>();
        for (RecipeResponse.Ingredient ingredient : ingredientsNet.getIngredients()) {
            ingredients.add(new IngredientDto(ingredient));
        }
        this.ingredients = ingredients;
        List<StepDto> steps = new ArrayList<>();
        for (RecipeResponse.Step step : ingredientsNet.getSteps()) {
            steps.add(new StepDto(step));
        }
        this.steps = steps;
        this.servings = ingredientsNet.getServings();
        this.image = ingredientsNet.getImage();
    }

    public RecipeDto(RecipeRealm recipeRealm) {
        this.id = recipeRealm.getId();
        this.name = recipeRealm.getName();
        List<IngredientDto> ingredients = new ArrayList<>();
        for (IngredientRealm ingredient : recipeRealm.getIngredients()) {
            ingredients.add(new IngredientDto(ingredient));
        }
        this.ingredients = ingredients;
        List<StepDto> steps = new ArrayList<>();
        for (StepRealm step : recipeRealm.getSteps()) {
            steps.add(new StepDto(step));
        }
        this.steps = steps;
        this.servings = recipeRealm.getServings();
        this.image = recipeRealm.getImage();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }

    public List<StepDto> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
