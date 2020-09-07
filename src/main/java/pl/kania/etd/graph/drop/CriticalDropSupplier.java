package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CriticalDropSupplier {

    static <T extends HasValue<T>> int getFirstElementIndexBeforeCriticalDrop(List<Drop<T>> sortedDrops, double averageDrop) {
        if (sortedDrops.isEmpty()) {
            return 0;
        } else if (sortedDrops.size() == 1) {
            return sortedDrops.get(0).getFirstElementIndex();
        }

        return sortedDrops.stream()
                .filter(d -> d.getValue() > averageDrop)
                .findFirst()
                .map(Drop::getFirstElementIndex) // element before the first drop meeting > avgDrop
                .orElse(0);
    }
}
