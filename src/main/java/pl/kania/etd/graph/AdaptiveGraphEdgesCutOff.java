package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import pl.kania.etd.debug.ProgressLogger;
import pl.kania.etd.graph.drop.AdaptiveCutOff;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdaptiveGraphEdgesCutOff {

    public static void perform(Graph<String, EdgeValue> graph) {
        ProgressLogger pl = new ProgressLogger("Cutting off edges");
        graph.vertexSet()
                .forEach(word -> {
                            Set<EdgeValue> edges = graph.outgoingEdgesOf(word);
                            AdaptiveCutOff.performActionForElementsAfterCriticalDrop(edges, graph::removeAllEdges);
                            pl.log();
                        }
                );
        pl.done();
    }
}
