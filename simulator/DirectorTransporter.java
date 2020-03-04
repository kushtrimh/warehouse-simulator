package simulator;

import graph.BreadthFirstPaths;

import java.util.concurrent.BlockingQueue;

public class DirectorTransporter implements Runnable {

    private Warehouse whouse;
    private Simulator sim;

    private Transporter transporter;
    private Collector collector;

    private BlockingQueue<Payload> payloads;

    private Payload currentPayload;

    public DirectorTransporter(Warehouse whouse, Simulator sim, BlockingQueue<Payload> pylds) {
        this.whouse = whouse;
        this.sim = sim;
        transporter = whouse.getTransporter();
        collector = whouse.getCollector();
        payloads = pylds;

        // Make qito me 1 ven
        BreadthFirstPaths bfs = new BreadthFirstPaths(
                whouse.getGraph(),
                whouse.getPathSquare(transporter.getCoordinates()));
        transporter.convertPathToSteps(bfs.pathTo(whouse.getPathSquare(new Coord(0, 0))));
    }

    private Iterable<Transporter.Step> getTransporterSteps(Coord dest) {
        BreadthFirstPaths bfs = new BreadthFirstPaths(
                whouse.getGraph(),
                whouse.getPathSquare(transporter.getCoordinates())
        );
       return transporter.convertPathToSteps(
                bfs.pathTo(whouse.getPathSquare(dest))
        );
    }

    private void sendTransporterTo(Coord dest) {

        for (Transporter.Step step: getTransporterSteps(dest)) {
            completeTransporterStep(step);
        }
    }

    private void sendTransporterToBase() {
        for (Transporter.Step step: getTransporterSteps(transporter.getBaseCoords())) {
            completeTransporterStep(step);

            if (!payloads.isEmpty()) {
                System.out.println("Switch directions, not to the base!! To the PAYLOAD!!"); // Debugging only
                break;
            }
        }
    }


    private void completeTransporterStep(Transporter.Step step) {
        transporter.setImage(step.getDirection());

        while (!step.completed()) {
            step.advance();

            switch (step.getDirection()) {
                case Transporter.Step.DOWN:
                    transporter.setYValue(transporter.yValue() + 5);
                    break;
                case Transporter.Step.UP:
                    transporter.setYValue(transporter.yValue() - 5);;
                    break;
                case Transporter.Step.LEFT:
                    transporter.setXValue(transporter.xValue() - 5);;
                    break;
                case Transporter.Step.RIGHT:
                    transporter.setXValue(transporter.xValue() + 5);;
                    break;
            }

            try {
                Thread.sleep(Simulator.FPS);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            // Update the coordinates of the transporter
            transporter.updateCoordinates(step.getSquare().getCoordinates());
        }
    }

    public void move() throws InterruptedException {
        while (true) {
            Thread.sleep(100);
            if((collector.isloaded()) && (!transporter.isloaded()))
            {
                currentPayload = collector.getPayload();
                int collectY = 0, collectX = 0;
                Coord collectorPosition = collector.getCoordinates();
                
                collectX = collectorPosition.X() + 1;
                collectY = collectorPosition.Y();
                
                sendTransporterTo(new Coord(collectX, collectY));
                Thread.sleep(500);
                sim.setDestinationCoord(currentPayload.getDestination());
                collector.unload();
                transporter.load(currentPayload);
            }
            else if((transporter.isloaded())) {
                currentPayload = transporter.getPayload();
                Coord payloadDest = currentPayload.getDestination();
                sendTransporterTo(payloadDest);

                Thread.sleep(500);

                currentPayload = null;
                sim.setDestinationCoord(null);
                transporter.unload();
            }
            else {
                sendTransporterToBase();
            }
            /*
            if (currentPayload == null && !payloads.isEmpty()) {
                currentPayload = payloads.take();
                Coord payloadsrc = currentPayload.getSource();
                sendTransporterTo(payloadsrc);
                // Unload the entry
                for (EntrySquare entry: whouse.getEntries()) {
                    if (entry.getCoordinates().equals(payloadsrc)) {
                        entry.unload();
                    }
                }

                Thread.sleep(500);
                sim.setDestinationCoord(currentPayload.getDestination());

            } else if (currentPayload != null){
                Coord payloadDest = currentPayload.getDestination();
                sendTransporterTo(payloadDest);

                Thread.sleep(500);

                currentPayload = null;
                sim.setDestinationCoord(null);

            } else {
                System.out.println("Go to base");
                sendTransporterToBase();
            }
            */
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
