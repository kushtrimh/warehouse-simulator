package simulator;

import java.awt.Image;

public class BlockSquare extends Square {
    /**
     * Block square can have 4 destination coordinates (usually 2).
     * Destination coordinates represent the path square coordinates from which
     * the 'baggage' will be put into the block square.
     */

    public BlockSquare(int xValue, int yValue, Coord coordinates) throws SimulatorException {
        super(xValue, yValue, coordinates);
    }

    public BlockSquare(int xValue, int yValue, Coord coordinates, Image img) throws SimulatorException {
        super(xValue, yValue, coordinates, img);
    }
}
