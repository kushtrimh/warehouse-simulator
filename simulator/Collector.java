package simulator;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Collector extends SimElement {

    private Coord baseCoords;

    /**
     * The yImg will be used when the Collector is moving in the y direction in the simulator,
     * and the xImg will be used when it is moving in the x direction.
     */
    private Image yImg;
    private Image xImg;

    private Image yImgLoaded;
    private Image xImgLoaded;

    private Image img;
    private boolean loaded;
    private Payload payload;
    private int heading;


    public Collector(Coord startCoordinates) throws SimulatorException {
        super(0, 0, startCoordinates);
        baseCoords = startCoordinates;
        updatePixelValues();
        setImages();
    }

    public boolean isloaded() { return loaded; }

    public void load(Payload pkg) {
        payload = pkg;
        loaded = true;
        setImage(heading);
    }

    public void unload() {
        loaded = false;
        payload = null;
    }

    public Payload getPayload() {
        return payload;
    }

    public Image getImage() { return img; }

    public Coord getCoordinates() {
        return coord;
    }

    public int xValue() {
        return xVal;
    }

    public int yValue() {
        return yVal;
    }

    /**
     * Updates the coordinates of the Collector.
     * @param newCoord - Coord object holding the new coordinates
     */
    public void updateCoordinates(Coord newCoord) {
        if (Coord.isValid(newCoord)) {
            coord = newCoord;
        }
    }

    public Coord getBaseCoords() {
        return baseCoords;
    }

    /**
     * Updates the coordinates of the Collector.
     * @param x - new x coordinate
     * @param y - new y coordinate
     */
    public void updateCoordinates(int x, int y) {
        updateCoordinates(new Coord(x, y));
    }

    /**
     * Updates the x and y values of the Collector based
     * on its current coordinates.
     */
    private void updatePixelValues() {
        int[] pixelValues = Coord.getCoordinatePixelValues(coord);
        xVal = pixelValues[0];
        yVal = pixelValues[1];
    }


    private void setImages() throws SimulatorException {
        try {
            xImg = ImageIO.read(Collector.class.getResource("/images/collector-left-right.png"));
            xImgLoaded = ImageIO.read(Transporter.class.getResource("/images/collector-left-right-loaded.png"));
            yImg = ImageIO.read(Collector.class.getResource("/images/collector-up-down.png"));
            yImgLoaded = ImageIO.read(Transporter.class.getResource("/images/collector-up-down-loaded.png"));
        } catch (IOException ex) {
            throw new SimulatorException(ex.getMessage());
        }
    }

    public void setImage(int direction) {
        if(loaded) {
            img = ((direction == Transporter.Step.DOWN) || (direction == Transporter.Step.UP)) ? yImgLoaded : xImgLoaded;
        } else {
            img = ((direction == Transporter.Step.DOWN) || (direction == Transporter.Step.UP)) ? yImg : xImg;
        }
        heading = direction;
    }

    public void setXValue(int xvalue) {
        xVal = xvalue;
    }

    public void setYValue(int yvalue) {
        yVal = yvalue;
    }

    /**
     * Converts the path that the Collector needs to take to go to its destination
     * into a list of steps it should take to arrive at that destination.
     * @param path path to destination
     * @return list of steps
     */
    public Iterable<Step> convertPathToSteps(Iterable<PathSquare> path) {
        List<Step> steps = new ArrayList<>();
        Coord currentCoord = getCoordinates();

        for (PathSquare sqr: path) {
            Coord nextCoord = sqr.getCoordinates();

            int dir;
            if (nextCoord.X() > currentCoord.X()) {
                dir = Step.RIGHT;
            } else if (nextCoord.X() < currentCoord.X()) {
                dir = Step.LEFT;
            } else if (nextCoord.Y() > currentCoord.Y()) {
                dir = Step.DOWN;
            } else {
                dir = Step.UP;
            }

            currentCoord = nextCoord;
            steps.add(new Step(sqr, dir));
        }

        return steps;
    }

    /**
     * Step objects represent a 'step' the Collector needs to make to move
     * from one square to another.
     */

    public class Step {

        /**
         * Two directions in which the Collector can move.
         */
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int UP = 2;
        public static final int DOWN = 3;

        private PathSquare square;

        /**
         * The direction the Collector will follow in this step.
         */
        private int direction;

        /**
         * Shows how much of this step is finished.
         * When progress reaches the value of square size (40) then the step
         * is considered as completed.
         */
        private int progress;

        private Step(PathSquare sqr, int dir) {
            square = sqr;
            direction = dir;
        }

        public boolean completed() {
            return progress == Simulator.SQUARE_SIZE;
        }

        public void advance() {
            progress += 5;
        }

        public int getDirection() {
            return direction;
        }

        public PathSquare getSquare() {
            return square;
        }

    }
}
