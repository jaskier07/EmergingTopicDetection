package pl.kania.etd.graph.drop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CriticalDropSupplierTest {

    @ParameterizedTest
    @EnumSource(value = TestSet.class)
    void givenDropsAndAverageDropFindFirstIndexBeforeAverageDrop(TestSet testSet) {
        DropTestFactory factory = new DropTestFactory(testSet);
        int index = CriticalDropSupplier.getFirstElementIndexBeforeCriticalDrop(factory.getDropsBeforeMaximumDrop(), factory.getAverageDrop());
        Assertions.assertEquals(factory.getFirstElementIndexBeforeCriticalDrop(), index);
    }

}