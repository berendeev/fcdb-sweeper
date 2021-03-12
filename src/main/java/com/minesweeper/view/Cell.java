package com.minesweeper.view;

import com.minesweeper.Paths;
import com.minesweeper.model.CellType;
import com.minesweeper.model.CellValue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Cell extends JLabel {

    private CellType type;
    private CellValue value;


    public Cell(CellValue value) {
        super();

        this.type = CellType.CLOSED;
        this.value = value;
    }

    public Cell() {
        super();

        this.type = CellType.CLOSED;
        value = CellValue.EMPTY;
    }

    public void open() {
        type = CellType.OPENED;
        setIcon();
    }

    public void showMistake() {
        if (type == CellType.FLAGGED && value != CellValue.BOMB) {
            type = CellType.NO_BOMB;
            setIcon();
        }
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public CellValue getValue() {
        return value;
    }

    public void setValue(CellValue value) {
        this.value = value;
    }

    public boolean isOpen() {
        return type == CellType.OPENED;
    }

    public boolean isFlagged() {
        return type == CellType.FLAGGED;
    }

    public void setFlagged(boolean flagged) {
        if (!isOpen()) {
            type = flagged ? CellType.FLAGGED : CellType.CLOSED;
            setIcon();
        }
    }

    public void setIcon() {
        String cellIconPath = Paths.getCellIconPath(this);
        this.setIcon(new ImageIcon(new ImageIcon(cellIconPath).getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
    }
}
