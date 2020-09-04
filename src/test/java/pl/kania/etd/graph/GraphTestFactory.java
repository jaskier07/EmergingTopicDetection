package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.mockito.internal.util.collections.Sets;
import pl.kania.etd.content.Word;
import pl.kania.etd.debug.Counter;
import pl.kania.etd.periods.WordStatistics;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static SimpleDirectedWeightedGraph<String, EdgeValue> getSCCGraph() {
        SimpleDirectedWeightedGraph<String, EdgeValue> g = new SimpleDirectedWeightedGraph<>(EdgeValue.class);
        Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9").forEach(g::addVertex);
        
        // first scc
        addEdge(1, 2, g);
        addEdge(1, 9, g);
        addEdge(2, 1, g);
        addEdge(2, 3, g);
        addEdge(3, 1, g);

        // second scc
        addEdge(3, 4, g);
        addEdge(4, 9, g);
        addEdge(4, 5, g);
        addEdge(5, 9, g);
        addEdge(6, 4, g);
        addEdge(9, 6, g);

        // third scc
        addEdge(7, 8, g);
        addEdge(7, 9, g);
        addEdge(8, 7, g);

        return g;
    }

    private static void addEdge(int source, int target, SimpleDirectedWeightedGraph<String, EdgeValue> g) {
        g.addEdge(Integer.toString(source), Integer.toString(target), new EdgeValue(
                Double.parseDouble(Integer.toString(source) + Integer.toString(target))));
    }

    public static Graph<String, EdgeValue> getSCCResult1_3() {
        SimpleDirectedWeightedGraph<String, EdgeValue> g = new SimpleDirectedWeightedGraph<>(EdgeValue.class);
        Arrays.asList("1", "2", "3").forEach(g::addVertex);

        addEdge(1, 2, g);
        addEdge(2, 1, g);
        addEdge(2, 3, g);
        addEdge(3, 1, g);

        return g;
    }

    public static Graph<String, EdgeValue> getSCCResult4_9() {
        SimpleDirectedWeightedGraph<String, EdgeValue> g = new SimpleDirectedWeightedGraph<>(EdgeValue.class);
        Arrays.asList("4", "5", "6", "9").forEach(g::addVertex);

        addEdge(4, 9, g);
        addEdge(4, 5, g);
        addEdge(5, 9, g);
        addEdge(6, 4, g);
        addEdge(9, 6, g);

        return g;
    }

    public static Graph<String, EdgeValue> getSCCResult7_8() {
        SimpleDirectedWeightedGraph<String, EdgeValue> g = new SimpleDirectedWeightedGraph<>(EdgeValue.class);
        Arrays.asList("7", "8").forEach(g::addVertex);

        addEdge(7, 8, g);
        addEdge(8, 7, g);

        return g;
    }

    public static Set<String> getSCCWords() {
        return Sets.newSet("2", "3", "5", "6", "7", "9");
    }

    public static Map<String, WordStatistics> getSCCWordStatistics() {
        Map<String, WordStatistics> statistics = new HashMap<>();
        IntStream.range(1, 10).forEach(i -> addStatisticToMap(i, statistics));
        return statistics;
    }

    private static void addStatisticToMap(int vertex, Map<String, WordStatistics> map) {
        String word = Integer.toString(vertex);
        WordStatistics wordStatistics = new WordStatistics(word);
        wordStatistics.setEnergy(vertex);
        map.put(word, wordStatistics);
    }
}
