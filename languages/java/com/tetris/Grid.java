package com.tetris;

import java.util.Arrays;

public class Grid {
    public int width;
    public int height;
    public String[][] matrix;
    public String cell = ".";

    public String toString() {
        String ret = "";

        for (int i = 0; i < height; i ++) {
            for (int j = 0; j < width; j++) {
                ret += cell;

                if (j < width - 1) {
                    ret += " ";
                }
            }

            if (i < height - 1) {
                ret += "\n";
            }
        }

        return ret;
    }

    public String[][] generateMatrix(int width, int height, String cell) {
        String[][] matrix = new String[width][height];

        for (int i = 0; i < matrix.length; i++) {
            String[] row = matrix[i];
            Arrays.fill(row, cell);
        }

        return matrix;
    }

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        matrix = generateMatrix(width, height, cell);
    }
}
