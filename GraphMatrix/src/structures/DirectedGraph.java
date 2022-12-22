package structures;

import graphs.Edge;
import graphs.IGraph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Class creates and manages a simulation of a directed graph
 * @author Mark Mendoza
 * @version 1.0
 */
public class DirectedGraph<V> implements IGraph<V>
{
    private static final int INITIAL_SIZE = 10; //base matrix size
    private static final double RESIZE_MULTIPLIER = 1.5; //new matrix will double in size

    private final Stack<Integer> stack = new Stack<>();
    private final Bijection<V, Integer> bijection = new Bijection<>();

    private int[][] matrix; //holder for vertices and edges
    private int verticesCount = 0; //initialize vertices
    private int edgesCount = 0;  //initialize edges

    /**
     * Initializes matrix to length of 10
     */
    public DirectedGraph()
    {
        matrix = new int[INITIAL_SIZE][INITIAL_SIZE]; //initialize matrix to hold 10 vertices
        stack.push(0);  //start at index zero
    }

    /**
     * Initializes matrix to length specified
     * @param initialSize size of matrix desired
     */
    public DirectedGraph(int initialSize)
    {
        matrix = new int[initialSize][initialSize];
        stack.push(0);  //start at index zero
    }

    @Override
    public boolean addVertex(V vertex)
    {
        //if vertex already exists
        if (bijection.containsKey(vertex))
        {
            return false; //exit
        }

        //if size threshold is reached
        if (verticesCount >= matrix.length)
        {
            int newMatrixLength = (int) (matrix.length * RESIZE_MULTIPLIER); //calc new matrix size
            int[][] newMatrix = new int[newMatrixLength][newMatrixLength];  //create new, bigger matrix

            //copy data to new matrix
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    newMatrix[i][j] = matrix[i][j];
                }
            }
            matrix = newMatrix; //update matrix to new, bigger matrix
        }

        //index logic
        int index = stack.pop();

        if (stack.isEmpty())
        {
            stack.push(index + 1);  //push next index to stack
        }
        bijection.add(vertex, index);
        verticesCount++;

        return true;
    }

    @Override
    public boolean addEdge(V source, V destination, int weight)
    {
        //edge already exists, or neither vertex exists
        if (containsEdge(source, destination) || !containsVertex(source) ||
                !containsVertex(destination))
        {
            return false;
        }
        if (weight < 0) //negative weights not allowed
        {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        //add the edge weight to spot
        Integer sourceIndex = bijection.getValue(source);
        Integer destIndex = bijection.getValue(destination);
        matrix[sourceIndex][destIndex] = weight;

        edgesCount++;
        return true;
    }

    @Override
    public int vertexSize()
    {
        return verticesCount;
    }

    @Override
    public int edgeSize()
    {
        return edgesCount;
    }

    @Override
    public boolean containsVertex(V vertex)
    {
        return bijection.containsKey(vertex);
    }

    @Override
    public boolean containsEdge(V source, V destination)
    {
        //if either vertex does not exist
        if (!bijection.containsKey(source) || !bijection.containsKey(destination))
        {
            return false;
        }

        int sourceValue = bijection.getValue(source);
        int destinationValue = bijection.getValue(destination);

        return matrix[sourceValue][destinationValue] > 0;   //return spot not empty
    }

    @Override
    public int edgeWeight(V source, V destination)
    {
        //if either vertex does not exist
        if (!bijection.containsKey(source) || !bijection.containsKey(destination))
        {
            return -1;
        }

        //return value at index
        int sourceValue = bijection.getValue(source);
        int destinationValue = bijection.getValue(destination);
        return matrix[sourceValue][destinationValue];
    }

    @Override
    public Set<V> vertices()
    {
        Set<V> set = new HashSet<>();

        //for each index
        for (int i = 0; i < verticesCount; i++) {
            set.add(bijection.getKey(i));   //add vertex equivalent
        }

        return set;
    }

    @Override
    public Set<Edge<V>> edges()
    {
        Set<Edge<V>> set = new HashSet<>();

        //check each index new matrix
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {

                // if edge exists
                if (matrix[i][j] > 0)
                {
                    V source = bijection.getKey(i);
                    V destination = bijection.getKey(j);
                    int weight = this.matrix[i][j];

                    set.add(new Edge<>(source, destination, weight));
                }
            }
        }
        return set;
    }

    @Override
    public boolean removeVertex(V vertex)
    {
        //vertex does not exist
        if (!bijection.containsKey(vertex))
        {
            return false;
        }

        //reset spots
        for (int i = 0; i < matrix.length; i++) {
            //if on x of vertex
            if (bijection.getValue(vertex) == i)
            {
                //reset along row
                for (int j = 0; j < matrix.length; j++) {
                    matrix[i][j] = 0;
                }
            }
            else
            {
                //reset single spot at y vertex
                matrix[i][bijection.getValue(vertex)] = 0;
            }
        }

        stack.push(bijection.getValue(vertex)); //push unused index to stack
        verticesCount--;
        return true;
    }

    @Override
    public boolean removeEdge(V source, V destination)
    {
        //if source or destination vertices do not exist
        if (!containsVertex(source) || !containsVertex(destination))
        {
            return false;
        }

        matrix[bijection.getValue(source)][bijection.getValue(destination)] = 0; //reset weight to 0
        edgesCount--;
        return true;
    }

    @Override
    public void clear()
    {
        //reset matrix to default
        matrix = new int[INITIAL_SIZE][INITIAL_SIZE];

        //reset stack and bijection
        stack.clear();
        stack.push(0);
        bijection.clear();

        //reset counters
        verticesCount = 0;
        edgesCount = 0;
    }

    @Override
    public String toString() {
        return "DirectedGraph{" +
                "stack=" + stack +
                ", bijection=" + bijection +
                ", matrix=" + Arrays.toString(matrix) +
                ", verticesCount=" + verticesCount +
                ", edgesCount=" + edgesCount +
                '}';
    }
}
