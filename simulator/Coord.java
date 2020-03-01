package simulator;

import java.util.Objects;

/**
 * Represents a coordinate of a DeprecatedSquare object in the simulator.
 */
public class Coord {
    private int x;
    private int y;

    /**
     * Checks if a coordinate object is in the valid range of coordinates.
     * @param coord - coordinate object to be checked
     * @return true if is valid, false otherwise
     */
    public static boolean isValid(Coord coord) {
        if (coord == null)
            return false;

        return coord.x >= 0 && coord.x < Simulator.SQUARE_PER_LINE &&
                coord.y >= 0 && coord.y < Simulator.SQUARE_PER_LINE;
    }

    /**
     * Returns the pixel values in the simulator for the given coordinate.
     * @param coord
     * @return an int array consisting the pixel values for x at (int[0]) and y at (int[1]),
     *         returns null if the given coordinate argument is null or not valid
     */
    public static int[] getCoordinatePixelValuesCollector(Coord coord) {
        if (!isValid(coord))
            return null;

        int xCoord = coord.X(), yCoord = coord.Y();
        int xVal = 0, yVal = 0; // Pixel values, starting at 0

        for (int y = 0; y < Simulator.SQUARE_PER_LINE; y++) {
            for (int x = 0; x < Simulator.SQUARE_PER_LINE; x++) {
                // If the coordinates match, return the x value and y value
                // for the coordinate
                if (x == xCoord && y == yCoord) {
                    return new int[] {xVal, yVal};
                }

                xVal += Simulator.SQUARE_SIZE; // Increment xVal
            }
            xVal = 0; // Reset the xValue for the next iteration
            yVal += Simulator.SQUARE_SIZE; // Increment yVal
        }

        return null;
    }

    public static int[] getCoordinatePixelValuesTransporter(Coord coord) {
        if (!isValid(coord))
            return null;

        int xCoord = coord.X(), yCoord = coord.Y();
        int xVal = 0, yVal = 0; // Pixel values, starting at 0

        for (int y = 0; y < Simulator.SQUARE_PER_LINE; y++) {
            for (int x = 0; x < Simulator.SQUARE_PER_LINE; x++) {
                // If the coordinates match, return the x value and y value
                // for the coordinate
                if (x == xCoord && y == yCoord) {
                    return new int[] {xVal, yVal};
                }

                xVal += Simulator.SQUARE_SIZE; // Increment xVal
            }
            xVal = 0; // Reset the xValue for the next iteration
            yVal += Simulator.SQUARE_SIZE; // Increment yVal
        }

        return null;
    }

    public static int[] getCoordinatePixelValuesDestination(Coord coord) {
        if (!isValid(coord))
            return null;

        int xCoord = coord.X() + 1, yCoord = coord.Y();
        int xVal = 0, yVal = 0; // Pixel values, starting at 0

        for (int y = 0; y < Simulator.SQUARE_PER_LINE; y++) {
            for (int x = 0; x < Simulator.SQUARE_PER_LINE; x++) {
                // If the coordinates match, return the x value and y value
                // for the coordinate
                if (x == xCoord && y == yCoord) {
                    return new int[] {xVal, yVal};
                }

                xVal += Simulator.SQUARE_SIZE; // Increment xVal
            }
            xVal = 0; // Reset the xValue for the next iteration
            yVal += Simulator.SQUARE_SIZE; // Increment yVal
        }

        return null;
    }


    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }

    @Override
    public String toString() {
        return "Coord(" + x + ", " + y + ")";
    }

    @Override
    public int hashCode() {
        return new Integer(x+y).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coord) {
            Coord c = (Coord) obj;
            return Objects.equals(x, c.x) && Objects.equals(y, c.y);
        }
        return false;
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }
}
