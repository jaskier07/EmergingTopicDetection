package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CriticalDropSupplier {

    static int getIndexInclusive(List<Drop> sortedDrops, double averageDrop) {
        return sortedDrops.stream()
                .filter(d -> d.getValue() > averageDrop)
                .findFirst()
                .orElse(new Drop(0, 0))
                .getLastIndex();
    }
}
