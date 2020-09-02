package pl.kania.etd.graph.drop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CriticalDropSupplierTest {

    @Test
    void givenDropsAndAverageDropFindFirstIndexBeforeAverageDrop() {
        int index = CriticalDropSupplier.getIndexInclusive(DropTestFactory.getDropsBeforeMaximumDrop(), DropTestFactory.getAverageDrop());
        Assertions.assertEquals(DropTestFactory.getCriticalDropIndexInclusive(), index);
    }

}