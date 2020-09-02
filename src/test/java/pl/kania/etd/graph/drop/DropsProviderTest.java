package pl.kania.etd.graph.drop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DropsProviderTest {

    @Test
    void givenValuesSortThemAndFindDifferencesBetweenNeighbours() {
        List<Double> values = DropTestFactory.getValues();
        List<Drop> drops = DropsProvider.getDrops(values);
        List<Drop> expectedDrops = DropTestFactory.getDrops();

        Assertions.assertEquals(expectedDrops, drops);
    }

}