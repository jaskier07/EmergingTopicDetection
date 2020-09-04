package pl.kania.etd.graph;

import org.jgrapht.Graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kania.etd.periods.WordStatistics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GraphSorterTest {

    @Test
    void givenGraphsAndWordsSortItByEnergy() {
        Map<String, WordStatistics> statistics = GraphTestFactory.getSCCWordStatistics();
        List<Graph<String, EdgeValue>> graphs = Arrays.asList(
                GraphTestFactory.getSCCResult1_3(),
                GraphTestFactory.getSCCResult4_9(),
                GraphTestFactory.getSCCResult7_8()
        );

        List<Graph<String, EdgeValue>> sortedGraphs = GraphSorter.sortByEnergy(new HashSet<>(graphs), statistics);

        List<Graph<String, EdgeValue>> graphsExpected = Arrays.asList(
                GraphTestFactory.getSCCResult7_8(),
                GraphTestFactory.getSCCResult4_9(),
                GraphTestFactory.getSCCResult1_3()
        );

        for (int i = 0; i < graphsExpected.size(); i++) {
            Assertions.assertEquals(GraphTestUtils.getEdgeValues(graphsExpected.get(i)), GraphTestUtils.getEdgeValues(sortedGraphs.get(i)));
        }
    }

}