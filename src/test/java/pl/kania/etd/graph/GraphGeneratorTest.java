package pl.kania.etd.graph;

import lombok.Value;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kania.etd.periods.WordStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class GraphGeneratorTest {

    @Test
    void givenWordStatisticsGenerateGraphWithProperWeights() {
        SimpleDirectedWeightedGraph<String, EdgeValue> graph = GraphTestFactory.getGraph();

        List<Double[]> matrix = GraphTestFactory.getGraphMatrix();
        Map<String, WordStatistics> statistics = new HashMap<>();
        addWordStatisticsToMap(0, matrix, statistics);
        addWordStatisticsToMap(1, matrix, statistics);
        addWordStatisticsToMap(2, matrix, statistics);
        addWordStatisticsToMap(3, matrix, statistics);
        addWordStatisticsToMap(4, matrix, statistics);

        SimpleDirectedWeightedGraph<String, EdgeValue> generatedGraph = GraphGenerator.generate(statistics);

        Assertions.assertEquals(graph.vertexSet(), generatedGraph.vertexSet());
        Assertions.assertEquals(getEdgeValues(graph), getEdgeValues(generatedGraph));
    }

    private List<Double> getEdgeValues(SimpleDirectedWeightedGraph<String, EdgeValue> graph) {
        return graph.edgeSet().stream().map(EdgeValue::getValue).collect(Collectors.toList());
    }

    private void addWordStatisticsToMap(int index, List<Double[]> matrix, Map<String, WordStatistics> statistics) {
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