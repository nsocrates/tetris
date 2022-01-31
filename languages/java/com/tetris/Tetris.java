package com.tetris;

public class Tetris {
    public static int BOARD_WIDTH = 10;
    public static int BOARD_HEIGHT = 20;
    public static String ROTATE_CCW = "ROTATE_CCW";
    public static String ROTATE_CW = "ROTATE_CW";
    public static String MOVE_RIGHT = "MOVE_RIGHT";
    public static String MOVE_LEFT = "MOVE_LEFT";
    public static String MOVE_DOWN = "MOVE_DOWN";
    public static String HARD_DROP = "HARD_DROP";
    public static String SPAWN = "SPAWN";
    public static String TICK = "TICK";

    protected Board board;
    protected Piece[] pieces;

    protected Piece activePiece;

    public Tetris() {
        Piece.generatePieces();
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        pieces = Piece.pieces;
    }

    public void spawn(String s) {
        Piece piece = Piece.get(s);
        activePiece = piece;
    }

    public void rotate(int dir) {
        if (dir == 1) {
            activePiece = activePiece.next;
        } else {
            activePiece = activePiece.prev;
        }
    }
}
