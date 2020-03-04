package simulator;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * This class is used to handle the creation of payloads
 * that will be transported by the Transporter.
 */
public class Factory {

    private BlockingQueue<Payload> payloads;
    private List<EntrySquare> entries;

    /**
     * A list of the Block Squares representing the destination
     * to which the payloads will be send.
     */
    private List<DestinationSquare> destinations;

    public Factory(List<EntrySquare> entr, List<DestinationSquare> dests, BlockingQueue<Payload> pylds) throws SimulatorException {
        if (entr == null || pylds == null || dests == null)
            throw new SimulatorException("Parameters of class: " + getClass().getSimpleName() + " should not be null.");

        payloads = pylds;
        destinations = dests;
        entries = entr;
    }

    public void setupFactory() {
        Random r = new Random();
        EntrySquare entry;
        List<RoadSquare> dstCoords;
        Coord destination;
        for(int i = 0; i < entries.size(); i++) {
            entry = entries.get(i);
            // Get the destination coordinates for the selected BlockSquare
            dstCoords = destinations.get(r.nextInt(destinations.size())).getDestinations();
            // Select one of the destination coordinates randomly
            destination = dstCoords.get(r.nextInt(dstCoords.size())).getCoordinates();
            // Add the new payload to the queue
            payloads.add(new Payload(entry.getCoordinates(), destination));
            entry.load();
        }
    }

    /*
    @Override
    public void run() {
        while (true) {
            Random r = new Random();

            // Get a random entry from the entries list
            EntrySquare entry = entries.get(r.nextInt(entries.size()));

            // If that entry is loaded, skip the process
            // if not, load that entry
            if (!entry.isLoaded()) {
                // Get the destination coordinates for the selected BlockSquare
                List<RoadSquare> dstCoords = destinations.get(r.nextInt(destinations.size())).getDestinations();
                // Select one of the destination coordinates randomly
                Coord destination = dstCoords.get(r.nextInt(dstCoords.size())).getCoordinates();

                // Add the new payload to the queue
                payloads.add(new Payload(entry.getCoordinates(), destination));
                entry.load();
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    */
}
