package pl.kania.etd.graph.drop;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class AdaptiveCutOffTest {

    @ParameterizedTest
    @EnumSource(value = TestSet.class)
    void givenValuesCutOffOnesBeforeCriticalDrop(TestSet testSet) {
        DropTestFactory factory = new DropTestFactory(testSet);

        List<Value> values = factory.getTestValues();
        AdaptiveCutOff.performActionForElementsBeforeCriticalDrop(values, list -> list.forEach(v -> v.setPreserved(true)));
        Set<String> preservedValues = values.stream()
                .filter(Value::isPreserved)
                .map(Value::getName)
                .collect(Collectors.toSet());

        Assertions.assertEquals(factory.getPreservedValues(), preservedValues);
    }

    @ParameterizedTest
    @EnumSource(value = TestSet.class)
    void givenValuesCutOffOnesAfterCriticalDrop(TestSet testSet) {
        DropTestFactory factory = new DropTestFactory(testSet);

        List<Value> values = factory.getTestValues();
        AdaptiveCutOff.performActionForElementsAfterCriticalDrop(values, list -> list.forEach(v -> v.setPreserved(true)));
        Set<String> removedValues = values.stream()
                .filter(Value::isPreserved)
                .map(Value::getName)
                .collect(Collectors.toSet());

        Assertions.assertEquals(factory.getRemovedValues(), removedValues);
    }

    @EqualsAndHashCode(of = "name")
    @Getter
    static class Value implements HasValue<Value> {

        String name;
        double value;
        @Setter
        boolean preserved;

        public Value(String name, double value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public double getValue() {
            return value;
        }

        @Override
        public int compareTo(Value o) {
            if (o == null) {
                return -1;
            }
            return (value - o.getValue()) > 0 ? 1 : -1;
        }
    }

}