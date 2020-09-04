package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.WordStatistics;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphFilter {

    public static List<Graph<String, EdgeValue>> filterOutGraphsSmallerThan(int elements, Collection<Graph<String, EdgeValue>> graphs) {
        log.info("Dropping graphs smaller than " + elements + ": ");
        return filter(graphs, g -> g.vertexSet().size() >= elements);
    }

    public static void cropGraphsBiggerThan(int maxClusterSize, List<Graph<String, EdgeValue>> graphs, Map<String, WordStatistics> wordStatistics) {
        log.info("Cropping graphs bigger than " + maxClusterSize + ": ");
        graphs.forEach(graph -> {
            if (graph.vertexSet().size() > maxClusterSize) {
                List<String> sortedVertices = graph.vertexSet().stream()
                        .sorted(getComparator(wordStatistics))
                        .collect(Collectors.toList());
                graph.removeAllVertices(sortedVertices.subList(maxClusterSize, sortedVertices.size()));
            }
        });
    }

    private static Comparator<? super String> getComparator(Map<String, WordStatistics> wordStatistics) {
        return Comparator.comparingDouble(o -> wordStatistics.get(o).getEnergy());
    }

    private static List<Graph<String, EdgeValue>> filter(Collection<Graph<String, EdgeValue>> graphs, Predicate<Graph> sizeCondition) {
        return graphs.stream()
                .filter(sizeCondition)
                .collect(Collectors.toList());
    }
}
