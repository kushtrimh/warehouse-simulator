package graph;

import simulator.PathSquare;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Collections;

/**
 * Searches through the graph using Breadth First Search algorithms, finding
 * all the paths to each square.
 */
public class BreadthFirstPaths {

    private boolean marked[];
    private PathSquare edgeTo[];

    /**
     * PathSquare from which we start (source square)
     */
    private PathSquare src;

    public BreadthFirstPaths(Graph g, PathSquare source) {
        marked = new boolean[g.V()];
        edgeTo = new PathSquare[g.V()];
        src = source;

        bfs(g, src);
    }

    public void bfs(Graph g, PathSquare s) {
        Queue<PathSquare> queue = new LinkedList<>();
        queue.add(s);
        marked[s.getSquareNumber()] = true;

        while (!queue.isEmpty()) {
            PathSquare v = queue.remove();
            for (PathSquare sqr: g.adj(v.getSquareNumber())) {
                int sqrNum = sqr.getSquareNumber();
                if (!marked[sqrNum]) {
                    marked[sqrNum] = true;
                    edgeTo[sqrNum] = v;
                    queue.add(sqr);
                }
            }
        }
    }

    /**
     * Checks if there is a path to the PathSquare({@param destSquare})
     * @param destSquare
     * @return true if there is such path, false otherwise
     */
    public boolean hasPathTo(PathSquare destSquare) {
        return marked[destSquare.getSquareNumber()];
    }

    /**
     * Returns the path that needs to be followed to get from {@code src} square to the
     * destination({@param dst}) square number
     * @param dst
     * @return
     */
    public Iterable<PathSquare> pathTo(PathSquare dst) {
        // Create a list to hold the path to the destination square
        List<PathSquare> path = new ArrayList<>();
        for (PathSquare v = dst; !v.equals(src); v = edgeTo[v.getSquareNumber()]) {
            path.add(v);
        }
        // Reverse the list to get the path in the correct order.
        Collections.reverse(path);
        return path;
    }
}
