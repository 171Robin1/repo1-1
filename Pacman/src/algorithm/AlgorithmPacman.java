package algorithm;

import entity.EntityGhost;
import maze.Maze;
import util.Point;

import java.util.*;

public class AlgorithmPacman extends Algorithm
{
    private final AlgorithmA aStar;
    public AlgorithmPacman(Maze m)
    {
        super(m);
        this.aStar = new AlgorithmA(m);
    }

    @Override
    public List<Point> getPath(Point x, Point y)
    {
        if (this.maze.isWall(x) || this.maze.isWall(y))
            return null;

        if (x.equals(y))
            return Arrays.asList(y);

        Maze m = this.maze;
        List<Map<Point, Point>> list = new ArrayList<>();
        list.add(new HashMap<>());
        list.get(0).put(x, null);
        int s = 0;

        while (list.get(s).size() > 0)
        {
            if (list.get(s).containsKey(y))
            {
                List<Point> res = new ArrayList<>();
                res.add(y);
                Point point = y;

                while (s > 0)
                {
                    point = list.get(s).get(point);
                    res.add(point);
                    --s;
                }

                Collections.reverse(res);

                return res;
            }

            m = m.getCopy();

            for (EntityGhost ghost : this.maze.getGhosts())
                for (int i = 0; i < m.getWidth(); ++i)
                    for (int j = 0; j < m.getHeight(); ++j)
                        if (ghost.getPos().getDistance(new Point(i, j)) <= s + 1)
                            m.setWall(i, j);


            list.add(new HashMap<>());

            for (Map.Entry<Point, Point> entry : list.get(s).entrySet())
            {
                List<Point> sucessors = m.getNeighbors(entry.getKey());

                for (Point point : sucessors)
                    list.get(s + 1).put(point, entry.getKey());
            }

            ++s;
        }

        return null;
    }
}
