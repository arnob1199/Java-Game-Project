import javax.swing.*;
import java.awt.*;

public class DoodleJump extends JPanel {

    public DoodleJump() {
        setPreferredSize(new Dimension(400, 600));
        setBackground(Color.cyan);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(175, 550, 50, 50);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Doodle Jump");
        DoodleJump game = new DoodleJump();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
