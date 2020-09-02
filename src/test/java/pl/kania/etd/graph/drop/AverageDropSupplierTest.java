package pl.kania.etd.graph.drop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class AverageDropSupplierTest {

    @Test
    void givenSortedDoubleValuesFindAverageValueDifference() {
        List<Drop> drops = DropTestFactory.getDropsBeforeMaximumDrop();
        double averageDrop = AverageDropSupplier.getAverageDropInclusive(drops);
        Assertions.assertEquals(DropTestFactory.getAverageDrop(), averageDrop, 0.01);
    }
}