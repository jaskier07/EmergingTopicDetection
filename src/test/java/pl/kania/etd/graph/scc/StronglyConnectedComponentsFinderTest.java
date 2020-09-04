package pl.kania.etd.graph.scc;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kania.etd.graph.EdgeValue;
import pl.kania.etd.graph.GraphTestFactory;
import pl.kania.etd.graph.GraphTestUtils;

import java.util.List;

class StronglyConnectedComponentsFinderTest {

    @Test
    void givenGraphFindStronglyConnectedComponents() {
        SimpleDirectedWeightedGraph<String, EdgeValue> graph = GraphTestFactory.getSCCGraph();
        List<Graph<String, EdgeValue>> graphs = StronglyConnectedComponentsFinder.find(graph);

        Assertions.assertTrue(GraphTestUtils.graphExistsInResult(GraphTestFactory.getSCCResult1_3(), graphs));
        Assertions.assertTrue(GraphTestUtils.graphExistsInResult(GraphTestFactory.getSCCResult4_9(), graphs));
        Assertions.assertTrue(GraphTestUtils.graphExistsInResult(GraphTestFactory.getSCCResult7_8(), graphs));
    }

}