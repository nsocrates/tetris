package com.tetris;

import java.util.Arrays;


public class Board {
    private Grid grid;
    private int width;
    private int height;
    private int[] widths;
    private int[] heights;
    private Grid xGrid;
    private int[] xWidths;
    private int[] xHeights;
    private boolean isCommitted = true;
    private int largestRow = 0;
    private int xLargestRow = 0;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Grid(width, height);
        widths = new int[height];
        heights = new int[width];
        xGrid = new Grid(width, height);
        xWidths = new int[height];
        xHeights = new int[width];
    }

    private void backup() {
        xGrid = grid.clone();
        xWidths = Arrays.copyOf(widths, widths.length);
        xHeights = Arrays.copyOf(heights, heights.length);
        xLargestRow = largestRow;
    }

    public String toString() {
        return grid.toString();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getHeights() {
        return heights;
    }

    public int[] getWidths() {
        return widths;
    }

    public void commit() {
        this.isCommitted = true;
    }

    public void commit(boolean isCommitted) {
        this.isCommitted = isCommitted;
    }

    public void undo() {
        grid = xGrid;
        widths = xWidths;
        heights = xHeights;
        largestRow = xLargestRow;
        commit();
    }

    public int dropHeight(Piece piece, int x) {
        int maxY = 0;
        int[] skirt = piece.getSkirt();
        int width = piece.getWidth();

        for (int i = 0; i < width; i++) {
            int lowestY = skirt[i];
            int comparableY = heights[x + i] - lowestY;
            maxY = Math.max(maxY, comparableY);
        }

        return maxY;
    }

    public Message insert(Piece piece, int x, int y) {
        if (!isCommitted) {
            throw new RuntimeException("Previous round has not been committed");
        }

        commit(false);
        backup();
        String color = piece.getColor().toUpperCase();
        Point[] body = piece.getBody();

        for (int i = 0; i < body.length; i++) {
            int pX = x + body[i].getX();
            int pY = y + body[i].getY();

            if (0 > pX || pX >= width || 0 > pY || pY >= height) {
                return Message.PLACE_OUT_BOUNDS;
            }

            if (grid.getCell(pX, pY) != grid.emptyCell) {
                return Message.PLACE_BAD;
            }

            widths[pY] += 1;
            heights[pX] = Math.max(heights[pX], pY + 1);
            largestRow = Math.max(largestRow, pY + 1);

            grid.set(pX, pY, color);
        }

        return Message.PLACE_OK;
    }

    private void shiftRow(int row) {
        for (int y = row + 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String cell = grid.getCell(x, y);
                grid.setCell(x, y - 1, cell);
            }

            widths[y - 1] = widths[y];
        }
    }

    private void clearRow(int row) {
        for (int x = 0; x < width; x++) {
            heights[x] -= 1;
            grid.setCell(x, row, grid.emptyCell);
        }
    }

    public int clearRows() {
        int lineCount = 0;
        int y = 0;

        while (y < largestRow) {
            if (widths[y] != width) {
                y += 1;
                continue;
            }

            clearRow(y);
            shiftRow(y);

            lineCount += 1;
            largestRow -= 1;
        }

        return lineCount;
    }
}
