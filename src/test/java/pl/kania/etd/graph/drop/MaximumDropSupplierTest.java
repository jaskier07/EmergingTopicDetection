package pl.kania.etd.graph.drop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.List;

class MaximumDropSupplierTest {

    @ParameterizedTest
    @EnumSource(value = TestSet.class)
    void givenDoubleValuesFindMaximumDropIndex(TestSet testSet) {
        DropTestFactory factory = new DropTestFactory(testSet);
        int maximumDropIndex = MaximumDropSupplier.getMaximumDropIndexInclusive(factory.getDrops());
        Assertions.assertEquals(factory.getMaximumDropIndex(), maximumDropIndex);
    }

}