import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DoodleJump extends JPanel implements ActionListener {

    private Player player;
    private Timer timer;

    public DoodleJump() {
        
        setPreferredSize(new Dimension(400, 600));
        setBackground(Color.cyan);

        player = new Player(175, 550); 
        timer = new Timer(20, this); 
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        player.update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        player.draw(g);
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

    class Player {

        private int x, y;
        private int width = 50, height = 50;
        private int velocityY = 0;
        private int gravity = 1;

        public Player(int x, int y) {

            this.x = x;
            this.y = y;
        }

        public void update() {

            velocityY += gravity;
            y += velocityY;
        }

        public void draw(Graphics g) {

            g.setColor(Color.GREEN);
            g.fillRect(x, y, width, height);
        }
    }
}
