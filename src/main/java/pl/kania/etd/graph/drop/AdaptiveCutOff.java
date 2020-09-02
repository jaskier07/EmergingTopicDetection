package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdaptiveCutOff {

    public static <T extends HasValue> List<T> perform(List<T> elements) {
        Comparator<T> comparator = Comparator.<T, T>comparing(Function.identity()).reversed();
        elements.sort(comparator);

        int indexInclusive = CriticalDropIndexSupplier.get(getSortedValues(elements));
        return elements.subList(0, indexInclusive);
    }

    private static <T extends HasValue> List<Double> getSortedValues(List<T> elements) {
        return elements.stream()
                .map(HasValue::getValue)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}
