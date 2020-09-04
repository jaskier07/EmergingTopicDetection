package pl.kania.etd.graph.scc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import pl.kania.etd.graph.EdgeValue;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StronglyConnectedComponentsFinder {

    public static List<Graph<String, EdgeValue>> find(Graph<String, EdgeValue> graph) {
        log.info("Finding SCC");

        StrongConnectivityAlgorithm<String, EdgeValue> algorithm = new KosarajuStrongConnectivityInspector<>(graph);
        List<Graph<String, EdgeValue>> components = algorithm.getStronglyConnectedComponents();

        log.info("Found " + components.size() + " SCC");
        return components;
    }

}
