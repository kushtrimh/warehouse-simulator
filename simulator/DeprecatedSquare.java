package simulator;

import java.awt.Image;
import java.awt.Color;

/**
 * OLD IMPLEMENTATION: NOT USED ANYMORE.
 */
public class DeprecatedSquare {

    /**
     * All squares in the simulator have a number, numbers start from 0 at the top-left corner
     * of the simulator.
     */
    private int squareNum;
    private int size;
    /**
     * If 'block' is true, then this square is considered as a block.
     * A block square is a square that you can't move through and can be used as a destination point.
     */
    private boolean block;

    /**
     * If true this square is considered as an entry. An entry serves as a square from which the baggage
     * that will be transported is taken from. Entries are path squares, so moving through them is allowed.
     * Only path squares can be entries.
     */
    private boolean entry;

    /**
     * X and Y values of this block in the simulator (value in pixels)
     */
    private int xVal;
    private int yVal;

    private Coord coord;
    private Image image;
    private Color color; // Debugging only

    public DeprecatedSquare(int sqrNum, int xValue, int yValue, Coord coordinates, boolean blck) {
        size = Simulator.SQUARE_SIZE;
        squareNum = sqrNum;
        block = blck;
        xVal = xValue;
        yVal = yValue;
        coord = coordinates;
        color = block ? Color.BLACK : Color.WHITE;
    }

    public DeprecatedSquare(int sqrNum, int xValue, int yValue, Coord coordinates, boolean blck, Image img) {
        this(sqrNum, xValue, yValue, coordinates, blck);
        image = img;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        if (img == null) {
            throw new IllegalArgumentException("Image argument can't be null");
        }

        image = img;
    }

    public void setEntry(boolean entr) throws SimulatorException {
        if (block)
            throw new SimulatorException("A square block cannot be used as an entry");

        entry = entr;
    }

    public int getSize() {
        return size;
    }

    public int getSquareNumber() { return squareNum;}

    public boolean isBlock() {
        return block;
    }

    public boolean isEntry() { return entry; }

    public int xValue() {
        return xVal;
    }

    public int yValue() {
        return yVal;
    }

    public Coord getCoordinates() {
        return coord;
    }

    /**
     * Debugging only
     */
    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("Shape(").append("squareNumber=").append(squareNum)
                .append(", size=").append(size).append(", x-value=").append(xVal)
                .append(", y-value=").append(yVal).append(", coordinates=")
                .append(coord).append(", block=").append(block)
                .append(")").toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DeprecatedSquare) {
            DeprecatedSquare s = (DeprecatedSquare) obj;
            return squareNum == s.squareNum;
        }
        return false;
    }
}
