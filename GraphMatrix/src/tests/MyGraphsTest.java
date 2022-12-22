package tests;

import graphs.IGraph;
import structures.DirectedGraph;

/**
 * Test driver for developing DirectedGraph
 * @author Mark Mendoza
 * @version 1.0
 */
public class MyGraphsTest {
    private static final String[] testVertices = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L"};
    public static final int OTHER_SIZE = 10;
    public static final int TEST_WEIGHT = 15;
    private static IGraph<String> graph;

    /**
     * @param args String array of arguments
     */
    public static void main(String[] args) {
        graph = new DirectedGraph<>();
        graph = new DirectedGraph<>(OTHER_SIZE);

        addTestEdges(2);
        graph.clear();

        addTestVertices();
        addTestEdges(TEST_WEIGHT);
        printSizes();

        System.out.println("Vertices: " + graph.vertices());
        System.out.println("Edges: " + graph.edges());

        graph.addEdge("C", "B", 4);
        graph.removeEdge("A", "B");
        graph.removeVertex("B");

        System.out.println(graph);
        graph.clear();

        System.out.println(graph);
    }

    private static void addTestVertices() {
        for (String letter : testVertices) {
            graph.addVertex(letter);
        }
    }

    private static void addTestEdges(int weight)
    {
        //add link between letters that are adjacent (A-B, B-C, ... , J-K, K-L)
        for (int i = 0; i < testVertices.length - 1; i++)
        {
            String source = testVertices[i];
            String destination = testVertices[i + 1];
            graph.addEdge(source, destination, weight);
        }
    }

    private static void printSizes() {
        System.out.println("Vertices: " + graph.vertexSize());
        System.out.println("Edges: " + graph.edgeSize());
    }
}
