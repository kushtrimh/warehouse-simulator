package simulator;

import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Simulator {
    public static final int SQUARE_SIZE = 40;
    public static final int WIDTH = 640;
    public static final int HEIGHT = 640;
    public static final int SQUARE_PER_LINE = WIDTH / SQUARE_SIZE;
    public static final int FPS = 1000 / 50;

    private JFrame frame;
    private JPanel mainPanel;

    private Warehouse whouse;

    private Coord destinationCoordinate;
    private Image destCoordImage;

    public Simulator(Warehouse w) throws SimulatorException {
        setFrameSettings();
        if (w == null) {
            throw new SimulatorException("Warehouse object should not be null");
        }
        whouse = w;
        try {
            destCoordImage = ImageIO.read(Simulator.class.getResource("/images/destination.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        mainPanel.repaint();

        Thread painter = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    repaint();

                    try {
                        Thread.sleep(FPS);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        painter.start();
    }

    /**
     * Creates the main simulation panel and sets the settings
     * for the JFrame.
     */
    private void setFrameSettings() {
        mainPanel = new SimulationPanel();

        // Settings for JFrame
        frame = new JFrame("Warehouse Simulator");
        frame.setContentPane(mainPanel);
        // frame.setSize(WIDTH, HEIGHT);
        frame.setSize(680, 680);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void repaint() {
        mainPanel.validate();
        mainPanel.repaint();
    }

    public void setDestinationCoord(Coord dst) {
        destinationCoordinate = dst;
    }

    /**
     * Instances of this class will be the content pane for
     * the simulator.
     */
    class SimulationPanel extends JPanel {

        /**
         * Clears the background, filling it with white color.
         */
        private void clearBackground(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        /**
         * Draws all the squares that are in the warehouse.
         */
        private void drawSquares(Graphics g) {
            for (Square s : whouse.getSquares()) {
                g.drawImage(s.getImage(), s.xValue(), s.yValue(), null);
            }
        }

        /**
         * Draws all the square numbers on the appropriate square.
         */
        private void drawSquareNumbers(Graphics g) {
            g.setColor(Color.WHITE);
            for (PathSquare s : whouse.getPathSquares()) {
                g.drawString(Integer.toString(s.getSquareNumber()), s.xValue() + 13, s.yValue() + 13);
            }
        }

        /**
         * Draws the coordinates of the each square.
         */
        private void drawCoordinates(Graphics g) {
            g.setColor(Color.WHITE);
            for (Square s: whouse.getSquares()) {
                Coord coord = s.getCoordinates();
                String coords = coord.X() + ":" + coord.Y();
                g.drawString(coords, s.xValue() + 5, s.yValue() + 27);
            }

        }

        /**
         * Draws the transporter at its current location.
         */
        private void drawTransporter(Graphics g) {
            Transporter t = whouse.getTransporter();

            g.drawImage(t.getImage(), t.xValue(), t.yValue(), null);
        }

        private void drawCollector(Graphics g) {
            Collector c = whouse.getCollector();

            g.drawImage(c.getImage(), c.xValue(), c.yValue(), null);
        }

        /**
         * Draws a green circle at the destination coordinate.
         */
        private void drawDestination(Graphics g) {
            if (destinationCoordinate != null) {
                int[] values = Coord.getCoordinatePixelValuesDestination(destinationCoordinate);
                g.drawImage(destCoordImage, values[0], values[1], null);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            clearBackground(g);

            // Draw
            drawSquares(g);
            //drawDestination(g);
            drawTransporter(g);
            drawCollector(g);
            // drawSquareNumbers(g); // Debugging only
            // drawCoordinates(g); // Debugging only
        }
    }
}
