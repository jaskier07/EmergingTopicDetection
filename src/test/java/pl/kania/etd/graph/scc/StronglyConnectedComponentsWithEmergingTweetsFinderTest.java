package pl.kania.etd.graph.scc;

import org.jgrapht.Graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.kania.etd.graph.EdgeValue;
import pl.kania.etd.graph.GraphTestFactory;
import pl.kania.etd.graph.GraphTestUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StronglyConnectedComponentsWithEmergingTweetsFinderTest {

    static Set<String> emergingWords;
    static List<Graph<String, EdgeValue>> graphs;
    static Set<Graph<String, EdgeValue>> resultSet;

    @BeforeAll
    static void init() {
        emergingWords = GraphTestFactory.getSCCWords();
        graphs = Arrays.asList(
                GraphTestFactory.getSCCResult1_3(),
                GraphTestFactory.getSCCResult4_9(),
                GraphTestFactory.getSCCResult7_8()
        );
        resultSet = StronglyConnectedComponentsWithEmergingTweetsFinder.find(emergingWords, graphs);
    }

    @Test
    void givenWordSetAndSCCGraphSetFindInResultSCC_1_3() {
        Assertions.assertTrue(GraphTestUtils.graphExistsInResult(GraphTestFactory.getSCCResult1_3(), resultSet));
    }

    @Test
    void givenWordSetAndSCCGraphSetFindInResultSCC_4_9() {
        Assertions.assertTrue(GraphTestUtils.graphExistsInResult(GraphTestFactory.getSCCResult4_9(), resultSet));
    }

    @Test
    void givenWordSetAndSCCGraphSetNotFindInResultSCC_7_8() {
        Assertions.assertFalse(GraphTestUtils.graphExistsInResult(GraphTestFactory.getSCCResult7_8(), resultSet));
    }


}