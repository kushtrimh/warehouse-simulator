import graph.GraphException;
import simulator.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WarehouseSimulator {
    public static void main(String[] args) {
        try {
            Warehouse w = new Warehouse(Warehouse.class.getResource("/warehouses/warehouse").getFile());
            Simulator s = new Simulator(w);

            BlockingQueue<Factory.Package> packages = new ArrayBlockingQueue<>(w.getEntries().size());
            Thread factoryThread = new Thread(new Factory(w.getEntries(), w.getDestinationSquares(), packages));
            Thread directorThread = new Thread(new Director(w, s, packages));

            factoryThread.start();
            directorThread.start();
        } catch (SimulatorException | GraphException ex) {
            ex.printStackTrace();
        }

    }
}
