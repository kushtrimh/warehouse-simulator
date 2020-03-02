package simulator;

import java.awt.Image;

/**
 * An Entry Square is a square from which payloads
 * will be taken by the Transporter.
 */
public class EntrySquare extends Square {

    /**
     * Two different images to be used, one where the square is loaded,
     * the other one when it is unloaded.
     */
    private Image loadedImg;
    private Image unloadedImg;

    /**
     * If 'loaded' is true then this entry square has a payload on it that is waiting
     * to be taken, if not there is not a payload yet on this entry.
     */
    private boolean loaded;

    public EntrySquare(int xValue, int yValue, Coord coordinates) throws SimulatorException {
        super(xValue, yValue, coordinates);
    }

    public void setImages(Image loadedImage, Image unloadedImage) throws SimulatorException {
        if (loadedImage == null || unloadedImage == null)
            throw new SimulatorException("Entry images cannot be null.");

        unloadedImg = unloadedImage;
        loadedImg = loadedImage;

        image = unloadedImg;
    }

    public void load() {
        loaded = true;
        image = loadedImg;
    }

    public void unload() {
        loaded = false;
        image = unloadedImg;
    }

    public boolean isLoaded() {
        return loaded;
    }


}
