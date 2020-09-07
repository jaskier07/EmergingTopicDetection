package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class DropsProvider {

    static <T extends HasValue<T>>List<Drop<T>> getDrops(Collection<T> values) {
        List<T> sortedValues = values.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        List<Drop<T>> drops = new ArrayList<>();

        for (int index = 1; index < sortedValues.size(); index++) {
            T previousElement = sortedValues.get(index - 1);
            T currentElement = sortedValues.get(index);
            double previousValue = previousElement.getValue();
            double currentValue = currentElement.getValue();

            double difference = previousValue - currentValue;

            drops.add(new Drop<>(index - 1, difference, previousElement, currentElement));
        }

        return drops;
    }

}
