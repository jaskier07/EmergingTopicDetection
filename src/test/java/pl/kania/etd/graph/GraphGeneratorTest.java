package pl.kania.etd.graph;

import lombok.Value;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.kania.etd.periods.WordStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class GraphGeneratorTest {

    static SimpleDirectedWeightedGraph<String, EdgeValue> graph;
    static SimpleDirectedWeightedGraph<String, EdgeValue> generatedGraph;

    @BeforeAll
    static void init() {
        graph = GraphTestFactory.getGraph();

        List<Double[]> matrix = GraphTestFactory.getGraphMatrix();
        Map<String, WordStatistics> statistics = new HashMap<>();
        addWordStatisticsToMap(0, matrix, statistics);
        addWordStatisticsToMap(1, matrix, statistics);
        addWordStatisticsToMap(2, matrix, statistics);
        addWordStatisticsToMap(3, matrix, statistics);
        addWordStatisticsToMap(4, matrix, statistics);

        generatedGraph = GraphGenerator.generate(statistics);
    }

    @Test
    void givenWordStatisticsGenerateGraphWithProperVertexSet() {
        Assertions.assertEquals(graph.vertexSet(), generatedGraph.vertexSet());
    }

    @Test
    void givenWordStatisticsGenerateGraphWithProperEdgeWeights() {
        Assertions.assertEquals(GraphTestUtils.getEdgeValues(graph), GraphTestUtils.getEdgeValues(generatedGraph));
    }

    private static void addWordStatisticsToMap(int index, List<Double[]> matrix, Map<String, WordStatistics> statistics) {
        String word = Integer.toString(index);
        WordStatistics wordStatistics = new WordStatistics(word);

        int wordIndex = 0;
        for (double value : matrix.get(index)) {
            if (wordIndex != index) {
                wordStatistics.getCorrelationVector().put(Integer.toString(wordIndex), value);
            }
            wordIndex++;
        }

        statistics.put(word, wordStatistics);
    }

    @Value
    static class CorrelationValue {
        String word;
        double value;
    }

}