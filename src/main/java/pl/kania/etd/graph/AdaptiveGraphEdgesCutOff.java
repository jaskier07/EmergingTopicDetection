package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import pl.kania.etd.debug.PercentageFormatter;
import pl.kania.etd.debug.ProgressLogger;
import pl.kania.etd.graph.drop.AdaptiveCutOff;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdaptiveGraphEdgesCutOff {

    public static void perform(Graph<String, EdgeValue> graph) {
        graph.vertexSet()
                .forEach(word -> {
                            Set<EdgeValue> edges = graph.outgoingEdgesOf(word);
                            List<EdgeValue> removedEdges = AdaptiveCutOff.getRemovedElements(new ArrayList<>(edges));
                            graph.removeAllEdges(removedEdges);
                        }
                );
        new ProgressLogger().log(1);
    }
}
