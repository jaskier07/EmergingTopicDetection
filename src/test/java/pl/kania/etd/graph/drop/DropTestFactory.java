package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DropTestFactory {

    static List<Double> getValues() {
        return Arrays.asList(35., 55., 47., 30., 10., 0.);
    }

    static List<Drop> getDrops() {
        return Arrays.asList(
                new Drop(1, 8),
                new Drop(2, 12),
                new Drop(3, 5),
                new Drop(4, 20),
                new Drop(5, 10)
        );
    }

    static List<Drop> getDropsBeforeMaximumDrop() {
        return Arrays.asList(
                new Drop(1, 8),
                new Drop(2, 12),
                new Drop(3, 5)
        );
    }


    static double getAverageDrop() {
        return 8.33;
    }

    static int getCriticalDropIndexInclusive() {
        return 2;
    }

    static List<AdaptiveCutOffTest.Value> getTestValues() {
        return Arrays.asList(
                new AdaptiveCutOffTest.Value("1", 35.),
                new AdaptiveCutOffTest.Value("2", 55.),
                new AdaptiveCutOffTest.Value("3", 47.),
                new AdaptiveCutOffTest.Value("4", 30.),
                new AdaptiveCutOffTest.Value("5", 10.),
                new AdaptiveCutOffTest.Value("1", 0.)
        );
    }
}
