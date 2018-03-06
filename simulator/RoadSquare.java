package simulator;

import java.awt.Image;

public class RoadSquare extends PathSquare {

    public RoadSquare(int xValue, int yValue, Coord coordinates, int sqrNum) throws SimulatorException {
        super(xValue, yValue, coordinates, sqrNum);
    }

    public RoadSquare(int xValue, int yValue, Coord coordinates, Image img, int sqrNum) throws SimulatorException {
        super(xValue, yValue, coordinates, img, sqrNum);
    }
}
