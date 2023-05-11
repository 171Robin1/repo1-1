package algorithm;

import maze.Maze;
import util.Point;

import java.util.*;

public class AlgorithmA extends Algorithm
{
    public AlgorithmA(Maze m)
    {
        super(m);
    }

    public List<Point> getPath(Point x, Point y)
    {
        if (this.maze.isWall(x) || this.maze.isWall(y))
            return null;

        if (x.equals(y))
            return Arrays.asList(y);

        PriorityQueue<GraphNode> openList = new PriorityQueue<>(new GraphNodeComparator());
        List<GraphNode> closedList = new ArrayList<>();

        openList.add(new GraphNode(x, null, 0, AlgorithmA.calculateHeuristics(x, y)));

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
                    int g = node.getG() + 1;
                    int h = AlgorithmA.calculateHeuristics(point, y);
                    boolean f = false;

                    for (GraphNode n : openList)
                        if (n.getPos().equals(point) && n.getF() < g + h)
                        {
                            f = true;
                            break;
                        }

                    if (f)
                        continue;

                    for (GraphNode n : closedList)
                        if (n.getPos().equals(point) && n.getF() < g + h)
                        {
                            f = true;
                            break;
                        }

                    if (f)
                        continue;

                    openList.add(new GraphNode(point, node, g, h));
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
        private final int g;
        private final int h;


        public GraphNode(Point t, GraphNode p, int g, int h)
        {
            this.pos = t;
            this.parent = p;
            this.g = g;
            this.h = h;
        }

        public int getG()
        {
            return this.g;
        }

        public int getF()
        {
            return this.g + this.h;
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
            return o1.getF() - o2.getF();
        }
    }
}