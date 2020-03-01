package simulator;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import graph.Graph;
import graph.GraphException;

import javax.imageio.ImageIO;

public class Warehouse {

    private Graph graph;
    private Transporter transporter;
    private Collector collector;
    private List<Square> squares;

    private List<EntrySquare> entries;
    private List<PathSquare> pathSquares;
    private List<BlockSquare> blockSquares;
    private List<DestinationSquare> destinationSquares;

    /**
     * An array representing the warehouse.
     * This array tells which squares will be blocks and which squares will be used
     * as path squares.
     * 0 - path square
     * 1 - block square
     */
    private int[][] warehouseArray;

    public Warehouse(String warehouseFilename) throws GraphException, SimulatorException {
        squares = new ArrayList<>();
        pathSquares = new ArrayList<>();
        blockSquares = new ArrayList<>();
        destinationSquares = new ArrayList<>();
        entries = new ArrayList<>();

        if (warehouseFilename == null)
            throw new SimulatorException("Warehouse filename cannot be null.");

        loadWarehouse(warehouseFilename);
        initSquares(warehouseArray);
        setDestinationSquares();
        setImagesToSquares();
        createGraph();
        // graph.print(); // Debugging only
        // printDestinationSquares(); // Debugging only
    }

    protected List<Square> getSquares() {
        return squares;
    }

    public List<PathSquare> getPathSquares() {
        return pathSquares;
    }

    public List<DestinationSquare> getDestinationSquares() {
        return destinationSquares;
    }

    public List<EntrySquare> getEntries() {
        return entries;
    }

    public Transporter getTransporter() {
        return transporter;
    }

    public Collector getCollector() {
        return collector;
    }

    /**
     * Returns the Square that has the coordinates given as the argument
     * @param coord
     * @return
     */
    protected Square getSquare(Coord coord) {
        for (Square s: squares) {
            if (s.getCoordinates().equals(coord)) {
                return s;
            }
        }
        return null;
    }

    protected PathSquare getPathSquare(Coord coord) {
        for (PathSquare s: pathSquares) {
            if (s.getCoordinates().equals(coord)) {
                return s;
            }
        }

        return null;
    }

    protected Graph getGraph() {
        return graph;
    }

    /**
     * Similar to {@code getSquare(Coord coord}
     * @param x
     * @param y
     * @return
     */
    private Square getSquare(int x, int y) {
        return getSquare(new Coord(x, y));
    }

    /**
     * Initializes a list of squares where the path squares
     * and block squares are created using an int array.
     */
    private void initSquares(int[][] arr) throws SimulatorException {
        // Start at 0 for both x and y value (top-left corner of the simulator).
        int xval = 0, yval = 0;
        // Same goes for the coordinates and the square number
        int xCoord = 0, yCoord = 0;
        int squareNumber = 0;

        for (int i = 0; i < Simulator.SQUARE_PER_LINE; i++) {
            for (int u = 0; u < Simulator.SQUARE_PER_LINE; u++) {

                boolean isBlock = arr[yCoord][xCoord] == 1;
                boolean isEntry = arr[yCoord][xCoord] == 2;
                boolean isDestination = arr[yCoord][xCoord] == 3;

                Square sqr;
                if (isBlock) {
                    sqr = new BlockSquare(xval, yval, new Coord(xCoord++, yCoord));
                    blockSquares.add((BlockSquare) sqr);
                } else if (isEntry) {
                    sqr = new EntrySquare(xval, yval, new Coord(xCoord++, yCoord), squareNumber++);
                    entries.add((EntrySquare) sqr);
                    pathSquares.add((EntrySquare) sqr);
                } else if (isDestination) {
                    sqr = new DestinationSquare(xval, yval, new Coord(xCoord++, yCoord));
                    destinationSquares.add((DestinationSquare) sqr);
                }
                else {
                    sqr = new RoadSquare(xval, yval, new Coord(xCoord++, yCoord), squareNumber++);
                    pathSquares.add((RoadSquare) sqr);
                }

                squares.add(sqr);

                // Increment the x value and the x coordinate
                xval += Simulator.SQUARE_SIZE;
            }
         xval = 0; yval += Simulator.SQUARE_SIZE;
         xCoord = 0; yCoord++;
        }
    }

    /**
     * Create the graph, and connect all the path squares.
     * @throws GraphException
     */
    private void createGraph() throws GraphException {
        // Create the graph
        graph = new Graph(pathSquares.size());

        // Add all the edges
        for (int y = 0; y < Simulator.SQUARE_PER_LINE; y++) {
            for (int x = 0; x < Simulator.SQUARE_PER_LINE; x++) {
                // Don't proceed to add edges if the current square is a block, destination or an entry
                if (warehouseArray[y][x] == 1 || warehouseArray[y][x] == 3)
                    continue;

                Square s = getSquare(x, y);

                // Check all the sides(N, E, S, W) of the current square if there are any path squares,
                // and if there are any add the found path squares with the current square.
                connectSquares(s, x-1, y); // Check West
                connectSquares(s, x+1, y); // Check East
                connectSquares(s, x, y-1); // Check North
                connectSquares(s, x, y+1); // Check South
            }
        }
    }

    /**
     * Connects the first square {@param s} with a second square if such square
     * exists at the given coordinates {@param x} and {@param y}
     * @param s - first square
     * @param x - x coordinate of the second square
     * @param y - y coordinate of the second square
     */
    private void connectSquares(Square s, int x, int y) {
        // Get the square in the x and y coordinates
        Square w = getSquare(x, y);

        // If it is a block or entry square, or there is no square at the given coordinates
        // simply return from the method
        if (w == null || w instanceof BlockSquare || w instanceof DestinationSquare) {
            return;
        }
        // If a square exists at the given coordinates connect the found square w,
        // with the given square s
        try {
            graph.addEdge((PathSquare) s, (PathSquare) w);
        } catch (GraphException ex) {}
    }

    /**
     * Sets the appropriate image for each square, depending if the square is a path square
     * or a block square.
     * @throws SimulatorException
     */
    private void setImagesToSquares() throws SimulatorException {
        // Load all the images
        BufferedImage blocksEdgeImg;
        BufferedImage blocksImg;
        BufferedImage floorImg;
        BufferedImage unloadedEntryImg;
        BufferedImage loadedEntryImg;
        BufferedImage destinationImage;

        try {
            blocksEdgeImg = ImageIO.read(Warehouse.class.getResource("/images/blocks-edge.png"));
            blocksImg = ImageIO.read(Warehouse.class.getResource("/images/blockss.png"));
            floorImg = ImageIO.read(Warehouse.class.getResource("/images/floor-gray.png"));
            unloadedEntryImg = ImageIO.read(Warehouse.class.getResource("/images/unloaded-entry.png"));
            loadedEntryImg = ImageIO.read(Warehouse.class.getResource("/images/loaded-entry.png"));
            destinationImage = ImageIO.read(Warehouse.class.getResource("/images/destination.png"));

        } catch (IOException ex) {
            throw new SimulatorException(ex.getMessage());
        }

        for (int y = 0; y < Simulator.SQUARE_PER_LINE; y++) {
            for (int x = 0; x < Simulator.SQUARE_PER_LINE; x++) {
                Square currentSquare = getSquare(x, y);

                // If the current square is a block add a block image
                // depending on its position
                if (currentSquare instanceof BlockSquare) {
                    boolean isEdgeBlock = false;
                    try {
                        isEdgeBlock = warehouseArray[y - 1][x] == 1 && warehouseArray[y + 1][x] == 0;
                    } catch (ArrayIndexOutOfBoundsException ex) {};

                    if (isEdgeBlock) {
                        currentSquare.setImage(blocksEdgeImg);
                    } else {
                        currentSquare.setImage(blocksImg);
                    }
                } else if (currentSquare instanceof EntrySquare) {
                    EntrySquare entry = (EntrySquare) currentSquare;
                    entry.setImages(loadedEntryImg, unloadedEntryImg);
                } else {
                    if (warehouseArray[y][x] == 3) {
                        currentSquare.setImage(destinationImage);
                    } else {
                        // Add floor image otherwise
                        currentSquare.setImage(floorImg);
                    }
                }
            }
        }
    }

    /**
     * Create the warehouse structure and lists the entries of the warehouse using
     * a text file as an input.
     */
    private void loadWarehouse(String flname) throws SimulatorException {
        String line = "";
        String tempLine;
        int pos = 0; // Tracks the position in the array 'splitted' array

        try (BufferedReader reader = new BufferedReader(new FileReader(flname))) {
            tempLine = reader.readLine();
            tempLine = tempLine.trim();
            line = tempLine;
            while(tempLine != null)
            {
                tempLine = reader.readLine();
                if(tempLine != null)
                line = line + " " + tempLine;
            }
        } catch (IOException ex) {
            throw new SimulatorException("There was a problem loading the warehouse file.");
        }
        line = line.trim();
        try {
            int squarePerLine = Simulator.SQUARE_PER_LINE;
            // Split the line from the file using spaces as delimiter
            String[] splitted = line.split(" ");
            // Initilaize the warehouse array
            warehouseArray = new int[squarePerLine][squarePerLine];

            // Fill the warehouse array with values from the warehouse file
            for (int y = 0; y < squarePerLine; y++) {
                for (int x = 0; x < squarePerLine; x++) {
                    warehouseArray[y][x] = Integer.parseInt(splitted[pos++]);
                }
            }

            // Get the transporter start coordinates
            transporter = new Transporter(
                    new Coord(Integer.parseInt(splitted[pos++]), Integer.parseInt(splitted[pos++]))
            );

            //Get the collector start coordinates
            collector = new Collector(
                    new Coord(Integer.parseInt(splitted[pos++]), Integer.parseInt(splitted[pos++]))
            );

        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new SimulatorException("Wrong format of 'warehouse' file name: " + flname +
                    "\n" + ex.getMessage());
        }

    }

    /**
     * Sets the destination square for each block square.
     */
    public void setDestinationSquares() {
        for (DestinationSquare block: destinationSquares) {
            Coord coord = block.getCoordinates();
            // Check for the left destination square
            Coord left = new Coord(coord.X() - 1, coord.Y());
            if (Coord.isValid(left)) {
                RoadSquare sqr = (RoadSquare) getSquare(left);
                block.addDestination(sqr);
            }

            // Check for the right destination square
            //Human will be on the left of grid - so no need for this       
            /*
            Coord right = new Coord(coord.X() + 1, coord.Y());
            if (Coord.isValid(right)) {
                RoadSquare sqr = (RoadSquare) getSquare(right);
                block.addDestination(sqr);
            }
            */
        }
    }


    /**
     * Debugging only
     */
    public void printDestinationSquares() {
        for (DestinationSquare sqr: destinationSquares) {
            StringBuilder sb = new StringBuilder();
            sb.append(sqr.getCoordinates()).append(": ");
            for (RoadSquare psqr: sqr.getDestinations()) {
                sb.append(psqr.getCoordinates()).append(" ");
            }
            System.out.println(sb);
        }
    }

    /**
     * TEMPORARY
     */
    /*
    private int[][] generateSquares() {
        return new int[][] {
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }
    */
}
