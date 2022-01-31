package com.tetris;

public class Board {
    public static String PLACE_OK = "PLACE_OK";
    public static String PLACE_OUT_BOUNDS = "PLACE_OUt_BOUNDS";
    public static String PLACE_BAD = "PLACE_BAD";
    public static String PLACE_ROW_FILLED = "PLACE_ROW_FILLED";
    public int width = 0;
    public int height = 0;
    protected Grid grid;

    public String toString() {
        return grid.toString();
    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Grid(width, height);
    }
}
