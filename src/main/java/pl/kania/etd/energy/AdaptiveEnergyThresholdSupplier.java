package pl.kania.etd.energy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.kania.etd.graph.drop.CriticalDropIndexSupplier;
import pl.kania.etd.periods.WordStatistics;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdaptiveEnergyThresholdSupplier {

    public static double get(Collection<WordStatistics> statistics) {
        return CriticalDropIndexSupplier.getValue(getValues(statistics));
    }

    private static List<Double> getValues(Collection<WordStatistics> statistics) {
        return statistics.stream()
                .map(WordStatistics::getEnergy)
                .collect(Collectors.toList());
    }
}
