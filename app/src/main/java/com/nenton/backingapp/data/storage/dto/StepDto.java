package com.nenton.backingapp.data.storage.dto;

import com.nenton.backingapp.data.network.res.RecipeResponse;

public class StepDto {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public StepDto(RecipeResponse.Step step) {
        this.id = step.getId();
        this.shortDescription = step.getShortDescription();
        this.description = step.getDescription();
        this.videoURL = step.getVideoURL();
        this.thumbnailURL = step.getThumbnailURL();
    }

    public StepDto(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
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
