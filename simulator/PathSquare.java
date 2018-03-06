package simulator;

import java.awt.*;

public class PathSquare extends Square {

    /**
     * All path squares in the simulator have a number, numbers start from 0 at the top-left corner
     * of the simulator.
     */
    private int squareNum;

    public PathSquare(int xValue, int yValue, Coord coordinates, int sqrNum) throws SimulatorException {
        super(xValue, yValue, coordinates);
        squareNum = sqrNum;
    }

    public PathSquare(int xValue, int yValue, Coord coordinates, Image img, int sqrNum) throws SimulatorException {
        super(xValue, yValue, coordinates, img);
        squareNum = sqrNum;
    }

    public int getSquareNumber() {
        return squareNum;
    }

}
