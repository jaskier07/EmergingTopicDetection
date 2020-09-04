package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import pl.kania.etd.debug.ProgressLogger;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.WordStatistics;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphGenerator {

    public static SimpleDirectedWeightedGraph<String, EdgeValue> generate(Map<String, WordStatistics> wordStatistics) {
        SimpleDirectedWeightedGraph<String, EdgeValue> graph = new SimpleDirectedWeightedGraph<>(EdgeValue.class);
        ProgressLogger pl = new ProgressLogger("Generating graph");

        wordStatistics.forEach((word, statistics) -> {
            graph.addVertex(word);
            double norm = EuclideanNormSupplier.get(statistics.getCorrelationVector().values());
            statistics.getCorrelationVector().forEach((word2, value) -> {
                graph.addVertex(word2);
                if (value != 0 && norm != 0) {
                    graph.addEdge(word, word2, new EdgeValue(value / norm));
                }
                pl.log(50000);
            });
        });

        pl.done("Generating graph");
        return graph;
    }
}
