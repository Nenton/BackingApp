package com.nenton.backingapp.data.storage.realm;

import com.nenton.backingapp.data.network.res.RecipeResponse.Step;
import com.nenton.backingapp.data.storage.dto.StepDto;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class StepRealm extends RealmObject {
    @PrimaryKey
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public StepRealm() {
    }

    public StepRealm(Step step) {
        this.id = step.getId();
        this.shortDescription = step.getShortDescription();
        this.description = step.getDescription();
        this.videoURL = step.getVideoURL();
        this.thumbnailURL = step.getThumbnailURL();
    }

    public StepRealm(StepDto step) {
        this.id = step.getId();
        this.shortDescription = step.getShortDescription();
        this.description = step.getDescription();
        this.videoURL = step.getVideoURL();
        this.thumbnailURL = step.getThumbnailURL();
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
