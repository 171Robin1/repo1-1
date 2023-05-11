package maze;

import entity.Entity;
import entity.EntityGhost;
import entity.EntityPacman;
import util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze {
    private final int width;
    private final int height;

    private boolean [][] walls;
    private boolean [][] dots;

    private List<Entity> entities;

    //private int [][][][] distances;

    public Maze(int w, int h)
    {
        this.entities = new ArrayList<>();
        this.width = w;
        this.height = h;
        this.walls = new boolean[w][h];
        this.dots = new boolean[w][h];
        //this.distances = null;
        Random random = new Random();

        for (int x = 0; x < this.width; ++x)
            for (int y = 0; y < this.height; ++y)
            {
                this.walls[x][y] = false;
                this.dots[x][y] = false;
            }

                //this.level[x][y] = new Tile(x, y, random.nextInt(100) < 0 ? TileType.FOOD : TileType.EMPTY);
    }

    /*public int getDistance(Point t1, Point t2)
    {
        if (this.distances == null)
        {
            AStar aStar = new AStar(this);
            this.distances = new int[this.width][this.height][this.width][this.height];

            for (int x = 0; x < this.width; ++x)
                for (int y = 0; y < this.height; ++y)
                    for (int x1 = 0; x1 < this.width; ++x1)
                        for (int y1 = 0; y1 < this.height; ++y1)
                        {
                            List<Point> p = aStar.getPath(new Point(x, y), new Point(x1, y1));

                            this.distances[x1][y1][x][y] = p != null ? p.size() - 1 : this.width * this.height;
                        }
        }

        return this.distances[t1.getX()][t1.getY()][t2.getX()][t2.getY()];
    }*/

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean isWall(Point x)
    {
        return this.walls[x.getX()][x.getY()];
    }

    public boolean isWall(int x, int y)
    {
        return this.walls[x][y];
    }

    public void setWall(int x, int y)
    {
        this.walls[x][y] = true;
        this.dots[x][y] = false;
    }

    public boolean isDot(Point x)
    {
        return this.dots[x.getX()][x.getY()];
    }

    public boolean isDot(int x, int y)
    {
        return this.dots[x][y];
    }

    public void eatDot(Point x)
    {
        this.dots[x.getX()][x.getY()] = false;
    }

    public void eatDot(int x, int y)
    {
        this.dots[x][y] = false;
    }

    public void move()
    {
        for (Entity entity : this.entities)
        {
            if (entity.getPos().getX() < 0 || entity.getPos().getX() >= this.width || entity.getPos().getY() < 0 || entity.getPos().getY() >= this.height)
                entity.setDead();

            if (entity.isAlive())
                entity.move();
        }

        this.entities.removeIf((Entity e) -> !e.isAlive());
    }

    public List<Point> getNeighbors(Point t)
    {
        List<Point> neighbors = new ArrayList<>();

        if (t.getX() != 0 && !this.isWall(t.getX() - 1, t.getY()))
            neighbors.add(new Point(t.getX() - 1, t.getY()));

        if (t.getY() != 0 && !this.isWall(t.getX(), t.getY() - 1))
            neighbors.add(new Point(t.getX(), t.getY() - 1));

        if (t.getX() != this.getWidth() - 1 && !this.isWall(t.getX() + 1, t.getY()))
            neighbors.add(new Point(t.getX() + 1, t.getY()));

        if (t.getY() != this.getHeight() - 1 && !this.isWall(t.getX(), t.getY() + 1))
            neighbors.add(new Point(t.getX(), t.getY() + 1));

        return neighbors;
    }

    public List<EntityGhost> getGhosts()
    {
        List<EntityGhost> res = new ArrayList<>();

        for (Entity e : this.entities)
            if (e instanceof EntityGhost)
                res.add((EntityGhost) e);

        return res;
    }

    public EntityPacman getPacman()
    {
        for (Entity e : this.entities)
            if (e instanceof EntityPacman)
                return (EntityPacman) e;

        return null;
    }

    public void addEnity(Entity e)
    {
        this.entities.add(e);
    }

    public Maze getCopy()
    {
        Maze m = new Maze(this.width, this.height);

        for (int x = 0; x < this.width; ++x)
            for (int y = 0; y < this.height; ++y)
            {
                m.walls[x][y] = this.walls[x][y];
                m.dots[x][y] = this.dots[x][y];
            }

        return m;
    }

    public static Maze getRandom()
    {
        Random random = new Random();

        int l = 5;
        l = 6 * l + 1;

        Maze m = new Maze(l, l);

        int x;
        int y;

        for (x = 3; x < m.width; x += 6)
            for (y = 3; y < m.height; y += 6)
            {
                switch (random.nextInt(3))
                {
                    case 0:
                    {
                        m.walls[x - 2][y - 2] = true;
                        m.walls[x][y - 2] = true;
                        m.walls[x + 2][y - 2] = true;

                        m.walls[x][y - 1] = true;

                        m.walls[x - 2][y] = true;
                        m.walls[x - 1][y] = true;
                        m.walls[x][y] = true;
                        m.walls[x + 1][y] = true;
                        m.walls[x + 2][y] = true;

                        m.walls[x][y + 1] = true;

                        m.walls[x - 2][y + 2] = true;
                        m.walls[x][y + 2] = true;
                        m.walls[x + 2][y + 2] = true;
                    }
                    break;
                    case 1:
                    {
                        switch (random.nextInt(2))
                        {
                            case 0:
                            {
                                m.walls[x - 2][y - 2] = true;
                                m.walls[x - 1][y - 2] = true;
                                ;
                                m.walls[x + 1][y - 2] = true;
                                m.walls[x + 2][y - 2] = true;

                                m.walls[x - 2][y - 1] = true;
                                m.walls[x - 1][y - 1] = true;
                                ;
                                ;
                                m.walls[x + 2][y - 1] = true;

                                ;
                                ;
                                m.walls[x][y] = true;
                                ;
                                ;

                                m.walls[x - 2][y + 1] = true;
                                ;
                                ;
                                m.walls[x + 1][y + 1] = true;
                                m.walls[x + 2][y + 1] = true;

                                m.walls[x - 2][y + 2] = true;
                                m.walls[x - 1][y + 2] = true;
                                ;
                                m.walls[x + 1][y + 2] = true;
                                m.walls[x + 2][y + 2] = true;
                            }
                            break;
                            case 1:
                            {
                                m.walls[x - 2][y - 2] = true;
                                m.walls[x - 1][y - 2] = true;
                                ;
                                m.walls[x + 1][y - 2] = true;
                                m.walls[x + 2][y - 2] = true;

                                m.walls[x - 2][y - 1] = true;
                                ;
                                ;
                                m.walls[x + 1][y - 1] = true;
                                m.walls[x + 2][y - 1] = true;

                                ;
                                ;
                                m.walls[x][y] = true;
                                ;
                                ;

                                m.walls[x - 2][y + 1] = true;
                                m.walls[x - 1][y + 1] = true;
                                ;
                                ;
                                m.walls[x + 2][y + 1] = true;

                                m.walls[x - 2][y + 2] = true;
                                m.walls[x - 1][y + 2] = true;
                                ;
                                m.walls[x + 1][y + 2] = true;
                                m.walls[x + 2][y + 2] = true;
                            }
                            break;
                        }
                    }
                    break;
                    case 2:
                    {
                        switch (random.nextInt(4))
                        {
                            case 0:
                            {
                                ;
                                m.walls[x - 1][y - 2] = true;
                                ;
                                m.walls[x + 1][y - 2] = true;
                                m.walls[x + 2][y - 2] = true;

                                m.walls[x - 2][y - 1] = true;
                                m.walls[x - 1][y - 1] = true;
                                ;
                                ;
                                m.walls[x + 2][y - 1] = true;

                                ;
                                ;
                                m.walls[x][y] = true;
                                ;
                                m.walls[x + 2][y] = true;

                                m.walls[x - 2][y + 1] = true;
                                ;
                                ;
                                ;
                                m.walls[x + 2][y + 1] = true;

                                m.walls[x - 2][y + 2] = true;
                                m.walls[x - 1][y + 2] = true;
                                m.walls[x][y + 2] = true;
                                m.walls[x + 1][y + 2] = true;
                                m.walls[x + 2][y + 2] = true;
                            }
                            break;
                            case 1:
                            {
                                m.walls[x - 2][y - 2] = true;
                                m.walls[x - 1][y - 2] = true;
                                m.walls[x][y - 2] = true;
                                m.walls[x + 1][y - 2] = true;
                                m.walls[x + 2][y - 2] = true;

                                m.walls[x - 2][y - 1] = true;
                                ;
                                ;
                                ;
                                m.walls[x + 2][y - 1] = true;

                                ;
                                ;
                                m.walls[x][y] = true;
                                ;
                                m.walls[x + 2][y] = true;

                                m.walls[x - 2][y + 1] = true;
                                m.walls[x - 1][y + 1] = true;
                                ;
                                ;
                                m.walls[x + 2][y + 1] = true;

                                ;
                                m.walls[x - 1][y + 2] = true;
                                ;
                                m.walls[x + 1][y + 2] = true;
                                m.walls[x + 2][y + 2] = true;
                            }
                            break;
                            case 2:
                            {
                                m.walls[x - 2][y - 2] = true;
                                m.walls[x - 1][y - 2] = true;
                                m.walls[x][y - 2] = true;
                                m.walls[x + 1][y - 2] = true;
                                m.walls[x + 2][y - 2] = true;

                                m.walls[x - 2][y - 1] = true;
                                ;
                                ;
                                ;
                                m.walls[x + 2][y - 1] = true;

                                m.walls[x - 2][y] = true;
                                ;
                                m.walls[x][y] = true;
                                ;
                                ;

                                m.walls[x - 2][y + 1] = true;
                                ;
                                ;
                                m.walls[x + 1][y + 1] = true;
                                m.walls[x + 2][y + 1] = true;

                                m.walls[x - 2][y + 2] = true;
                                m.walls[x - 1][y + 2] = true;
                                ;
                                m.walls[x + 1][y + 2] = true;
                                ;
                            }
                            break;
                            case 3:
                            {
                                m.walls[x - 2][y - 2] = true;
                                m.walls[x - 1][y - 2] = true;
                                ;
                                m.walls[x + 1][y - 2] = true;
                                ;

                                m.walls[x - 2][y - 1] = true;
                                ;
                                ;
                                m.walls[x + 1][y - 1] = true;
                                m.walls[x + 2][y - 1] = true;

                                m.walls[x - 2][y] = true;
                                ;
                                m.walls[x][y] = true;
                                ;
                                ;

                                m.walls[x - 2][y + 1] = true;
                                ;
                                ;
                                ;
                                m.walls[x + 2][y + 1] = true;

                                m.walls[x - 2][y + 2] = true;
                                m.walls[x - 1][y + 2] = true;
                                m.walls[x][y + 2] = true;
                                m.walls[x + 1][y + 2] = true;
                                m.walls[x + 2][y + 2] = true;
                            }
                            break;
                        }

                    }
                    break;
                }
            }

        {
            x = l / 2;
            y = l / 2;

            m.walls[x - 2][y - 2] = false;
            m.walls[x - 1][y - 2] = false;
            m.walls[x][y - 2] = true;
            m.walls[x + 1][y - 2] = false;
            m.walls[x + 2][y - 2] = false;

            m.walls[x - 2][y - 1] = false;
            m.walls[x - 1][y - 1] = false;
            m.walls[x][y - 1] = true;
            m.walls[x + 1][y - 1] = false;
            m.walls[x + 2][y - 1] = false;

            m.walls[x - 2][y] = true;
            m.walls[x - 1][y] = true;
            m.walls[x][y] = true;
            m.walls[x + 1][y] = true;
            m.walls[x + 2][y] = true;

            m.walls[x - 2][y + 1] = false;
            m.walls[x - 1][y + 1] = false;
            m.walls[x][y + 1] = true;
            m.walls[x + 1][y + 1] = false;
            m.walls[x + 2][y + 1] = false;

            m.walls[x - 2][y + 2] = false;
            m.walls[x - 1][y + 2] = false;
            m.walls[x][y + 2] = true;
            m.walls[x + 1][y + 2] = false;
            m.walls[x + 2][y + 2] = false;
        }

        for (x = 0; x < l; ++x)
            for (y = 0; y < l; ++y)
                if (!m.walls[x][y])
                    m.dots[x][y] = true;

        return m;
    }
}
