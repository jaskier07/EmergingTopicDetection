package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StronglyConnectedComponentsFinder {

    public static List<Graph<String, EdgeValue>> find(Graph<String, EdgeValue> graph) {
        StrongConnectivityAlgorithm<String, EdgeValue> algorithm = new KosarajuStrongConnectivityInspector<>(graph);
        List<Graph<String, EdgeValue>> components = algorithm.getStronglyConnectedComponents();
        return components;
    }

}
