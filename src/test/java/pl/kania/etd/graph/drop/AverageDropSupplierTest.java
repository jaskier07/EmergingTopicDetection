package pl.kania.etd.graph.drop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.List;

class AverageDropSupplierTest {

    @ParameterizedTest
    @EnumSource(value = TestSet.class)
    void givenSortedDoubleValuesFindAverageValueDifference(TestSet testSet) {
        DropTestFactory factory = new DropTestFactory(testSet);

        List<Drop<SimpleDoubleValue>> drops = factory.getDropsBeforeMaximumDrop();
        double averageDrop = AverageDropSupplier.getAverageDropInclusive(drops);
        Assertions.assertEquals(factory.getAverageDrop(), averageDrop, 0.01);
    }
}