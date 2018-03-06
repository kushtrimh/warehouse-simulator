package simulator;

/**
 * Used to represent an element in the simulator.
 */
public abstract class SimElement {

    /**
     * X and Y values of this block in the simulator (value in pixels)
     */
    protected int xVal;
    protected int yVal;

    /**
     * Coordinates of this sim element.
     */
    protected Coord coord;

    public SimElement(int xVal, int yVal, Coord coord) throws SimulatorException {
        if (xVal < 0 || yVal < 0)
            throw new SimulatorException("XValue and YValue should be positive.");

        if (coord == null || !Coord.isValid(coord))
            throw new SimulatorException("Coordinate values should be valid.");

        this.coord = coord;
        this.xVal = xVal;
        this.yVal = yVal;
    }

    public int xValue() { return xVal; }

    public int yValue() { return yVal; }

    public Coord getCoordinates() { return coord; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Square) {
            Square s = (Square) obj;
            return coord.equals(s.coord);
        }
        return false;
    }
}
