package pl.kania.etd.graph;

import lombok.Value;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kania.etd.periods.WordStatistics;

import java.util.HashMap;
import java.util.Map;

class GraphGeneratorTest {

    @Test
    void givenWordStatisticsGenerateGraphWithProperWeights() {
        SimpleDirectedWeightedGraph<String, Double> graph = GraphFactory.getGraph();

        Map<String, WordStatistics> statistics = new HashMap<>();
        addWordStatisticsToMap("1", new double[] { 0., 0., 0., 1., 1. }, statistics);
        addWordStatisticsToMap("2", new double[] { 1., 0., 1., 2., 0. }, statistics);
        addWordStatisticsToMap("3", new double[] { 1., 0., 0., 3., 0. }, statistics);
        addWordStatisticsToMap("4", new double[] { 0., 0., 4., 0., 0. }, statistics);
        addWordStatisticsToMap("5", new double[] { 3., 2., 0., 0., 0. }, statistics);

        SimpleDirectedWeightedGraph<String, Double> generatedGraph = GraphGenerator.generate(statistics);

        Assertions.assertEquals(graph.vertexSet(), generatedGraph.vertexSet());
        Assertions.assertEquals(graph.edgeSet(), generatedGraph.edgeSet());
    }

    private void addWordStatisticsToMap(String word, double[] correlationValues, Map<String, WordStatistics> statistics) {
        WordStatistics wordStatistics = new WordStatistics(word);

        int wordIndex = 1;
        for (double value : correlationValues) {
            wordStatistics.getCorrelationVector().put(Integer.toString(wordIndex++), value);
        }

        statistics.put(word, wordStatistics);
    }

    @Value
    static class CorrelationValue {
        String word;
        double value;
    }

}