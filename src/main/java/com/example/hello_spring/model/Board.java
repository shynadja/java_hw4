package com.example.hello_spring.model;

public class Board {
    private final char[][] grid;
    private final int size;

    public Board(int size) {
        this.size = size;
        this.grid = new char[size][size];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = ' ';
            }
        }
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkWin() {
        // Проверка строк
        for (int i = 0; i < size; i++) {
            if (checkLine(i, 0, 0, 1)) return true;
        }

        // Проверка столбцов
        for (int j = 0; j < size; j++) {
            if (checkLine(0, j, 1, 0)) return true;
        }

        // Проверка диагоналей
        return checkLine(0, 0, 1, 1) || checkLine(0, size - 1, 1, -1);
    }

    private boolean checkLine(int startRow, int startCol, int rowStep, int colStep) {
        char first = grid[startRow][startCol];
        if (first == ' ') return false;
        
        for (int i = 1; i < size; i++) {
            if (grid[startRow + i * rowStep][startCol + i * colStep] != first) {
                return false;
            }
        }
        return true;
    }

    public boolean placeMark(int row, int col, char mark) {
        if (row >= 0 && row < size && col >= 0 && col < size && grid[row][col] == ' ') {
            grid[row][col] = mark;
            return true;
        }
        return false;
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size && grid[row][col] == ' ';
    }
        
    public int getSize() {
        return size;
    }

    public char[][] getGrid() {
        return grid.clone();
    }

    public String getGridAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(grid[i][j]);
                if (j < size - 1) sb.append('|');
            }
            if (i < size - 1) sb.append(';');
        }
        return sb.toString();
    }

    public void setGridFromString(String gridString) {
        String[] rows = gridString.split(";");
        for (int i = 0; i < size && i < rows.length; i++) {
            String[] cells = rows[i].split("\\|");
            for (int j = 0; j < size && j < cells.length; j++) {
                grid[i][j] = cells[j].charAt(0);
            }
        }
    }
}
