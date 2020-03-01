package simulator;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class DestinationSquare extends Square {

    private List<RoadSquare> destinations;

    public DestinationSquare(int xValue, int yValue, Coord coordinates) throws SimulatorException {
        super(xValue, yValue, coordinates);
        destinations = new ArrayList<>();
    }

    public DestinationSquare(int xValue, int yValue, Coord coordinates, Image img) throws SimulatorException {
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
