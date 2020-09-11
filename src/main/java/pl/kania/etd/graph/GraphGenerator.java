package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import pl.kania.etd.debug.ProgressLogger;
import pl.kania.etd.periods.WordStatistics;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphGenerator {

    public static SimpleDirectedWeightedGraph<String, EdgeValue> generate(Map<String, WordStatistics> wordStatistics) {
        SimpleDirectedWeightedGraph<String, EdgeValue> graph = new SimpleDirectedWeightedGraph<>(EdgeValue.class);
        ProgressLogger pl = new ProgressLogger("Generating graph");
        int wordsInPeriod = wordStatistics.size();
        Average val = new Average(wordsInPeriod);

        wordStatistics.forEach((word, statistics) -> {
            graph.addVertex(word);
            double norm = EuclideanNormSupplier.get(statistics.getCorrelationVector().values());
            statistics.getCorrelationVector().forEach((word2, value) -> {
                graph.addVertex(word2);
                if (value != 0 && norm != 0) {
                    graph.addEdge(word, word2, new EdgeValue(value / norm));
                }
                val.add(value);
            });
            pl.log(100);
        });

        pl.done("Generating graph, average value = " + val.get());
        return graph;
    }

    @Getter
    static class Average {
        double value;
        int size;

        public Average(int size) {
            this.size = size;
        }

        void add(double newValue) {
            value += newValue;
        }

        double get() {
            return value / size;
        }
    }
}
