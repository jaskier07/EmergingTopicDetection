package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jgrapht.Graph;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphTestUtils {


    public static boolean graphExistsInResult(Graph<String, EdgeValue> searchedGraph, Collection<Graph<String, EdgeValue>> result) {
        Set<Double> searchedGraphEdgeValues = getEdgeValues(searchedGraph);

        return result.stream().anyMatch(graph -> {
            Set<Double> edgeValues = getEdgeValues(graph);
            return graph.vertexSet().equals(searchedGraph.vertexSet()) && searchedGraphEdgeValues.equals(edgeValues);
        });
    }

    public static Set<Double> getEdgeValues(Graph<String, EdgeValue> graph) {
        return graph.edgeSet().stream().map(EdgeValue::getValue).collect(Collectors.toSet());
    }
}
