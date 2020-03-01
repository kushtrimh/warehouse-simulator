package simulator;

import graph.BreadthFirstPaths;

import java.util.concurrent.BlockingQueue;

public class Director implements Runnable {

    private Warehouse whouse;
    private Simulator sim;

    private Transporter transporter;
    private Collector collector;

    private BlockingQueue<Factory.Package> packages;

    private Factory.Package currentPackage;

    public Director(Warehouse whouse, Simulator sim, BlockingQueue<Factory.Package> pckgs) {
        this.whouse = whouse;
        this.sim = sim;
        transporter = whouse.getTransporter();
        collector = whouse.getCollector();
        packages = pckgs;

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

    private Iterable<Collector.Step> getCollectorSteps(Coord dest) {
        BreadthFirstPaths bfs = new BreadthFirstPaths(
            whouse.getGraph(), whouse.getPathSquare(collector.getCoordinates())
        );
        return collector.convertPathToSteps(
            bfs.pathTo(whouse.getPathSquare(dest))
        );
    }

    private void sendTransporterTo(Coord dest) {

        for (Transporter.Step step: getTransporterSteps(dest)) {
            completeTransporterStep(step);
        }
    }

    private void sendCollectorTo(Coord dest) {

        for (Collector.Step step: getCollectorSteps(dest)) {
            completeCollectorStep(step);
        }
    }

    private void sendTransporterToBase() {
        for (Transporter.Step step: getTransporterSteps(transporter.getBaseCoords())) {
            completeTransporterStep(step);

            if (!packages.isEmpty()) {
                System.out.println("Switch directions, not to the base!! To the PACKAGE!!"); // Debugging only
                break;
            }
        }
    }

    private void sendCollectorToBase() {
        for (Collector.Step step: getCollectorSteps(collector.getBaseCoords())) {
            completeCollectorStep(step);

            if (!packages.isEmpty()) {
                System.out.println("Switch directions, not to the base!! To the PACKAGE!!"); // Debugging only
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

            if (currentPackage == null && !packages.isEmpty()) {
                currentPackage = packages.take();
                Coord packageSrc = currentPackage.getSource();
                sendTransporterTo(packageSrc);
                // Unload the entry
                for (EntrySquare entry: whouse.getEntries()) {
                    if (entry.getCoordinates().equals(packageSrc)) {
                        entry.unload();
                    }
                }

                Thread.sleep(300);
                sim.setDestinationCoord(currentPackage.getDestination());

            } else if (currentPackage != null){
                Coord packageDest = currentPackage.getDestination();
                sendTransporterTo(packageDest);

                Thread.sleep(300);

                currentPackage = null;
                sim.setDestinationCoord(null);

            } else {
                System.out.println("Go to base");
                sendTransporterToBase();
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
