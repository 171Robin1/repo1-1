package algorithm;

import maze.Maze;
import util.Point;

import java.util.List;

public abstract class Algorithm
{
    protected final Maze maze;

    public Algorithm(Maze m)
    {
        this.maze = m;
    }

    public static int getDistance(Point x, Point y)
    {
        int a = x.getX() - y.getX();
        int b = x.getY() - y.getY();

        if (a < 0)
            a = -a;

        if (b < 0)
            b = -b;

        return a + b;
    }

    public abstract List<Point> getPath(Point x, Point y);
}
