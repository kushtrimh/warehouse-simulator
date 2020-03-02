package simulator;

/**
 * Represents a payload that will be transported by the Transporter.
 */
public class Payload {

    private Coord source;
    private Coord destination;

    public Payload(Coord src, Coord dst) {
        source = src;
        destination = dst;
    }

    public Coord getSource() {
        return source;
    }

    public Coord getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "Payload[" + source + ": " + destination + "]";
    }
}