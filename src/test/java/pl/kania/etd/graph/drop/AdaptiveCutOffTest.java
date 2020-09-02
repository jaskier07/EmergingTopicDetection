package pl.kania.etd.graph.drop;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class AdaptiveCutOffTest {

    @Test
    void givenValuesCutOffOnesBeforeCriticalDrop() {
        List<Value> values = DropTestFactory.getTestValues();
        List<Value> result = AdaptiveCutOff.perform(values);

        List<Value> preservedValues = values.subList(0, DropTestFactory.getCriticalDropIndexInclusive());
        Assertions.assertEquals(preservedValues, result);
    }

    @AllArgsConstructor
    static class Value implements HasValue<Value> {

        String name;
        double value;

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