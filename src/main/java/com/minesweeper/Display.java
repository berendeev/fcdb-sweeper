package com.minesweeper;

import com.minesweeper.model.CellValue;
import com.minesweeper.view.Cell;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


import static com.minesweeper.model.CellValue.*;

public class Display {
    private JFrame frame;
    private List<Cell> closedCells;
    private int width;
    private int height;
    private int bombCount;
    private int currentBombCount;
    private boolean isGameOver;
    private Cell[][] cells;
    private JLabel bombCountViewer;
    public Display() {
        this(15, 15, 10);
    }
    public Display(int width, int height, int bombsCount) {
        this.width = width;
        this.height = height;
        this.bombCount = bombsCount;
        currentBombCount = bombCount;
        closedCells = new ArrayList<>();
        bombCountViewer = new JLabel("Bomb count: " + currentBombCount);
        createMainWindow();
    }
    private void createSettings() {
        JFrame settingsFrame = new JFrame();
        settingsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        settingsFrame.setTitle("Settings");
        settingsFrame.setSize(new Dimension(300, 150));
        JLabel settingsLabel = new JLabel("Settings");
        JLabel widthLabel = new JLabel("width: ");
        JLabel heightLabel = new JLabel("height: ");
        JLabel bombsCountLabel = new JLabel("bombs count: ");
        JTextField widthTF = new JTextField();
        JTextField heightTF = new JTextField();
        JTextField mineCountTF = new JTextField();
        settingsFrame.add(settingsLabel, BorderLayout.NORTH);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        settingsFrame.add(panel);
        //panel.add(settingsLabel);
        panel.add(widthLabel);
        panel.add(widthTF);
        panel.add(heightLabel);
        panel.add(heightTF);
        panel.add(bombsCountLabel);
        panel.add(mineCountTF);

        JButton buttonStart = new JButton("New Game");
        settingsFrame.add(buttonStart, BorderLayout.SOUTH);
        buttonStart.addActionListener(e -> {
            try {
                int height = Integer.parseInt(heightTF.getText());
                int width = Integer.parseInt(widthTF.getText());
                int mineCount = Integer.parseInt(mineCountTF.getText());
                settingsFrame.dispose();
                frame.dispose();
                new Display(width, height, mineCount);
            } catch (NumberFormatException ex) {
                //do nothing
            }
        });
        settingsFrame.setVisible(true);
        frame.pack();
    }
    private void createMainWindow() {
        frame = new JFrame();
        Panel menuPanel = new Panel();
        frame.add(menuPanel, BorderLayout.NORTH);
        JMenuBar menuBar = new JMenuBar();
        JMenuItem newGameMenu = new JMenuItem("New game");
        JMenuItem settingsMenu = new JMenuItem("Settings");
        menuBar.add(newGameMenu);
        menuBar.add(settingsMenu);
        menuBar.add(bombCountViewer);
        menuPanel.add(menuBar);
        newGameMenu.addActionListener(e -> {
            frame.dispose();
            new Display(width, height, bombCount);
        });
        settingsMenu.addActionListener(e -> createSettings());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        game();
        frame.setResizable(false);
        frame.setVisible(true);
    }
    private void game() {
        createAndFill();
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(height, width));
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.setIcon();

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (isGameOver) {
                            return;
                        }
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            Cell cell = (Cell) e.getSource();
                            if (!cell.isOpen()) {
                                if (cell.getValue() == BOMB) {
                                    isGameOver = true;
                                    cell.open();
                                    for (Cell closedCell : closedCells) {
                                        closedCell.showMistake();
                                    }
                                }
                                //cell.open(true);
                                if (cell.getValue() == EMPTY) {
                                    // open empty slots: fill
                                    Point cellPos = findPosition(cell);
                                    openEmptyCells(cellPos);
                                } else {
                                    cell.open();
                                    closedCells.remove(cell);
                                }
                            }
                        }
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            Cell cell = (Cell) e.getSource();
                            cell.setFlagged(!cell.isFlagged());
                            if (cell.isFlagged()) {
                                currentBombCount--;
                            } else {
                                currentBombCount++;
                            }
                            bombCountViewer.setText("Bomb count: " + currentBombCount);
                        }

                        if (closedCells.size() == bombCount) {
                            isGameOver = true;
                            bombCountViewer.setText("Win");
                        }
                    }
                });
                panel.add(cell);
            }
        }
        frame.add(panel);
        frame.pack();
    }

    private void openEmptyCells(Point pos) {
        if ((pos.x < 0 || pos.x >= cells.length) || (pos.y < 0 || pos.y >= cells[pos.x].length)) {
            return;
        }

        Cell cell = cells[pos.x][pos.y];
        if (cell.isOpen()) {
            return;
        }

        cell.open();
        closedCells.remove(cell);
        if (cell.getValue() != EMPTY) {
            return;
        }

        openEmptyCells(new Point(pos.x - 1, pos.y));
        openEmptyCells(new Point(pos.x + 1, pos.y));
        openEmptyCells(new Point(pos.x, pos.y + 1));
        openEmptyCells(new Point(pos.x, pos.y - 1));

        openEmptyCells(new Point(pos.x - 1, pos.y - 1));
        openEmptyCells(new Point(pos.x - 1, pos.y + 1));
        openEmptyCells(new Point(pos.x + 1, pos.y - 1));
        openEmptyCells(new Point(pos.x + 1, pos.y + 1));
    }

    private Point findPosition(Cell cell) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].hashCode() == cell.hashCode()) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    private void createAndFill() {
        cells = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell();
                closedCells.add(cells[i][j]);
            }
        }

        for (int i = 0; i < bombCount; i++) {
            int x;
            int y;
            do {
                x = (int) (Math.random() * cells.length);
                y = (int) (Math.random() * cells[0].length);
            } while (cells[x][y].getValue() == BOMB);
            cells[x][y].setValue(BOMB);
        }
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].getValue() == BOMB) {
                    if (i - 1 >= 0) {                        //array index out of bound check
                        incrementNearMinesCount(cells[i - 1][j]);
                    }
                    if (i + 1 < cells.length) {
                        incrementNearMinesCount(cells[i + 1][j]);
                    }
                    if (j - 1 >= 0) {
                        incrementNearMinesCount(cells[i][j - 1]);
                    }
                    if (j + 1 < cells[i].length) {
                        incrementNearMinesCount(cells[i][j + 1]);
                    }
                    if ((i - 1 >= 0) && (j - 1 >= 0)) {
                        incrementNearMinesCount(cells[i - 1][j - 1]);
                    }
                    if ((i + 1 < cells.length) && (j - 1 >= 0)) {
                        incrementNearMinesCount(cells[i + 1][j - 1]);
                    }
                    if ((i - 1 >= 0) && (j + 1 < cells[i].length)) {
                        incrementNearMinesCount(cells[i - 1][j + 1]);
                    }
                    if ((i + 1 < cells.length) && (j + 1 < cells[i].length)) {
                        incrementNearMinesCount(cells[i + 1][j + 1]);
                    }
                }
            }
        }
    }

    private void incrementNearMinesCount(Cell cell){
        if (cell.getValue() != BOMB){
            cell.setValue(CellValue.getNext(cell.getValue()));
        }
    }
}
