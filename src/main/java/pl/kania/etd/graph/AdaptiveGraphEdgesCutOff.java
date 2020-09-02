package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import pl.kania.etd.debug.ProgressLogger;
import pl.kania.etd.graph.drop.AdaptiveCutOff;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdaptiveGraphEdgesCutOff {

    public static void perform(SimpleDirectedWeightedGraph<String, EdgeValue> graph) {
        int edgesBeforeCutting = graph.edgeSet().size();

        ProgressLogger pl = new ProgressLogger("Cutting off edges in graph");
        graph.vertexSet()
                .forEach(word -> {
                            Set<EdgeValue> edges = graph.outgoingEdgesOf(word);
                            List<EdgeValue> removedEdges = AdaptiveCutOff.getRemovedElements(new ArrayList<>(edges));
                            graph.removeAllEdges(removedEdges);
                            pl.log();
                        }
                );
        pl.done("Cutting off edges in graph");
        log.info("Preserved " + graph.edgeSet().size() + " edges / " + edgesBeforeCutting);
    }
}
