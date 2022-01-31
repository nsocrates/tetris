package com.tetris;

import java.lang.Math;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum Shape {
    I_SHAPE("I", "c", new Point[] {
        new Point(0, 0),
        new Point(0, 1),
        new Point(0, 2),
        new Point(0, 3)
    }, new Point(1.5, 1.5)),

    O_SHAPE("O", "y", new Point[] {
        new Point(0, 0),
        new Point(0, 1),
        new Point(1, 0),
        new Point(1, 1)
    }, new Point(0.5, 0.5)),

    Z_SHAPE("Z", "r", new Point[] {
        new Point(0, 0),
        new Point(1, 0),
        new Point(1, 1),
        new Point(2, 1)
    }, new Point(1, 1)),

    S_SHAPE("S", "g", new Point[] {
        new Point(0, 1),
        new Point(1, 1),
        new Point(1, 0),
        new Point(2, 0)
    }, new Point(1, 1)),

    J_SHAPE("J", "b", new Point[] {
        new Point(0, 0),
        new Point(0, 1),
        new Point(0, 2),
        new Point(1, 0)
    }, new Point(1, 1)),

    L_SHAPE("L", "o", new Point[] {
        new Point(0, 0),
        new Point(1, 0),
        new Point(1, 1),
        new Point(1, 2)
    }, new Point(1, 1)),

    T_SHAPE("T", "m", new Point[] {
        new Point(0, 0),
        new Point(1, 0),
        new Point(1, 1),
        new Point(2, 0)
    }, new Point(1, 1));

    String color;
    String name;
    Point pivot;
    Point[] body;

    Shape(String name, String color, Point[] body, Point pivot) {
        this.color = color;
        this.name = name;
        this.pivot = pivot;
        this.body = body;
    };
}

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

    public static void generatePieces() {
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


    public static void addRotations(Piece root) {
        Piece prev = root;

        while (true) {
            Point[] nextBody = prev.computeRotation();
            Piece next = new Piece(nextBody, root.pivot, root.color, root.name);

            next.prev = prev;
            prev.next = next;

            if (next.equals(root)) {
                root.prev = prev;
                prev.next = root;
                break;
            } else {
                prev = next;
            }
        }
    }

    public static Piece get(String s) {
        if (Piece.dictionary == null) {
            Piece.generatePieces();
        }

        Piece piece = Piece.dictionary.get(s);

        return piece;
    }

    public Point[] body;
    public Point pivot;
    public String color;
    public String name;

    public double width;
    public double height;
    public double[] skirt;

    public Piece next;
    public Piece prev;

    public Piece(Point[] body, Point pivot, String color, String name) {
        this.body = body;
        this.pivot = pivot;
        this.color = color;
        this.name = name;

        for (int i = 0; i < body.length; i++) {
            double x = body[i].getX();
            double y = body[i].getY();

            if (x > width) {
                width = x;
            }

            if (y > height) {
                height = y;
            }
        }

        width += 1;
        height += 1;

        skirt = new double[(int)width];
        for (int i = 0; i < body.length; i++) {
            double x = body[i].getX();
            double y = body[i].getY();

            if (skirt[(int)x] > y) {
                skirt[(int)x] = y;
            }
        }
    }

    public String toString() {
        double matrixSize = Math.max(width, height);
        String s = "";

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                boolean isFilled = false;
                for (int k = 0; k < body.length; k++) {
                    Point element = body[k];
                    if (element.x == j && element.y == i) {
                        isFilled = true;
                        break;
                    }
                }

                s += isFilled ? color : ".";

                if (j < matrixSize - 1) {
                    s += " ";
                }
            }

            if (i < matrixSize - 1) {
                s += "\n";
            }
        }

        return s;
    }

    public void printCoordinates() {
        for (int i = 0; i < body.length; i++) {
            System.out.print(body[i]);
            System.out.print(" ");
        }
    }

    public Point[] computeRotation() {
        Point[] ret = new Point[body.length];
        double cx = pivot.getX();
        double cy = pivot.getY();

        for (int i = 0; i < body.length; i++) {
            Point point = body[i];
            double px = point.getX();
            double py = point.getY();
            double rx = cx + cy - py;
            double ry = cy - cx + px;
            ret[i] = new Point(rx, ry);
        }

        return ret;
    }
}
