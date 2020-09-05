package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdaptiveCutOff {

    public static List<Double> performForDouble(List<Double> elements) {
        Comparator<Double> comparator = Double::compareTo;
        elements.sort(comparator.reversed());

        int indexInclusive = CriticalDropIndexSupplier.getIndex(elements);
        return elements.subList(0, indexInclusive);
    }

    public static <T extends HasValue> List<T> getPreservedElements(List<T> elements) {
        int indexInclusive = getIndexInclusive(elements);
        return elements.subList(0, indexInclusive);
    }

    public static <T extends HasValue> List<T> getRemovedElements(List<T> elements) {
        int indexInclusive = getIndexInclusive(elements);
        return elements.subList(indexInclusive, elements.size());
    }

    private static <T extends HasValue> int getIndexInclusive(List<T> elements) {
        Comparator<T> comparator = Comparator.<T, T>comparing(Function.identity()).reversed();
        elements.sort(comparator);

        int indexInclusive = CriticalDropIndexSupplier.getIndex(getValues(elements));
        return indexInclusive;
    }

    private static <T extends HasValue> List<Double> getValues(List<T> elements) {
        return elements.stream()
                .map(HasValue::getValue)
                .collect(Collectors.toList());
    }
}
