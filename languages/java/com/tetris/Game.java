package com.tetris;

import java.io.Console;
import java.util.HashMap;

public class Game {
    protected Tetris tetris;
    private HashMap<String, Runnable> commands = new HashMap<>();

    public Game() {
        tetris = new Tetris();
        commands.put("p", () -> printBoard());
        commands.put("q", () -> quit());
        commands.put("t", () -> printActivePiece());
        commands.put("(", () -> rotateLeft());
        commands.put(")", () -> rotateRight());
    }

    public void run() {
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

    public void quit() {
        System.out.println("Quit Game");
        System.exit(0);
    }

    public void printBoard() {
        System.out.println(tetris.board);
    }

    public void printActivePiece() {
        System.out.println(tetris.activePiece);
    }

    public void spawn(String s) {
        tetris.spawn(s);
    }

    public void rotateLeft() {
        tetris.rotate(-1);
    }

    public void rotateRight() {
        tetris.rotate(1);
    }
}
