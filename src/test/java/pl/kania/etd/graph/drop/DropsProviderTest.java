package pl.kania.etd.graph.drop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.stream.Collectors;

class DropsProviderTest {

    @ParameterizedTest
    @EnumSource(value = TestSet.class)
    <T extends HasValue<T>> void givenValuesSortThemAndFindDifferencesBetweenNeighbours(TestSet testSet) {
        DropTestFactory factory = new DropTestFactory(testSet);
        List<SimpleDoubleValue> values = factory.getValues().stream()
                .map(SimpleDoubleValue::new)
                .collect(Collectors.toList());
        List<Drop<SimpleDoubleValue>> drops = DropsProvider.getDrops(values);
        List<Drop<SimpleDoubleValue>> expectedDrops = factory.getDrops();

        Assertions.assertEquals(expectedDrops.size(), drops.size());
        for (int i = 0; i < drops.size(); i++) {
            Assertions.assertEquals(expectedDrops.get(i).getValue(), drops.get(i).getValue());
        }
    }

}