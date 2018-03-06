package simulator;

import java.awt.Image;

public abstract class Square extends SimElement {
    private int size;

    protected Image image;

    public Square(int xValue, int yValue, Coord coordinates) throws SimulatorException {
        super(xValue, yValue, coordinates);
        size = Simulator.SQUARE_SIZE;
    }

    public Square(int xValue, int yValue, Coord coordinates, Image img) throws SimulatorException {
        this(xValue, yValue, coordinates);
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

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("Square(").append("size=").append(size)
                .append(", x-value=").append(xVal).append(", y-value=")
                .append(yValue()).append(", coordinates=")
                .append(coord).append(")").toString();
    }

}
