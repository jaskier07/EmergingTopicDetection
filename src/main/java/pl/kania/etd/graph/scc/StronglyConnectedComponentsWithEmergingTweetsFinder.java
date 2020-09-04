package pl.kania.etd.graph.scc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import pl.kania.etd.debug.PercentageFormatter;
import pl.kania.etd.graph.EdgeValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StronglyConnectedComponentsWithEmergingTweetsFinder {

    public static Set<Graph<String, EdgeValue>> find(Set<String> emergingWords, List<Graph<String, EdgeValue>> graphs) {
        Set<Graph<String, EdgeValue>> preservedGraphs = graphs.stream().filter(g -> {
            Set<String> foundEmergingWords = new HashSet<>();
            for (String word : g.vertexSet()) {
                if (emergingWords.contains(word)) {
                    foundEmergingWords.add(word);
                    if (foundEmergingWords.size() > 1) {
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toSet());

        log.info("Preserved graphs: " + preservedGraphs.size() + PercentageFormatter.format(preservedGraphs.size(), graphs.size()));
        return preservedGraphs;
    }
}
