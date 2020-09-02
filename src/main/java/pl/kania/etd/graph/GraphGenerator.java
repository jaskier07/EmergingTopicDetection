package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.WordStatistics;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphGenerator {

    public static SimpleDirectedWeightedGraph<String, Double> generate(Map<String, WordStatistics> wordStatistics) {
        SimpleDirectedWeightedGraph<String, Double> graph = new SimpleDirectedWeightedGraph<>(Double.class);

        wordStatistics.forEach((word, statistics) -> {
            graph.addVertex(word);
            double norm = EuclideanNormSupplier.get(statistics.getCorrelationVector().values());
            statistics.getCorrelationVector().forEach((word2, value) -> {
                graph.addVertex(word2);
                if (value != 0 && norm != 0) {
                    graph.addEdge(word, word2, value / norm);
                }
            });
        });

        return graph;
    }
}
