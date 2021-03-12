package com.minesweeper.model;

public enum CellType {
    NO_BOMB("nobomb.png"),
    CLOSED("closed.png"),
    OPENED(null),
    FLAGGED("flagged.png");

    private final String iconName;

    CellType(String iconName) {
        this.iconName = iconName;
    }

    public String getIconName() {
        return iconName;
    }
}
