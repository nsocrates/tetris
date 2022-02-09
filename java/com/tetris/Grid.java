package com.tetris;

import java.util.Arrays;

public class Grid {
    private int width;
    private int height;
    private String[][] matrix;
    public String emptyCell = ".";

    private static String[][] generateMatrix(int width, int height, String value) {
        String[][] matrix = new String[width][height];

        for (int i = 0; i < matrix.length; i++) {
            String[] row = matrix[i];
            Arrays.fill(row, value);
        }

        return matrix;
    }

    public String toString() {
        String ret = "";

        for (int y = height - 1; y>= 0; y--) {
            for (int x = 0; x < width; x++) {
                ret += matrix[x][y];

                if (x < width - 1) {
                    ret += " ";
                }
            }

            if (y != 0) {
                ret += "\n";
            }
        }

        return ret;
    }

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        matrix = generateMatrix(width, height, emptyCell);
    }

    public void set(int x, int y, String value) {
        matrix[x][y] = value;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(String[][] matrix) {
        this.matrix = matrix;
    }

    public String[] getRow(int y) {
        String[] ret = new String[width];

        for (int x = 0; x < width; x++) {
            ret[x] = matrix[x][y];
        }

        return ret;
    }

    public void setRow(int y, String[] row) {
        if (row.length != width) {
            throw new RuntimeException("Cannout set row. Out of bounds");
        }

        for (int x = 0; x < width; x++) {
            set(x, y, row[x]);
        }
    }

    public void fillRow(int y, String value) {
        for (int x = 0; x < width; x++) {
            set(x, y, value);
        }
    }

    public String[] getCol(int y) {
        return matrix[y];
    }

    public String getCell(int x, int y) {
        return matrix[x][y];
    }

    public void setCell(int x, int y, String value) {
        matrix[x][y] = value;
    }

    public Grid clone() {
        Grid newGrid = new Grid(width, height);

        String[][] copiedMatrix = new String[width][height];
        for (int i = 0; i < matrix.length; i++) {
            copiedMatrix[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }

        newGrid.setMatrix(copiedMatrix);

        return newGrid;
    }
}
