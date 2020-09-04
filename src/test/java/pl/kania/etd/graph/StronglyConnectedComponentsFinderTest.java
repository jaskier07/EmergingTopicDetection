package pl.kania.etd.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StronglyConnectedComponentsFinderTest {

    @Test
    void givenGraphFindStronglyConnectedComponents() {
        SimpleDirectedWeightedGraph<String, EdgeValue> graph = GraphTestFactory.getSCCGraph();
        List<Graph<String, EdgeValue>> graphs = StronglyConnectedComponentsFinder.find(graph);
        Assertions.assertEquals(3, graphs.size());
    }

}