package com.tetris;

import java.util.Arrays;
import java.util.Random;


public class Tetris {
    private boolean DEBUG = true;
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

    protected Piece currentPiece;
    protected int currentX;
    protected int currentY;

    protected Random random;
    protected int lineCount;
    protected int score;

    public Tetris() {
        Piece.generatePieces();
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        pieces = Piece.pieces;
        random = new Random();
        lineCount = 0;
        score = 0;
    }

    public void debug() {
        if (DEBUG) {
            System.out.println("Name: " + currentPiece.getName());
            System.out.println("Coordinates: " + "(" + currentX + ", " + currentY + ")");
            System.out.println("W: " + currentPiece.getWidth() + ", H: " + currentPiece.getHeight());
            System.out.println("Skirt: " + Arrays.toString(currentPiece.getSkirt()));
            System.out.println("Body: " + Arrays.toString(currentPiece.getBody()));
            System.out.println("Heights: " + Arrays.toString(board.getHeights()));
            System.out.println("Widths: " + Arrays.toString(board.getWidths()));
        }
    }

    private Message setCurrentPiece(Piece piece, int x, int y) {
        Message message = board.insert(piece, x, y);

        if (message.ordinal() <= Message.PLACE_ROW_FILLED.ordinal()) {
            currentPiece = piece;
            currentX = x;
            currentY = y;
        } else {
            board.undo();
        }

        return message;
    }

    private void step() {
        int lineCount = board.clearRows();
        this.lineCount += lineCount;
        score += lineCount * 100;
    }

    public Piece getRandomPiece() {
        int index;
        index = (int)(pieces.length * random.nextDouble());
        return pieces[index];
    }

    public void spawn(Piece piece) {
        int pieceWidth = piece.getWidth();
        int pieceHeight = piece.getHeight();
        int boardWidth = board.getWidth();
        int boardHeight = board.getHeight();
        int pX = (boardWidth - pieceWidth) / 2;
        int pY = boardHeight - pieceHeight;

        board.commit();
        setCurrentPiece(piece, pX, pY);
    }

    public void spawn(String s) {
        Piece newPiece = Piece.get(s);
        int pieceWidth = newPiece.getWidth();
        int pieceHeight = newPiece.getHeight();
        int boardWidth = board.getWidth();
        int boardHeight = board.getHeight();
        int pX = (boardWidth - pieceWidth) / 2;
        int pY = boardHeight - pieceHeight;

        board.commit();
        setCurrentPiece(newPiece, pX, pY);
    }

    public void rotate(int dir) {
        Piece rotatedPiece = currentPiece.getRotated(dir);
        int currentWidth = currentPiece.getWidth();
        int currentHeight = currentPiece.getHeight();
        int rotatedWidth = rotatedPiece.getWidth();
        int rotatedHeight = rotatedPiece.getHeight();
        int pX = currentX + (currentWidth - rotatedWidth) / 2;
        int pY = currentY + currentHeight - rotatedHeight;

        board.undo();
        Message message = setCurrentPiece(rotatedPiece, pX, pY);

        if (message.ordinal() >= Message.PLACE_OUT_BOUNDS.ordinal()) {
            System.out.println("Failed");
            board.insert(currentPiece, currentX, currentY);
        }
    }

    public Message move(int x, int y) {
        int dX = currentX + x;
        int dY = currentY + y;

        board.undo();
        Message message = setCurrentPiece(currentPiece, dX, dY);

        if (message.ordinal() >= Message.PLACE_OUT_BOUNDS.ordinal()) {
            System.out.println("Failed");
            board.insert(currentPiece, currentX, currentY);
        }

        return message;
    }

    public void moveHard(int x, int y) {
        Message message = move(x, y);

        while (message == Message.PLACE_OK) {
            message = move(x, y);
        }
    }

    public void generateRandomPiece() {
        Piece nextPiece = getRandomPiece();
        step();
        spawn(nextPiece);
    }

    public void hardDrop() {
        board.undo();
        int pY = board.dropHeight(currentPiece, currentX);
        Message message = setCurrentPiece(currentPiece, currentX, pY);

        if (message == Message.PLACE_OK) {
            generateRandomPiece();
        }
    }

    public void tick() {
        board.undo();
        Message message = move(0, -1);

        if (message.ordinal() >= Message.PLACE_OUT_BOUNDS.ordinal()) {
            generateRandomPiece();
        }
    }
}
