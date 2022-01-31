package com.tetris;

import java.lang.Math;

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Point)) {
            return false;
        }

        final Point p = (Point) other;

        return p.x == x && p.y == y;
    }

    public Point add(double x, double y) {
        return new Point(this.x + x, this.y + y);
    }

    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    public double distance(double dx, double dy) {
        double sqDx = Math.pow(dx - this.x, 2);
        double sqDy = Math.pow(dy - this.y, 2);
        double sqrtD = Math.sqrt(sqDx + sqDy);
        return sqrtD;
    }

    public Point move(double dx, double dy) {
        Point point = this.add(dx, dy);
        this.x = point.x;
        this.y = point.y;
        return this;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
