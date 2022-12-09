public class Edge {
    int vertexTo;
    int flow;
    int capacity;
    Edge residual;

    public Edge(int to, int flow, int capacity) {
        this.vertexTo = to;
        this.flow = flow;
        this.capacity = capacity;
    }
}
