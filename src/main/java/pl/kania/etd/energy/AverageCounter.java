package pl.kania.etd.energy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.kania.etd.periods.WordStatistics;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AverageCounter {

    public static double count(List<Double> value) {
        int size = value.size();
        return value.stream()
                .reduce(Double::sum)
                .orElse(0.) / size;

    }
}
