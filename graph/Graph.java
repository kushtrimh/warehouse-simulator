package graph;

import simulator.PathSquare;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private int vertices;
    private int edges;

    /**
     * Adjacency list, representing all the squares that are connected with each other.
     */
    List<List<PathSquare>> adj;

    public Graph(int v) throws GraphException {
        if (v < 1) {
            throw new GraphException("Vertices number should be greater than 0");
        }
        vertices = v;

        adj = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void validateVertex(PathSquare v) throws GraphException {
        if (v == null) {
            throw new GraphException("PathSquare argument cannot be NULL");
        }
        int sqrNum = v.getSquareNumber();
        if (sqrNum < 0 || sqrNum > vertices) {
            throw new GraphException("PathSquare number should be between 0 and " + vertices);
        }
    }

    public int V() {
        return vertices;
    }

    public int E() {
        return edges;
    }

    /**
     * Adds an 'edge' between 2 squares, thus connecting them.
     * @param v
     * @param w
     * @throws GraphException
     */
    public void addEdge(PathSquare v, PathSquare w) throws GraphException {
        validateVertex(v);
        validateVertex(w);

        if (edgeExists(v, w))
                return;
        adj.get(v.getSquareNumber()).add(w);
        adj.get(w.getSquareNumber()).add(v);
        edges++;
    }

    public Iterable<PathSquare> adj(int vNum) {
        return adj.get(vNum);
    }

    /**
     * Checks if 2 PathSquare are already connected with each other.
     * @param v
     * @param w
     * @return
     */
    private boolean edgeExists(PathSquare v, PathSquare w) {
        return adj.get(v.getSquareNumber()).indexOf(w) != -1;
    }

    /**
     * Prints all the square and a list of the squares that they're connected to.
     */
    public void print() {
        System.out.println("====================");
        System.out.println(this);
        System.out.println("Vertices: " + vertices);
        System.out.println("Edges: " + edges );
        System.out.println("====================");


        for (int i = 0; i < adj.size(); i++) {
            StringBuilder sb = new StringBuilder();

            sb.append(i).append(": [");
            for (PathSquare s: adj.get(i)) {
                sb.append(s.getSquareNumber()).append(", ");
            }

            int lastCommaIdx = sb.lastIndexOf(", ");
            sb.deleteCharAt(lastCommaIdx).replace(lastCommaIdx, lastCommaIdx+1, "");
            sb.append("]");
            System.out.println(sb);
        }
        System.out.println("====================");
    }
}
