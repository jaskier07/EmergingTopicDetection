package pl.kania.etd.graph.drop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MaximumDropSupplierTest {

    @Test
    void givenSortedDoubleValuesFindOnesIndexWithBiggestValueDifference() {
        int maximumDropIndex = MaximumDropSupplier.getMaximumDropIndexInclusive(DropTestFactory.getDrops());
        Assertions.assertEquals(3, maximumDropIndex);
    }

}