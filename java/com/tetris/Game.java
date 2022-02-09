package com.tetris;

import java.io.Console;
import java.util.Arrays;
import java.util.HashMap;

public class Game {
    private Tetris tetris;
    private HashMap<String, Runnable> commands = new HashMap<>();

    public Game() {
        tetris = new Tetris();
        commands.put("p", () -> printBoard());
        commands.put("q", () -> quit());
        commands.put("t", () -> printCurrentPiece());
        commands.put("(", () -> rotateLeft());
        commands.put(")", () -> rotateRight());
        commands.put("v", () -> moveDown());
        commands.put("<", () -> moveLeft());
        commands.put(">", () -> moveRight());
        commands.put("vvvv", () -> hardDrop());
        commands.put("<<<<", () -> hardLeft());
        commands.put(">>>>", () -> hardRight());
        commands.put("de", () -> debug());
    }

    public void run() {
        System.out.println("Welcome!");

        while (true) {
            Console console = System.console();
            String input = console.readLine();

            if (commands.containsKey(input)) {
                commands.get(input).run();
            } else {
                spawn(input);
            }
        }
    }

    private void quit() {
        System.out.println("Quit Game");
        System.exit(0);
    }

    public void printBoard() {
        System.out.println(tetris.board);
    }

    private void printCurrentPiece() {
        System.out.println(tetris.currentPiece);
        System.out.println(Arrays.toString(tetris.currentPiece.getBody()));
    }

    private void spawn(String s) {
        tetris.spawn(s);
    }

    private void rotateLeft() {
        tetris.rotate(-1);
    }

    private void rotateRight() {
        tetris.rotate(1);
    }

    private void moveDown() {
        tetris.move(0, -1);
    }

    public void moveLeft() {
        tetris.move(-1, 0);
    }

    private void moveRight() {
        tetris.move(1, 0);
    }

    private void hardDrop() {
        tetris.hardDrop();
    }

    private void hardLeft() {
        tetris.moveHard(-1, 0);
    }

    private void hardRight() {
        tetris.moveHard(1, 0);
    }

    public void debug() {
        tetris.debug();
    }

    public void hold() {}
}
