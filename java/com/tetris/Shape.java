package com.tetris;

enum Shape {
    I_SHAPE("I", "c", new Point[] {
        new Point(0, 0),
        new Point(0, 1),
        new Point(0, 2),
        new Point(0, 3)
    }, new double[] { 1.5, 1.5 }),

    O_SHAPE("O", "y", new Point[] {
        new Point(0, 0),
        new Point(0, 1),
        new Point(1, 0),
        new Point(1, 1)
    }, new double[] { 0.5, 0.5 }),

    Z_SHAPE("Z", "r", new Point[] {
        new Point(0, 0),
        new Point(1, 0),
        new Point(1, 1),
        new Point(2, 1)
    }, new double[] { 1, 1 }),

    S_SHAPE("S", "g", new Point[] {
        new Point(0, 1),
        new Point(1, 1),
        new Point(1, 0),
        new Point(2, 0)
    }, new double[] { 1, 1 }),

    J_SHAPE("J", "b", new Point[] {
        new Point(0, 0),
        new Point(0, 1),
        new Point(0, 2),
        new Point(1, 0)
    }, new double[] { 1, 1 }),

    L_SHAPE("L", "o", new Point[] {
        new Point(0, 0),
        new Point(1, 0),
        new Point(1, 1),
        new Point(1, 2)
    }, new double[] { 1, 1 }),

    T_SHAPE("T", "m", new Point[] {
        new Point(0, 0),
        new Point(1, 0),
        new Point(1, 1),
        new Point(2, 0)
    }, new double[] { 1, 1 });

    String color;
    String name;
    double[] pivot;
    Point[] body;

    Shape(String name, String color, Point[] body, double[] pivot) {
        this.color = color;
        this.name = name;
        this.pivot = pivot;
        this.body = body;
    };
}
