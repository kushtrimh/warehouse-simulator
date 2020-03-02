import graph.GraphException;
import simulator.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WarehouseSimulator {
    public static void main(String[] args) {
        try {
            Warehouse w = new Warehouse(Warehouse.class.getResource("/warehouses/warehouse").getFile());
            Simulator s = new Simulator(w);

            BlockingQueue<Payload> payloads = new ArrayBlockingQueue<>(w.getEntries().size());
            Thread factoryThread = new Thread(new Factory(w.getEntries(), w.getDestinationSquares(), payloads));
            Thread transporterThread = new Thread(new DirectorTransporter(w, s, payloads));
            Thread collectorThread = new Thread(new DirectorCollector(w, s, payloads));

            factoryThread.start();
            collectorThread.start();
            transporterThread.start();
        } catch (SimulatorException | GraphException ex) {
            ex.printStackTrace();
        }

    }
}
