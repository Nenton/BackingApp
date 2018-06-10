package com.nenton.backingapp.data.storage.dto;

public class WidgetDto {
    private int id;
    private String name;

    public WidgetDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
