package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AverageDropSupplier {

    static <T extends HasValue<T>>double getAverageDropInclusive(List<Drop<T>> sortedDrops) {
        if (sortedDrops.isEmpty()) {
            return 0;
        }
        return sortedDrops.stream()
                .map(Drop::getValue)
                .reduce(Double::sum)
                .orElse(Double.MAX_VALUE) / sortedDrops.size();
    }
}
