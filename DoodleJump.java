import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DoodleJump extends JPanel implements ActionListener, KeyListener {

    private Player player;
    private ArrayList<Platform> platforms;
    private Timer timer;
    private Random rand;

    private boolean hasLanded = false;
    private boolean isGameOver = false;
    private int score = 0;

    private BufferedImage backgroundImage;
    private BufferedImage playerImage;
    private BufferedImage platformImage;

    public DoodleJump() {

        setPreferredSize(new Dimension(600, 800));
        setFocusable(true);
        addKeyListener(this);
        rand = new Random();

        try {

            backgroundImage = ImageIO.read(new File("background.png"));
            playerImage = ImageIO.read(new File("player.png"));
            platformImage = ImageIO.read(new File("platform.png"));
        } 
        catch (IOException e) {

            System.out.println("Image loading failed: " + e.getMessage());
        }

        startGame();
    }

    private void startGame() {

        player = new Player(262, 725, playerImage);
        platforms = new ArrayList<>();
        generatePlatforms();
        score = 0;
        hasLanded = false;
        isGameOver = false;

        if (timer == null) {

            timer = new Timer(20, this);
        }

        timer.start();
    }

    private void generatePlatforms() {

        platforms.clear();
        int y = 750;

        while (y > 0) {

            int x = rand.nextInt(510);
            platforms.add(new Platform(x, y, platformImage));
            y -= rand.nextInt(70) + 50;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (isGameOver) {

            return;
        }

        if (!hasLanded && player.getY() >= 725 && player.getVelocityY() > 0) {

            player.jump();
        }

        player.update();

        if (hasLanded && player.getY() < 300) {

            scrollWorld();
        }

        if (hasLanded && player.getY() > 800) {

            isGameOver = true;
            timer.stop();
        }

        checkPlatformCollision();
        repaint();
    }

    private void scrollWorld() {

        int dy = 300 - player.getY();
        player.setY(300);
        score += dy;

        for (int i = 0; i < platforms.size(); i++) {

            Platform p = platforms.get(i);
            p.setY(p.getY() + dy);

            if (p.getY() > 800) {

                platforms.remove(i);
                platforms.add(0, new Platform(rand.nextInt(510), 0, platformImage));
            }
        }
    }

    private void checkPlatformCollision() {

        int playerBottom = player.getY() + Player.HEIGHT;
        int playerLeft = player.getX();
        int playerRight = player.getX() + Player.WIDTH;

        for (Platform p : platforms) {

            int platTop = p.getY();
            int platLeft = p.getX();
            int platRight = p.getX() + Platform.WIDTH;

            if (player.getVelocityY() > 0 &&
                playerBottom >= platTop && playerBottom <= platTop + Platform.HEIGHT &&
                playerRight > platLeft && playerLeft < platRight) {

                player.jump();

                if (!hasLanded) {

                    hasLanded = true;
                }

                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (backgroundImage != null) {

            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } 
        else {

            g.setColor(Color.cyan);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        player.draw(g);
        for (Platform p : platforms) p.draw(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);

        if (isGameOver) {

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER", 170, 380);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Final Score: " + score, 220, 420);
            g.drawString("Press R to Restart", 200, 460);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (isGameOver && code == KeyEvent.VK_R) {

            startGame();
        }

        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {

            player.moveLeft();
        } 
        else if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {

            player.moveRight();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT ||
            code == KeyEvent.VK_A || code == KeyEvent.VK_D) {

            player.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    interface Movable {

        void moveLeft();
        void moveRight();
        void stop();
    }

    abstract class Game {

        protected int x, y;
        protected BufferedImage image;

        public Game(int x, int y, BufferedImage img) {

            this.x = x;
            this.y = y;
            this.image = img;
        }

        public abstract void draw(Graphics g);

        public int getX() { return x; }
        public int getY() { return y; }
        public void setY(int y) { this.y = y; }
    }

    class Player extends Game implements Movable {

        public static final int WIDTH = 75;
        public static final int HEIGHT = 75;
        private static final int GRAVITY = 1;
        private static final int JUMP_STRENGTH = -20;
        private static final int SPEED = 5;

        private int velocityY = 0;
        private int xVelocity = 0;

        public Player(int x, int y, BufferedImage img) {

            super(x, y, img);
        }

        public void update() {

            velocityY += GRAVITY;
            y += velocityY;
            x += xVelocity;

            if (x > 600) {

                x = -WIDTH;
            }
            else if (x + WIDTH < 0) {

                x = 600;
            }
        }

        public void jump() {

            velocityY = JUMP_STRENGTH;
        }

        public void draw(Graphics g) {

            if (image != null) {

                g.drawImage(image, x, y, WIDTH, HEIGHT, null);
            } 
            else {

                g.setColor(Color.GREEN);
                g.fillRect(x, y, WIDTH, HEIGHT);
            }
        }

        public int getVelocityY() { return velocityY; }

        @Override
        public void moveLeft() {

            xVelocity = -SPEED;
        }

        @Override
        public void moveRight() {

            xVelocity = SPEED;
        }

        @Override
        public void stop() {

            xVelocity = 0;
        }
    }

    class Platform extends Game {

        public static final int WIDTH = 90;
        public static final int HEIGHT = 15;

        public Platform(int x, int y, BufferedImage img) {

            super(x, y, img);
        }

        public void draw(Graphics g) {

            if (image != null) {

                g.drawImage(image, x, y, WIDTH, HEIGHT, null);
            } 
            else {

                g.setColor(Color.BLACK);
                g.fillRect(x, y, WIDTH, HEIGHT);
            }
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Doodle Jump");
        DoodleJump game = new DoodleJump();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
