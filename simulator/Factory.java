package simulator;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * This class is used to handle the creation of packages
 * that will be transported by the Transporter.
 */
public class Factory implements Runnable {

    private BlockingQueue<Package> packages;
    private List<EntrySquare> entries;

    /**
     * A list of the Block Squares representing the destination
     * to which the packages will be send.
     */
    private List<DestinationSquare> destinations;

    public Factory(List<EntrySquare> entr, List<DestinationSquare> dests, BlockingQueue<Package> pckgs) throws SimulatorException {
        if (entr == null || pckgs == null || dests == null)
            throw new SimulatorException("Parameters of class: " + getClass().getSimpleName() + " should not be null.");

        packages = pckgs;
        destinations = dests;
        entries = entr;
    }

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

                // Add the new package to the queue
                packages.add(new Package(entry.getCoordinates(), destination));
                entry.load();

                System.out.println("Package added.");
                System.out.println(packages);
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Represents a package that will be transported by the Transporter.
     */
    public class Package {

        private Coord source;
        private Coord destination;

        private Package(Coord src, Coord dst) {
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
            return "Package[" + source + ": " + destination + "]";
        }
    }
}
