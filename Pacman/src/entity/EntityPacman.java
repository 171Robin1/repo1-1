package entity;

import maze.Maze;
import algorithm.AlgorithmPacman;
import algorithm.Algorithm;
import util.Point;

import java.util.ArrayList;
import java.util.List;

public class EntityPacman extends Entity
{
    private int direction;
    private boolean isPlayerControlled;
    private List<Point> path;

    public EntityPacman(Maze m, Point p)
    {
        super(m, p);
        this.path = null;
        this.isPlayerControlled = false;
    }

    public int getDirection()
    {
        return direction;
    }

    public void setDirection(int d)
    {
        this.direction = d;
    }

    public boolean isPlayerControlled()
    {
        return isPlayerControlled;
    }

    public void setPlayerControlled(boolean playerControlled)
    {
        isPlayerControlled = playerControlled;
    }

    public void move()
    {
        this.maze.eatDot(this.pos);

        if (this.isPlayerControlled)
        {
            int dx = 0;
            int dy = 0;

            switch (this.direction) {
                case 0:
                    dx = -1;
                    break;
                case 1:
                    dy = -1;
                    break;
                case 2:
                    dx = 1;
                    break;
                case 3:
                    dy = 1;
                    break;
            }

            int x = this.pos.getX() + dx;
            int y = this.pos.getY() + dy;

            if (x >= 0 && x < this.maze.getWidth() && y >= 0 && y < this.maze.getHeight())
                if (!this.maze.isWall(x, y))
                    this.pos = new Point(x, y);
        }
        else
        {
            if (this.path == null || this.path.size() == 1)
            {
                List<Point> escapePath = new ArrayList<>();
                List<Point> foodPath = new ArrayList<>();

                Algorithm algorithm = new AlgorithmPacman(this.maze);

                for (int x = 0; x < this.maze.getWidth(); ++x)
                    for(int y = 0; y < this.maze.getHeight(); ++y)
                    {
                        List<Point> p = algorithm.getPath(this.pos, new Point(x, y));

                        if (p != null)
                        {
                            if (this.maze.isDot(x, y))
                                if (foodPath.size() == 0 || foodPath.size() < p.size())
                                    foodPath = p;

                            if (escapePath.size() < p.size())
                                escapePath = p;
                        }
                    }

                if (escapePath.size() == 0 && foodPath.size() == 0)
                    this.path = null;
                else if (escapePath.size() == 0)
                    this.path = foodPath;
                else if (foodPath.size() == 0)
                    this.path = escapePath;
                else
                {
                    int d = maze.getWidth() * maze.getHeight();

                    for (EntityGhost ghost : maze.getGhosts())
                    {
                        if (d < this.pos.getDistance(ghost.getPos()))
                            d = (int) this.pos.getDistance(ghost.getPos());
                    }

                    if (d > 7)
                        this.path = foodPath;
                    else
                        this.path = escapePath;
                }
            }

            if (this.path == null || this.path.size() == 1)
                this.path = null;
            else
            {
                this.path.remove(0);

                Point target = this.path.get(0);

                if (this.pos.getX() > target.getX())
                    this.direction = 0;
                else if (this.pos.getX() < target.getX())
                    this.direction = 2;
                else if (this.pos.getY() > target.getY())
                    this.direction = 1;
                else if (this.pos.getY() < target.getY())
                    this.direction = 3;

                this.pos = new Point(target.getX(), target.getY());
            }
        }
    }
}
