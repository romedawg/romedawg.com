package com.romedawg.domain.todo;

public enum Category {
    FEATURE("Feature"),
    SECURITY("Security"),
    TASK("Improvement"),
    SUPPORT("Nice to have");

    private final String displayValue;

    Category(String displayValue) {
        this.displayValue = displayValue;
    }
    public String getDisplayValue(){
        return displayValue;
    }
}
