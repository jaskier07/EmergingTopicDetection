package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AverageDropSupplier {

    static double getAverageDropInclusive(List<Drop> sortedDrops) {
        return sortedDrops.stream()
                .map(Drop::getValue)
                .reduce(Double::sum)
                .orElse(Double.MAX_VALUE) / sortedDrops.size();
    }
}
