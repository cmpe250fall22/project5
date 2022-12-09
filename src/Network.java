import java.io.*;
import java.util.*;

public class Network {
    int maxFlow = 0;
    int numVertex;
    int[] levels;
    Vector<Vector<Edge>> adjacencyList;

    // holds the number of visited edges of each vertex
    int[] numVisitedEdges;

    public Network(int numVertex) {
        this.numVertex = numVertex;
        this.levels = new int[numVertex];
        this.adjacencyList = new Vector<>();
        for (int i = 0; i < numVertex; i++) {
            adjacencyList.add(new Vector<>());
        }
        this.numVisitedEdges = new int[numVertex];
        Arrays.fill(numVisitedEdges, 0);
    }

    public void addEdge(int from, int to, int capacity) {
        Edge edge1 = new Edge(to, 0, capacity);
        adjacencyList.get(from).add(edge1);

        // reverse edge
        Edge edge2 = new Edge(from, 0, 0);
        adjacencyList.get(to).add(edge2);

        edge1.residual = edge2;
        edge2.residual = edge2;
    }

    private boolean bfs(int sink) {

        Arrays.fill(levels, -1);

        Queue<Integer> bfsQueue = new LinkedList<>();
        bfsQueue.add(0);
        levels[0] = 0;

        // build the level graph
        while (!bfsQueue.isEmpty()) {
            int from = bfsQueue.poll();
            for (Edge e : adjacencyList.get(from)) {

                // levels[e.vertexTo] < 0 -> not visited yet
                if (e.flow < e.capacity && levels[e.vertexTo] < 0) {
                    bfsQueue.add(e.vertexTo);
                    levels[e.vertexTo] = levels[from] + 1;
                }
            }
        }
        return (levels[sink] >= 0);
    }

    private int dfs(int from, int sink, int flow) {
        if (from == sink)
            return flow;

        for (; numVisitedEdges[from] < adjacencyList.get(from).size(); ++numVisitedEdges[from]) {
            Edge curEdge = adjacencyList.get(from).get(numVisitedEdges[from]);

            // flow can only go to one level high
            if (levels[curEdge.vertexTo] == levels[from] + 1 && curEdge.flow < curEdge.capacity) {
                int bottleneck = dfs(curEdge.vertexTo, sink, Math.min(flow, curEdge.capacity - curEdge.flow));
                if (bottleneck > 0) {
                    curEdge.flow += bottleneck;
                    curEdge.residual.flow -= bottleneck;
                    return bottleneck;
                }
            }
        }
        return 0;
    }

    public void Dinic(int sink) {
        while (bfs(sink)) {
            Arrays.fill(numVisitedEdges, 0);
            int bottleneck = dfs(0, sink, Integer.MAX_VALUE);
            while (bottleneck != 0) {
                this.maxFlow += bottleneck;
                bottleneck = dfs(0, sink, Integer.MAX_VALUE);
            }
        }
    }

    public void minCut(PrintStream out) {
        HashSet<Integer> reachable = new HashSet<>();
        reachable.add(0);
        minCutBFS(reachable);
        for (int from = 0; from < numVertex; from++) {
            if (!reachable.contains(from))
                continue;
            for (Edge e : adjacencyList.get(from)) {
                if (e.capacity == 0)
                    continue;
                int to = e.vertexTo;
                if (!reachable.contains(to)) {
                    if (from == 0) { // just need to print regions
                        out.println("r" + (to - 1));
                        continue;
                    } else if (from < 7) {
                        out.print("r" + (from - 1));
                    } else {
                        out.print("c" + (from - 7));
                    }
                    if (to == numVertex - 1)
                        out.println(" KL");
                    else
                        out.println(" c" + (to - 7));
                }
            }
        }
    }

    private void minCutBFS(HashSet<Integer> reachable) {
        int[] visited = new int[numVertex];
        Queue<Integer> bfsQueue = new LinkedList<>();
        bfsQueue.add(0);
        visited[0] = 1;
        while (!bfsQueue.isEmpty()) {
            int from = bfsQueue.poll();
            for (Edge e : adjacencyList.get(from)) {
                if (e.flow < e.capacity && visited[e.vertexTo] != 1) {
                    bfsQueue.add(e.vertexTo);
                    visited[e.vertexTo] = 1;
                    reachable.add(e.vertexTo);
                }
            }
        }
    }
}
