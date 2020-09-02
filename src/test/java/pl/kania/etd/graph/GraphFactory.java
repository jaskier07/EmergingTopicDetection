package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphFactory {

    public static SimpleDirectedWeightedGraph<String, Double> getGraph() {
        SimpleDirectedWeightedGraph<String, Double> graph = new SimpleDirectedWeightedGraph<>(Double.class);
        String[] vertices = {"1", "2", "3", "4", "5"};
        Arrays.stream(vertices).forEach(graph::addVertex);

        double norm1 = EuclideanNormSupplier.get(Arrays.asList(1., 1.));
        double norm2 = EuclideanNormSupplier.get(Arrays.asList(1., 1., 2.));
        double norm3 = EuclideanNormSupplier.get(Arrays.asList(1., 3.));
        double norm4 = EuclideanNormSupplier.get(Arrays.asList(4.));
        double norm5 = EuclideanNormSupplier.get(Arrays.asList(3., 2.));

        graph.addEdge("1", "4", getEdgeValue(1., norm1));
        graph.addEdge("1", "5", getEdgeValue(1., norm1));
        graph.addEdge("2", "1", getEdgeValue(1., norm2));
        graph.addEdge("2", "3", getEdgeValue(1., norm2));
        graph.addEdge("2", "4", getEdgeValue(2., norm2));
        graph.addEdge("3", "1", getEdgeValue(1., norm3));
        graph.addEdge("3", "4", getEdgeValue(3., norm3));
        graph.addEdge("4", "3", getEdgeValue(4., norm4));
        graph.addEdge("5", "1", getEdgeValue(3., norm5));
        graph.addEdge("5", "2",getEdgeValue( 2., norm5));

        return graph;
    }

    private static double getEdgeValue(double val, double norm) {
        return val / norm;
    }
}
