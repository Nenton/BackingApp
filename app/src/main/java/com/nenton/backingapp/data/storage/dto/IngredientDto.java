package com.nenton.backingapp.data.storage.dto;

import com.nenton.backingapp.data.network.res.RecipeResponse;

public class IngredientDto {
    private double quantity;
    private String measure;
    private String ingredient;

    public IngredientDto(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public IngredientDto(RecipeResponse.Ingredient ingredientNet) {
        this.quantity = ingredientNet.getQuantity();
        this.measure = ingredientNet.getMeasure();
        this.ingredient = ingredientNet.getIngredient();
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
