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

    static List<Drop> getDrops(Collection<Double> values) {
        List<Double> sortedValues = values.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        List<Drop> drops = new ArrayList<>();

        for (int i = 1; i < sortedValues.size(); i++) {
            double previous = sortedValues.get(i - 1);
            double current = sortedValues.get(i);
            double difference = previous - current;
            drops.add(new Drop(i, difference));
        }

        return drops;
    }

}
