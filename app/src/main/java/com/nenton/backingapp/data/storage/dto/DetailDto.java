package com.nenton.backingapp.data.storage.dto;

public class DetailDto {
    private int id;
    private DetailType type;
    private String text;

    public DetailDto(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.text = builder.text;
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

    public enum DetailType {
        INGREDIENTS,
        STEP
    }

    public static class Builder {
        private int id = -1;
        private DetailType type = DetailType.STEP;
        private String text = "";

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

        public DetailDto build() {
            return new DetailDto(this);
        }
    }
}
