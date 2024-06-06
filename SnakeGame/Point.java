package SnakeGame;

public class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point add(Point p1, Point p2) {
        return new Point(p1.x + p2.x, p1.y + p2.y);
    }

    public static Point addClamp(Point p1, Point p2, int xEnd, int yEnd) {
        Point ret = new Point(p1.x + p2.x, p1.y + p2.y);
        if (ret.x < 0) ret.x = xEnd - 1;
        else if (ret.x >= xEnd) ret.x = 0;
        if (ret.y < 0) ret.y = yEnd - 1;
        else if (ret.y >= yEnd) ret.y = 0;
        return ret;
    }
}