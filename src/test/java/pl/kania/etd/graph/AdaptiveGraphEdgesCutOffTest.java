package pl.kania.etd.graph;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Test;

class AdaptiveGraphEdgesCutOffTest {

    @Test
    void test() {
        SimpleDirectedWeightedGraph<String, EdgeValue> graph = GraphTestFactory.getGraph();
        AdaptiveGraphEdgesCutOff.perform(graph);
        graph.edgeSet();
    }

}