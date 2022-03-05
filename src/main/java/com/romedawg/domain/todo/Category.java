package com.romedawg.domain.todo;

public enum Category {
    HOME("Home"),
    WORK("Work"),
    KIDS("Kids"),
    FAMILY("Family");

    private final String displayValue;

    Category(String displayValue) {
        this.displayValue = displayValue;
    }
    public String getDisplayValue(){
        return displayValue;
    }
}
