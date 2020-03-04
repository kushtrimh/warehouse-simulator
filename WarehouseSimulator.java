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
            
            Factory factory = new Factory(w.getEntries(), w.getDestinationSquares(), payloads);
            factory.setupFactory();
            
            Thread transporterThread = new Thread(new DirectorTransporter(w, s, payloads));
            Thread collectorThread = new Thread(new DirectorCollector(w, s, payloads));

            collectorThread.start();
            transporterThread.start();
        } catch (SimulatorException | GraphException ex) {
            ex.printStackTrace();
        }
    }
}
