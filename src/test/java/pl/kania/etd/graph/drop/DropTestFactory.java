package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Sets;
import pl.kania.etd.debug.Counter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DropTestFactory {

    private static final List<Double> DEF_TEST_SET = Arrays.asList(66., 30., 10., 35., 42., 0., 60., 70.);
    private static final List<Double> DEF_TEST_SET_2 = Arrays.asList(100., 75., 90., 60., 39., 10., 5., 5.);
    private static final List<Double> FIRST_TEST_SET = Arrays.asList(5., 100., 20., 15., 15., 2.);
    private static final List<Double> FIRST_TEST_SET_2 = Arrays.asList(100., 0., 50.);
    private static final List<Double> LAST_TEST_SET = Arrays.asList(12., 11., 10., 9., 8., 7., 6., 5., 3., 0.);
    private static final List<Double> ONE_ELEMENT_TEST_SET = Arrays.asList(10.);
    private final TestSet testSet;

    public DropTestFactory(TestSet testSet) {
        this.testSet = testSet;
    }

    public List<Double> getValues() {
        if (testSet == TestSet.DEFAULT) {
            return DEF_TEST_SET;
        } else if (testSet == TestSet.DEFAULT_2) {
            return DEF_TEST_SET_2;
        } else if (testSet == TestSet.FIRST) {
            return FIRST_TEST_SET;
        } else if (testSet == TestSet.FIRST_2) {
            return FIRST_TEST_SET_2;
        } else if (testSet == TestSet.LAST) {
            return LAST_TEST_SET;
        } else if (testSet == TestSet.ONE_ELEMENT) {
            return ONE_ELEMENT_TEST_SET;
        }
        throw new IllegalStateException("Unknown TestSet: " + testSet);
    }

    public List<SimpleDoubleValue> getElements() {
        return getValues().stream()
                .map(SimpleDoubleValue::new)
                .collect(Collectors.toList());
    }

    public List<Drop<SimpleDoubleValue>> getDrops() {
        int index = 0;
        if (testSet == TestSet.DEFAULT) {
            return Arrays.asList(
                    getDrop(index++, 4),
                    getDrop(index++, 6),
                    getDrop(index++, 18),
                    getDrop(index++, 7),
                    getDrop(index++, 5),
                    getDrop(index++, 20),
                    getDrop(index, 10)
            );
        } else if (testSet == TestSet.DEFAULT_2) {
            return Arrays.asList(
                    getDrop(index++, 10),
                    getDrop(index++, 15),
                    getDrop(index++, 15),
                    getDrop(index++, 21),
                    getDrop(index++, 29),
                    getDrop(index++, 5),
                    getDrop(index, 0)
            );
        } else if (testSet == TestSet.FIRST) {
            return Arrays.asList(
                    getDrop(index++, 80),
                    getDrop(index++, 5),
                    getDrop(index++, 0),
                    getDrop(index++, 10),
                    getDrop(index, 3));
        } else if (testSet == TestSet.FIRST_2) {
            return Arrays.asList(
                    getDrop(index++, 50),
                    getDrop(index, 50));
        } else if (testSet == TestSet.LAST) {
            return Arrays.asList(
                    getDrop(index++, 1),
                    getDrop(index++, 1),
                    getDrop(index++, 1),
                    getDrop(index++, 1),
                    getDrop(index++, 1),
                    getDrop(index++, 1),
                    getDrop(index++, 1),
                    getDrop(index++, 2),
                    getDrop(index, 3)
            );
        } else if (testSet == TestSet.ONE_ELEMENT) {
            return Collections.emptyList();
        }
        throw new IllegalStateException("Unknown TestSet: " + testSet);
    }

    public List<Drop<SimpleDoubleValue>> getDropsBeforeMaximumDrop() {
        return getDrops().subList(0, getMaximumDropIndex());
    }


    public double getAverageDrop() {
        List<Drop<SimpleDoubleValue>> drops = getDropsBeforeMaximumDrop();
        if (drops.isEmpty()) {
            return 0;
        }
        return drops.stream()
                .map(Drop::getValue)
                .reduce(Double::sum)
                .get() / drops.size();
    }

    public int getFirstElementIndexBeforeCriticalDrop() {
        if (testSet == TestSet.DEFAULT) {
            return 2;
        } else if (testSet == TestSet.DEFAULT_2) {
            return 3;
        } else if (testSet == TestSet.FIRST) {
            return 0;
        } else if (testSet == TestSet.FIRST_2) {
            return 0;
        } else if (testSet == TestSet.LAST) {
            return 7;
        } else if (testSet == TestSet.ONE_ELEMENT) {
            return 0;
        }
        throw new IllegalStateException("Unknown TestSet: " + testSet);
    }

    public List<AdaptiveCutOffTest.Value> getTestValues() {
        Counter ctr = new Counter();
        return getElements().stream()
                .map(e -> new AdaptiveCutOffTest.Value(ctr.getValueAsStringAndIncrement(), e.getValue()))
                .collect(Collectors.toList());
    }

    public Set<String> getPreservedValues() {
        if (testSet == TestSet.DEFAULT) {
            return new HashSet<>(Arrays.asList("0", "6", "7"));
        } else if (testSet == TestSet.DEFAULT_2) {
            return new HashSet<>(Arrays.asList("0", "1", "2", "3"));
        } else if (testSet == TestSet.FIRST) {
            return new HashSet<>(Arrays.asList("1"));
        } else if (testSet == TestSet.FIRST_2) {
            return new HashSet<>(Arrays.asList("0"));
        } else if (testSet == TestSet.LAST) {
            return new HashSet<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7"));
        } else if (testSet == TestSet.ONE_ELEMENT) {
            return new HashSet<>();
        }
        throw new IllegalStateException("Unknown TestSet: " + testSet);
    }

    public Set<String> getRemovedValues() {
        Set<String> testValues = getTestValues().stream()
                .map(AdaptiveCutOffTest.Value::getName)
                .collect(Collectors.toSet());
        testValues.removeAll(getPreservedValues());
        return testValues;
    }

    public int getMaximumDropIndex() {
        if (testSet == TestSet.DEFAULT) {
            return 5;
        } else if (testSet == TestSet.DEFAULT_2) {
            return 4;
        } else if (testSet == TestSet.FIRST) {
            return 0;
        } else if (testSet == TestSet.FIRST_2) {
            return 0;
        } else if (testSet == TestSet.LAST) {
            return 8;
        } else if (testSet == TestSet.ONE_ELEMENT) {
            return 0;
        }
        throw new IllegalStateException("Unknown TestSet: " + testSet);
    }

    private Drop<SimpleDoubleValue> getDrop(int index, int value) {
        return new Drop<>(index, value, getElements().get(index), getElements().get(index + 1));
    }
}
