package simulator;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class BlockSquare extends Square {
    /**
     * Block square can have 4 destination coordinates (usually 2).
     * Destination coordinates represent the path square coordinates from which
     * the 'baggage' will be put into the block square.
     */
    private List<RoadSquare> destinations;

    public BlockSquare(int xValue, int yValue, Coord coordinates) throws SimulatorException {
        super(xValue, yValue, coordinates);
        destinations = new ArrayList<>();
    }

    public BlockSquare(int xValue, int yValue, Coord coordinates, Image img) throws SimulatorException {
        super(xValue, yValue, coordinates, img);
        destinations = new ArrayList<>();
    }

    public void addDestination(RoadSquare sqr) {
        destinations.add(sqr);
    }

    public List<RoadSquare> getDestinations() {
        return destinations;
    }
}
