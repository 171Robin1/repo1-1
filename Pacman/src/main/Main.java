package main;

import entity.EntityGhost;
import entity.EntityPacman;
import maze.Maze;
import algorithm.AlgorithmA;
import algorithm.AlgorithmGreedy;
import util.Point;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class GraphicsPanel extends JPanel implements KeyListener
{
    private static final BufferedImage wallImage;
    private static final BufferedImage ghostImage;
    private static final BufferedImage pacmanImages[];
    private static final BufferedImage dotImage;

    public static final int length = 25;

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Maze maze = Main.maze;

        g.setColor(Color.black);
        g.drawRect(0, 0, maze.getWidth() * length, maze.getHeight() * length);

        for (int y = 0; y < maze.getHeight(); ++y)
        {
            for (int x = 0; x < maze.getWidth(); ++x)
            {
                if (maze.isWall(x, y))
                    g.drawImage(wallImage, x * length, y * length, length, length, null);

                if (maze.isDot(x, y))
                    g.drawImage(dotImage, x * length, y * length, length, length, null);
            }
        }

        for (EntityGhost ghost : maze.getGhosts())
            g.drawImage(ghostImage, ghost.getPos().getX() * length, ghost.getPos().getY() * length, length, length, null);

        EntityPacman pacman = maze.getPacman();

        if (pacman != null)
            g.drawImage(pacmanImages[pacman.getDirection()], pacman.getPos().getX() * length, pacman.getPos().getY() * length, length, length, null);
    }

    static
    {
        try {
            wallImage = ImageIO.read(new File("sprite/wall.png"));
            ghostImage = ImageIO.read(new File("sprite/ghost.png"));
            dotImage = ImageIO.read(new File("sprite/dot.png"));

            pacmanImages = new BufferedImage[4];
            pacmanImages[0] = ImageIO.read(new File("sprite/pacman0.png"));
            pacmanImages[1] = ImageIO.read(new File("sprite/pacman1.png"));
            pacmanImages[2] = ImageIO.read(new File("sprite/pacman2.png"));
            pacmanImages[3] = ImageIO.read(new File("sprite/pacman3.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        Maze maze = Main.maze;

        switch (e.getKeyCode())
        {
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                maze.getPacman().setDirection(2);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                maze.getPacman().setDirection(3);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                maze.getPacman().setDirection(0);
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                maze.getPacman().setDirection(1);
                break;
            case KeyEvent.VK_R:
                if (maze.getPacman() == null)
                    new EntityPacman(maze, new Point(0, 0));
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}

public class Main
{
    public static Maze maze;
    public static final int gameplayTicksPerSecond = 4;
    public static final int framesPerSecond = 30;

    public static void main(String[] args)
    {
        maze = Maze.getRandom();
        int xp = maze.getWidth() / 2;
        int yp = maze.getHeight() / 2;
        int xm = xp - 1;
        int ym = yp - 1;
        ++xp;
        ++yp;
        new EntityGhost(maze, new Point(xm, ym), new Point(0, 0), AlgorithmA.class);
        new EntityGhost(maze, new Point(xm, yp), new Point(maze.getWidth() - 1, 0), AlgorithmA.class);
        new EntityGhost(maze, new Point(xp, ym), new Point(0, maze.getHeight() - 1), AlgorithmGreedy.class);
        new EntityGhost(maze, new Point(xp, yp), new Point(maze.getWidth() - 1, maze.getHeight() - 1), AlgorithmGreedy.class);
        new EntityPacman(maze, new Point(0, 0));

        //System.out.println(maze.getDistance(new Point(0,0), new Point(1,0)));

        //printMap(level);

        /*AStar aStar = new AStar(map);
        List<Tile> list = aStar.getPath(map.getTile(0,0), map.getTile(map.getWidth() - 1, map.getHeight() - 1));

        if (list != null)
        {
            System.out.println("not null");
            for (Tile tile : list)
                map.getTile(tile.getX(), tile.getY()).setType(TileType.FOOD);
            //System.out.println(tile.getX() + " " + tile.getY());
        }
        else
            System.out.println("null");*/

        //printMap(map);

        JFrame frame = new JFrame("FrameDemo");

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //3. Create components and put them in the frame.
        // ...create emptyLabel...
        //frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        GraphicsPanel panel = new GraphicsPanel();

        panel.addKeyListener(panel);
        panel.setFocusable(true);

        // Adding buttons and textfield to panel
        // using add() method
        //panel.add(b);
        //panel.add(b1);
        //panel.add(b2);
        //panel.add(l);

        // setbackground of panel
        panel.setBackground(Color.black);
        panel.setPreferredSize(new Dimension(GraphicsPanel.length * maze.getWidth(), GraphicsPanel.length * maze.getHeight()));

        // Adding panel to frame
        frame.add(panel);

        // Setting the size of frame
        //frame.setSize(300, 300);
        // 4. Size the frame.
        frame.pack();

        //5. Show it.
        frame.setVisible(true);

        Thread graphicsThread = new Thread(() -> {
            long time = System.currentTimeMillis();

            while (true)
            {
                while (System.currentTimeMillis() < time + 1000 / framesPerSecond)
                    ;

                time = System.currentTimeMillis();
                frame.repaint();
            }
        });
        graphicsThread.start();

        Thread gameplayThread = new Thread(() -> {
            long time = System.currentTimeMillis();

            while (true)
            {
                while (System.currentTimeMillis() < time + 1000 / gameplayTicksPerSecond)
                    ;

                time = System.currentTimeMillis();

                maze.move();

                boolean f = true;

                for (int x = 0; f && x < maze.getWidth(); ++x)
                    for (int y = 0; f && y < maze.getHeight(); ++y)
                        if (maze.isDot(x, y))
                            f = false;

                if (f)
                {
                    JOptionPane.showMessageDialog(frame, "Victory.");
                    System.exit(0);
                }
            }
        });
        gameplayThread.start();
    }
}
