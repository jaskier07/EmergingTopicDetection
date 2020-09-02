package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import pl.kania.etd.debug.Counter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphTestFactory {

    public static SimpleDirectedWeightedGraph<String, EdgeValue> getGraph() {
        SimpleDirectedWeightedGraph<String, EdgeValue> graph = new SimpleDirectedWeightedGraph<>(EdgeValue.class);
        String[] vertices = {"0", "1", "2", "3", "4"};
        Arrays.stream(vertices).forEach(graph::addVertex);

        addEdges(graph);

        return graph;
    }

    private static void addEdges(SimpleDirectedWeightedGraph<String, EdgeValue> graph) {
        List<Double[]> graphMatrix = getGraphMatrix();
        List<Double> norms = new ArrayList<>();
        graphMatrix.forEach(vector -> {
            norms.add(EuclideanNormSupplier.get(Arrays.asList(vector)));
        });

        for (int i = 0; i < norms.size(); i++) {
            for (int j = 0; j < norms.size(); j++) {
                if (i != j && graphMatrix.get(i)[j] != 0.) {
                    graph.addEdge(Integer.toString(i), Integer.toString(j), new EdgeValue(graphMatrix.get(i)[j] / norms.get(i)));
                }
            }
        }
    }

    public static List<Double[]> getGraphMatrix() {
        return Arrays.asList(
                new Double[]{5., 17., 29., 25., 4.},
                new Double[]{5., 0., 12., 6., 0.},
                new Double[]{8., 0., 0., 3., 0.},
                new Double[]{0., 0., 4., 0., 0.},
                new Double[]{6., 8., 0., 0., 0.}
        );
    }

    private static EdgeValue getEdgeValue(double val, double norm) {
        return new EdgeValue(val / norm);
    }
}
