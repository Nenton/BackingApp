package com.nenton.backingapp.data.pojo;

import com.nenton.backingapp.data.network.res.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private List<Ingredient> ingredients = null;
    private List<Step> steps = null;
    private int servings;
    private String image;

    public Recipe(RecipeResponse ingredientsNet) {
        this.id = ingredientsNet.getId();
        this.name = ingredientsNet.getName();
        List<Ingredient> ingredients = new ArrayList<>();
        for (RecipeResponse.Ingredient ingredient : ingredientsNet.getIngredients()) {
            ingredients.add(new Ingredient(ingredient));
        }
        this.ingredients = ingredients;
        List<Step> steps = new ArrayList<>();
        for (RecipeResponse.Step step : ingredientsNet.getSteps()) {
            steps.add(new Step(step));
        }
        this.steps = steps;
        this.servings = ingredientsNet.getServings();
        this.image = ingredientsNet.getImage();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public class Ingredient {
        private double quantity;
        private String measure;
        private String ingredient;

        public Ingredient(int quantity, String measure, String ingredient) {
            this.quantity = quantity;
            this.measure = measure;
            this.ingredient = ingredient;
        }

        public Ingredient(RecipeResponse.Ingredient ingredientNet) {
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

    public class Step {
        private int id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        public Step(RecipeResponse.Step step) {
            this.id = step.getId();
            this.shortDescription = step.getShortDescription();
            this.description = step.getDescription();
            this.videoURL = step.getVideoURL();
            this.thumbnailURL = step.getThumbnailURL();
        }

        public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoURL = videoURL;
            this.thumbnailURL = thumbnailURL;
        }

        public int getId() {
            return id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }
    }
}
