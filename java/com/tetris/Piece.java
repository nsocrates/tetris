package com.tetris;

import java.lang.Math;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Piece {
    public static Piece[] pieces = null;
    public static Map<String, Piece> dictionary = null;

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Piece)) {
            return false;
        }

        final Piece p = (Piece) other;
        List<Point> a = Arrays.asList(this.body);
        List<Point> b = Arrays.asList(p.body);

        return a.containsAll(b);
    }

    private static void addRotations(Piece root) {
        Piece prev = root;

        while (true) {
            Piece next = prev.computeNextRotation();

            if (next.equals(root)) {
                root.prev = prev;
                prev.next = root;
                break;
            } else {
                next.prev = prev;
                prev.next = next;
                prev = next;
            }
        }
    }

    public static void generatePieces() {
        if (Piece.pieces == null || Piece.dictionary == null) {
            Shape[] values = Shape.values();
            Piece[] pieces = new Piece[values.length];
            Map<String, Piece> dictionary = new HashMap<String, Piece>();

            for (int i = 0; i < values.length; i++) {
                Shape s = values[i];
                Piece piece = new Piece(s.body, s.pivot, s.color, s.name);
                Piece.addRotations(piece);
                dictionary.put(s.name, piece);
                pieces[i] = piece;
            }

            Piece.pieces = pieces;
            Piece.dictionary = dictionary;
        }
    }

    public static Piece get(String s) {
        if (Piece.dictionary == null) {
            Piece.generatePieces();
        }

        return Piece.dictionary.get(s);
    }

    private Point[] body;
    private double[] pivot;
    private String color;
    private String name;

    private int width;
    private int height;
    private int[] skirt;

    private Piece next = null;
    private Piece prev = null;

    public Piece(Point[] body, double[] pivot, String color, String name) {
        this.body = body;
        this.pivot = pivot;
        this.color = color;
        this.name = name;

        for (int i = 0; i < body.length; i++) {
            int x = body[i].getX();
            int y = body[i].getY();

            width = Math.max(x, width);
            height = Math.max(y, height);
        }

        width += 1;
        height += 1;

        skirt = new int[width];
        Arrays.fill(skirt, height - 1);
        for (int i = 0; i < body.length; i++) {
            int x = body[i].getX();
            int y = body[i].getY();
            skirt[x] = Math.min(skirt[x], y);
        }
    }

    private Piece computeNextRotation() {
        Point[] rotatedBody = new Point[body.length];

        for (int i = 0; i < body.length; i++) {
            Point point = body[i];
            int px = point.getX();
            int py = point.getY();
            int rx = height - 1 - py;
            int ry = px;
            rotatedBody[i] = new Point(rx, ry);
        }

        return new Piece(rotatedBody, pivot, color, name);
    }

    public String toString() {
        int drawSize = Math.max(width, height);
        String s = "";

        for (int i = drawSize - 1; i >= 0; i--) {
            for (int j = 0; j < drawSize; j++) {
                boolean isFilled = false;
                for (int k = 0; k < body.length; k++) {
                    Point element = body[k];
                    if (element.getX() == j && element.getY() == i) {
                        isFilled = true;
                        break;
                    }
                }

                s += isFilled ? color : ".";

                if (j < drawSize - 1) {
                    s += " ";
                }
            }

            if (i > 0) {
                s += "\n";
            }
        }

        return s;
    }

    public Point[] getBody() {
        return body;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getSkirt() {
        return skirt;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public Piece getRotated(int dir) {
        if (dir == 1) {
            return next;
        }

        if (dir == -1) {
            return prev;
        }

        return this;
    }
}
