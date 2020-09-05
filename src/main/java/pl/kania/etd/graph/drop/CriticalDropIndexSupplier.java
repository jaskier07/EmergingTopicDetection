package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriticalDropIndexSupplier {

    public static int getIndex(Collection<Double> values) {
        List<Double> sortedValues = values.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        List<Drop> drops = getDrops(sortedValues);
        int maximumDropIndexInclusive = getMaximumDropIndexInclusive(drops);
        List<Drop> dropsInRange = getDropsInRange(drops, maximumDropIndexInclusive);

        double averageDrop = AverageDropSupplier.getAverageDropInclusive(dropsInRange);
        return CriticalDropSupplier.getIndexInclusive(dropsInRange, averageDrop);
    }

    public static double getValue(Collection<Double> values) {
        List<Double> sortedValues = values.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        List<Drop> drops = getDrops(sortedValues);
        int maximumDropIndexInclusive = getMaximumDropIndexInclusive(drops);
        List<Drop> dropsInRange = getDropsInRange(drops, maximumDropIndexInclusive);

        return AverageDropSupplier.getAverageDropInclusive(dropsInRange);
    }

    private static List<Drop> getDropsInRange(List<Drop> drops, int maximumDropIndexInclusive) {
        return drops.subList(0, maximumDropIndexInclusive);
    }

    private static int getMaximumDropIndexInclusive(List<Drop> drops) {
        return MaximumDropSupplier.getMaximumDropIndexInclusive(drops);
    }

    private static List<Drop> getDrops(Collection<Double> values) {
        return DropsProvider.getDrops(values);
    }
}
