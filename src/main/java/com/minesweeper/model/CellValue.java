package com.minesweeper.model;

import java.util.Arrays;

public enum CellValue {
    EMPTY("empty.png", 0),
    ONE("1.png", 1),
    TWO("2.png", 2),
    THREE("3.png", 3),
    FOUR("4.png", 4),
    FIVE("5.png", 5),
    SIX("6.png", 6),
    SEVEN("7.png", 7),
    EIGHT("8.png", 8),
    BOMB("bomb.png", -1);

    private final String iconName;
    private int intPresentation;

    CellValue(String iconName, int intPresentation) {
        this.iconName = iconName;
        this.intPresentation = intPresentation;
    }

    public static CellValue getNext(CellValue value) {
        if (value.intPresentation < 8) {
            return foundByIntPresentation(value.intPresentation + 1);
        } else {
            throw new RuntimeException("Something was wrong");
        }
    }

    public String getIconName() {
        return iconName;
    }

    private static CellValue foundByIntPresentation(int value){
        return Arrays.stream(CellValue.values())
                .filter(cellValue -> cellValue.intPresentation == value)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Can not found cell type by value:" + value));
    }
}
