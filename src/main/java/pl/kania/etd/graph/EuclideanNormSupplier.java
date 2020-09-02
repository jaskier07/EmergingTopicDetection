package pl.kania.etd.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EuclideanNormSupplier {

    public static double get(Collection<Double> values) {
        return Math.sqrt(values.stream()
                .map(v -> v * v)
                .reduce(Double::sum)
                .orElseThrow(() -> new IllegalStateException("Error counting euclidean norm")));
    }
}
