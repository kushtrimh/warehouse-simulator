package simulator;

import graph.BreadthFirstPaths;

import java.util.concurrent.BlockingQueue;

public class DirectorCollector implements Runnable {

    private Warehouse whouse;
    private Simulator sim;

    private Transporter transporter;
    private Collector collector;

    private BlockingQueue<Payload> payloads;

    private Payload currentPayload;

    private int[][] warehousearray;

    public DirectorCollector(Warehouse whouse, Simulator sim, BlockingQueue<Payload> pylds) {
        this.whouse = whouse;
        this.sim = sim;
        transporter = whouse.getTransporter();
        collector = whouse.getCollector();
        payloads = pylds;

        // Make qito me 1 ven
        BreadthFirstPaths bfs = new BreadthFirstPaths(
                whouse.getGraph(),
                whouse.getPathSquare(collector.getCoordinates()));
                collector.convertPathToSteps(bfs.pathTo(whouse.getPathSquare(new Coord(0, 0))));
    }

    private Iterable<Collector.Step> getCollectorSteps(Coord dest) {
        BreadthFirstPaths bfs = new BreadthFirstPaths(
            whouse.getGraph(), whouse.getPathSquare(collector.getCoordinates())
        );
        return collector.convertPathToSteps(
            bfs.pathTo(whouse.getPathSquare(dest))
        );
    }

    private void sendCollectorTo(Coord dest)
    {
        warehousearray = whouse.getwarehousearray();
        Coord destination = dest;
        if (warehousearray[dest.Y()][dest.X()] == 2) {
            destination = new Coord(dest.X() + 1, dest.Y());
        }

        for (Collector.Step step: getCollectorSteps(destination)) {
            completeCollectorStep(step);
        }
    }


    private void sendCollectorToBase() {
        for (Collector.Step step: getCollectorSteps(collector.getBaseCoords())) {
            completeCollectorStep(step);

            if (!payloads.isEmpty()) {
                System.out.println("Switch directions, not to the base!! To the PAYLOAD!!"); // Debugging only
                break;
            }
        }
    }

    private void completeCollectorStep(Collector.Step step) {
        collector.setImage(step.getDirection());

        while (!step.completed()) {
            step.advance();

            switch (step.getDirection()) {
                case Collector.Step.DOWN:
                    collector.setYValue(collector.yValue() + 5);
                    break;
                case Collector.Step.UP:
                    collector.setYValue(collector.yValue() - 5);;
                    break;
                case Collector.Step.LEFT:
                    collector.setXValue(collector.xValue() - 5);;
                    break;
                case Collector.Step.RIGHT:
                    collector.setXValue(collector.xValue() + 5);;
                    break;
            }

            try {
                Thread.sleep(Simulator.FPS);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            // Update the coordinates of the collector
            collector.updateCoordinates(step.getSquare().getCoordinates());
        }
    }

    public void move() throws InterruptedException {
        while (true) {
            Thread.sleep(100);
            if((!collector.isloaded()) && (!payloads.isEmpty()))
            {
                currentPayload = payloads.take();
                Coord payloadsrc = currentPayload.getSource();
                sendCollectorTo(payloadsrc);
                // Unload the entry
                for (EntrySquare entry: whouse.getEntries()) {
                    if (entry.getCoordinates().equals(payloadsrc)) {
                        entry.unload();
                    }
                }
                Thread.sleep(500);
                sim.setDestinationCoord(currentPayload.getDestination());
                collector.load(currentPayload);
            }
        }
    }

    @Override
    public void run() {
        try {
            move();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
