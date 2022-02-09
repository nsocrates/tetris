package com.tetris;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        x = p.getX();
        y = p.getY();
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

    public Point add(int x, int y) {
        return new Point(this.x + x, this.y + y);
    }

    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    public void move(int dx, int dy) {
        Point point = this.add(dx, dy);
        this.x = point.getX();
        this.y = point.getY();
    }

    public double distance(int dx, int dy) {
        double sqDx = Math.pow(dx - this.x, 2);
        double sqDy = Math.pow(dy - this.y, 2);
        return Math.sqrt(sqDx + sqDy);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
