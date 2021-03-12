package com.minesweeper;

import com.minesweeper.model.CellType;
import com.minesweeper.view.Cell;

public class Paths {
    private static final String path = "/home/dberendeev/Java/mine/src/main/resources/images/";


    public static String getCellIconPath(Cell cell){
        if (cell.getType() == CellType.OPENED){
            return path + cell.getValue().getIconName();
        }
        return path + cell.getType().getIconName();
    }
}
