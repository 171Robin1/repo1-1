package entity;

import main.Main;
import maze.Maze;
import util.Point;
import algorithm.AlgorithmA;
import algorithm.Algorithm;

import java.util.List;
import java.util.Random;

public class EntityGhost extends Entity
{
    private Algorithm algorithm;
    private List<Point> path;
    private final Point restPos;
    private GhostBehaviour behaviour;
    private int timeout;

    public EntityGhost(Maze m, Point p, Point r, Class<? extends Algorithm> a)
    {
        super(m, p);

        try
        {
            this.algorithm = a.getConstructor(Maze.class).newInstance(this.maze);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            this.algorithm = new AlgorithmA(this.maze);
        }

        this.path = null;
        this.restPos = r;//this.map.getTile(this.map.getWidth() / 2, this.map.getHeight() / 2);
        this.behaviour = GhostBehaviour.RANDOM;
        this.timeout = 0;
    }

    public EntityGhost(Maze m, Point p)
    {
        this(m, p, p, AlgorithmA.class);
    }

    public GhostBehaviour getBehaviour()
    {
        return behaviour;
    }

    public void move()
    {
        EntityPacman pacman = this.maze.getPacman();
        Random random = new Random();
        --this.timeout;

        if (this.timeout <= 0)
        {
            this.path = null;

            if (this.behaviour != GhostBehaviour.REST)
            {
                this.timeout += (5 + random.nextInt(6)) * Main.gameplayTicksPerSecond;
                this.behaviour = GhostBehaviour.REST;
            }
            else
            {
                this.timeout += (10 + random.nextInt(11)) * Main.gameplayTicksPerSecond;
                this.behaviour = GhostBehaviour.RANDOM;
            }
        }

        if (pacman != null)
        {
            if (pacman.getPos().equals(this.pos))
                pacman.setDead();

            if (this.behaviour == GhostBehaviour.RANDOM && this.maze.getPacman().getPos().getDistance(this.pos) <= 5)
            {
                this.behaviour = GhostBehaviour.ATTACK;
                this.timeout = 10 * Main.gameplayTicksPerSecond;
            }
        }
        else
        {
            if (this.behaviour == GhostBehaviour.ATTACK)
                this.behaviour = GhostBehaviour.RANDOM;
        }

        /*if (this.behaviour == GhostBehaviour.REST && this.getPos().posEquals(this.restPos))
            this.behaviour = GhostBehaviour.RANDOM;*/

        while (this.path == null)
        {
            switch (this.behaviour)
            {
                case RANDOM:
                    this.path = this.algorithm.getPath(this.pos, new Point(
                            random.nextInt(this.maze.getWidth()),
                            random.nextInt(this.maze.getHeight())
                    ));
                    break;
                case REST:
                    this.path = this.algorithm.getPath(this.pos, this.restPos);
                    break;
                case ATTACK:
                    this.path = this.algorithm.getPath(this.pos, pacman.getPos());
                    break;
            }
        }

        if (this.path.size() == 1)
            this.path = null;
        else
        {
            this.path.remove(0);
            this.pos = new Point(this.path.get(0).getX(), this.path.get(0).getY());

            if (this.behaviour == GhostBehaviour.ATTACK)
                this.path = null;
        }

        if (pacman != null && pacman.getPos().equals(this.pos))
            pacman.setDead();
    }

    public enum GhostBehaviour
    {
        REST,
        RANDOM,
        ATTACK
    }
}
