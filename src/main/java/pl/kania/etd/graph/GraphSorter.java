package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jgrapht.Graph;
import pl.kania.etd.energy.AverageCounter;
import pl.kania.etd.periods.WordStatistics;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphSorter {

    public static List<Graph<String, EdgeValue>> sortByEnergy(Set<Graph<String, EdgeValue>> graphs, Map<String, WordStatistics> wordStatistics) {
        Map<Graph<String, EdgeValue>, Double> energyPerGraph = new HashMap<>();
        graphs.forEach(graph -> energyPerGraph.put(graph, AverageCounter.count(getEnergy(graph, wordStatistics))));
        return energyPerGraph.entrySet()
                .stream()
                .sorted((g1, g2) -> -g1.getValue().compareTo(g2.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static List<Double> getEnergy(Graph<String, EdgeValue> graph, Map<String, WordStatistics> wordStatistics) {
        return graph.vertexSet().stream().map(word -> wordStatistics.get(word).getEnergy()).collect(Collectors.toList());
    }
}
