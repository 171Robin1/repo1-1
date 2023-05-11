package algorithm;

import maze.Maze;
import util.Point;

import java.util.*;

public class AlgorithmGreedy extends Algorithm
{
    public AlgorithmGreedy(Maze m)
    {
        super(m);
    }

    @Override
    public List<Point> getPath(Point x, Point y)
    {
        if (this.maze.isWall(x) || this.maze.isWall(y))
            return null;

        if (x.equals(y))
            return Arrays.asList(y);

        PriorityQueue<GraphNode> openList = new PriorityQueue<>(new GraphNodeComparator());
        List<GraphNode> closedList = new ArrayList<>();

        openList.add(new GraphNode(x, null, AlgorithmGreedy.calculateHeuristics(x, y)));

        while (openList.size() > 0)
        {
            GraphNode node = openList.remove();
            List<Point> successors = this.maze.getNeighbors(node.getPos());

            for (Point point : successors)
            {
                if (point.equals(y))
                {
                    List<Point> res = new ArrayList<>();

                    res.add(y);
                    res.add(node.getPos());

                    while (node.getParent() != null)
                    {
                        node = node.getParent();
                        res.add(node.getPos());
                    }

                    Collections.reverse(res);

                    return res;
                }
                else
                {
                    int h = AlgorithmGreedy.calculateHeuristics(point, y);
                    boolean f = false;

                    for (GraphNode n : closedList)
                        if (n.getPos().equals(point))
                        {
                            f = true;
                            break;
                        }

                    if (!f)
                        openList.add(new GraphNode(point, node, h));
                }
            }

            closedList.add(node);
        }

        return null;
    }

    private static int calculateHeuristics(Point x, Point y)
    {
        return Algorithm.getDistance(x, y);
    }

    private class GraphNode
    {
        private final Point pos;
        private final GraphNode parent;
        private final int h;


        public GraphNode(Point t, GraphNode p, int h)
        {
            this.pos = t;
            this.parent = p;
            this.h = h;
        }

        public int getH()
        {
            return this.h;
        }

        public Point getPos()
        {
            return pos;
        }

        public GraphNode getParent()
        {
            return parent;
        }
    }

    private class GraphNodeComparator implements Comparator<GraphNode>
    {
        @Override
        public int compare(GraphNode o1, GraphNode o2)
        {
            return o1.getH() - o2.getH();
        }
    }
}
