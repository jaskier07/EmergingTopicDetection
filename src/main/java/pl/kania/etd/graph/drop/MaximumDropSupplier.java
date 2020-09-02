package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MaximumDropSupplier {

    public static int getMaximumDropIndexInclusive(List<Drop> drops) {
        double max = Double.MIN_VALUE;
        int maxDropIndex = 0;

        for (Drop drop : drops) {
            if (max < drop.getValue()) {
                max = drop.getValue();
                maxDropIndex = drop.getLastIndex() - 1;
            }
        }

        return maxDropIndex;
    }
}
