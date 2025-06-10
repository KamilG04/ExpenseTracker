package org.budget_manager.model;

public enum Category {
    FOOD("Żywność"),
    TRANSPORT("Transport"),
    UTILITIES("Media"),
    ENTERTAINMENT("Rozrywka"),
    OTHER("Inne");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String toString() {
        return displayName;
    }
}