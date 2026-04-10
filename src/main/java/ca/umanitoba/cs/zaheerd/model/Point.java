package ca.umanitoba.cs.zaheerd.model;

import com.google.common.base.Preconditions;

public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        Preconditions.checkState(x >= 0, "x co-ordinate must be non-negative");
        Preconditions.checkState(y >= 0, "y co-ordinate must be non-negative");
        this.x = x;
        this.y = y;
        checkPoint();
    }

    public int getX() { return x; }
    public int getY() { return y; }

    private void checkPoint() {
        Preconditions.checkState(x >= 0, "x must be non-negative");
        Preconditions.checkState(y >= 0, "y must be non-negative");
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
