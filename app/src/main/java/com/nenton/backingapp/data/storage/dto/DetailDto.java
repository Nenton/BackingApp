package com.nenton.backingapp.data.storage.dto;

import java.io.Serializable;

public class DetailDto implements Serializable {
    private int id;
    private DetailType type;
    private String text;
    private boolean isVideo;

    public DetailDto(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.text = builder.text;
        this.isVideo = builder.isVideo;
    }

    public int getId() {
        return id;
    }

    public DetailType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public enum DetailType {
        INGREDIENTS,
        STEP
    }

    public static class Builder {
        private int id = -1;
        private DetailType type = DetailType.STEP;
        private String text = "";
        private boolean isVideo = false;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setType(DetailType type) {
            this.type = type;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setIsVideo(boolean isVideo) {
            this.isVideo = isVideo;
            return this;
        }

        public DetailDto build() {
            return new DetailDto(this);
        }
    }
}
