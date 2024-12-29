import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Circles extends JPanel implements KeyListener, ActionListener {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int CIRCLE_RADIUS = 30;
    private static final int SQUARE_SIZE = 30;

    private int circleX = 100, circleY = 100; // Initial position of the circle
    private boolean[] squareVisible = {true, true, true}; // Keeps track of square visibility
    private int[] squareX = new int[3], squareY = new int[3]; // Square positions
    private Color[] squareColors = {Color.RED, Color.GREEN, Color.BLUE}; // Colors of squares

    private Timer timer;

    public Circles() {
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(Color.WHITE);
        this.addKeyListener(this);
        this.setFocusable(true);

        // Generate initial random positions for the squares
        generateSquares();

        // Set up a timer for continuous redrawing of the screen
        timer = new Timer(16, this); // 60 FPS
        timer.start();
    }

    // Generate random positions for the squares
    private void generateSquares() {
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            squareX[i] = rand.nextInt(WINDOW_WIDTH - SQUARE_SIZE);
            squareY[i] = rand.nextInt(WINDOW_HEIGHT - SQUARE_SIZE);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the circle
        g.setColor(Color.BLACK);
        g.fillOval(circleX - CIRCLE_RADIUS, circleY - CIRCLE_RADIUS, CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);

        // Draw the squares if they are visible
        for (int i = 0; i < 3; i++) {
            if (squareVisible[i]) {
                g.setColor(squareColors[i]);
                g.fillRect(squareX[i], squareY[i], SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Move the circle based on arrow keys
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                if (circleX - CIRCLE_RADIUS > 0) circleX -= 10;
                break;
            case KeyEvent.VK_RIGHT:
                if (circleX + CIRCLE_RADIUS < WINDOW_WIDTH) circleX += 10;
                break;
            case KeyEvent.VK_UP:
                if (circleY - CIRCLE_RADIUS > 0) circleY -= 10;
                break;
            case KeyEvent.VK_DOWN:
                if (circleY + CIRCLE_RADIUS < WINDOW_HEIGHT) circleY += 10;
                break;
        }

        // Check for collisions with the squares
        checkCollisions();
    }

    // Check if the circle touches any square
    private void checkCollisions() {
        for (int i = 0; i < 3; i++) {
            if (squareVisible[i]) {
                int squareCenterX = squareX[i] + SQUARE_SIZE / 2;
                int squareCenterY = squareY[i] + SQUARE_SIZE / 2;

                // Calculate distance between circle center and square center
                double distance = Math.sqrt(Math.pow(circleX - squareCenterX, 2) + Math.pow(circleY - squareCenterY, 2));

                // If the distance is less than the sum of the radius and half of the square's size, they collide
                if (distance < CIRCLE_RADIUS + SQUARE_SIZE / 2) {
                    squareVisible[i] = false; // Make the square disappear
                }
            }
        }

        // If all squares are gone, regenerate them
        if (!squareVisible[0] && !squareVisible[1] && !squareVisible[2]) {
            generateSquares();
            for (int i = 0; i < 3; i++) {
                squareVisible[i] = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Circles");
        Circles circlesPanel = new Circles();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(circlesPanel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}
