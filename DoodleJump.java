import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DoodleJump extends JPanel implements ActionListener, KeyListener {

    private Player player;
    private Timer timer;

    public DoodleJump() {

        setPreferredSize(new Dimension(400, 600));
        setBackground(Color.cyan);
        setFocusable(true);
        addKeyListener(this);

        player = new Player(175, 550);

        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        player.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        player.update();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

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

    class Player {

        private int x, y;
        private int width = 50, height = 50;
        private int velocityY = 0;
        private int gravity = 1;
        private int xVelocity = 0;
        private int speed = 5;

        public Player(int x, int y) {

            this.x = x;
            this.y = y;
        }

        public void update() {

            velocityY += gravity;
            y += velocityY;

            x += xVelocity;

            if (x > 400) 
            {
                x = -width;
            }

            if (x + width < 0) 
            {
                x = 400;
            }
        }

        public void moveLeft() {

            xVelocity = -speed;
        }

        public void moveRight() {

            xVelocity = speed;
        }

        public void stop() {

            xVelocity = 0;
        }

        public void draw(Graphics g) {

            g.setColor(Color.GREEN);
            g.fillRect(x, y, width, height);
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Doodle Jump");
        DoodleJump panel = new DoodleJump();

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
