package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.content.Topic;
import pl.kania.etd.periods.WordStatistics;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphFilter {

    public static void filterOutGraphsSmallerThan(int elements, List<Topic> graphs) {
        log.info("Dropping graphs smaller than " + elements + ": ");
        filterOut(graphs, g -> g.getGraph().vertexSet().size() <= elements);
    }

    public static void cropGraphsBiggerThan(int maxClusterSize, List<Topic> graphs, Map<String, WordStatistics> wordStatistics) {
        log.info("Cropping graphs bigger than " + maxClusterSize + ": ");
        graphs.forEach(topic -> {
            if (topic.getGraph().vertexSet().size() > maxClusterSize) {
                List<String> sortedVertices = topic.getGraph().vertexSet().stream()
                        .sorted(getComparator(wordStatistics))
                        .collect(Collectors.toList());
                topic.getGraph().removeAllVertices(sortedVertices.subList(maxClusterSize, sortedVertices.size()));
            }
        });
    }

    private static Comparator<? super String> getComparator(Map<String, WordStatistics> wordStatistics) {
        return Comparator.comparingDouble(o -> wordStatistics.get(o).getEnergy());
    }

    private static void filterOut(List<Topic> graphs, Predicate<Topic> sizeCondition) {
        graphs.removeIf(sizeCondition);
    }
}
