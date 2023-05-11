package entity;

import maze.Maze;
import util.Point;

public abstract class Entity
{
    protected Maze maze;
    protected Point pos;
    private boolean isAlive;

    public Entity(Maze m, Point p)
    {
        this.maze = m;
        this.pos = p;

        if (this.maze != null)
        {
            this.maze.addEnity(this);
            this.isAlive = true;
        }
    }

    public abstract void move();

    public final boolean isAlive()
    {
        return this.isAlive;
    }

    public final void setDead()
    {
        this.isAlive = false;
    }

    public final Maze getMaze()
    {
        return this.maze;
    }

    public final Point getPos()
    {
        return this.pos;
    }
}
