package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriticalDropIndexSupplier {

    public static int get(Collection<Double> values) {
        List<Drop> drops = DropsProvider.getDrops(values);
        int maximumDropIndexExclusive = MaximumDropSupplier.getMaximumDropIndexInclusive(drops);
        List<Drop> dropsInRange = drops.subList(0, maximumDropIndexExclusive);

        double averageDrop = AverageDropSupplier.getAverageDropInclusive(dropsInRange);
        return CriticalDropSupplier.getIndexInclusive(dropsInRange, averageDrop);
    }
}
