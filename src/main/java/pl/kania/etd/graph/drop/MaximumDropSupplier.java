package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MaximumDropSupplier {

    public static <T extends HasValue<T>>int getMaximumDropIndexInclusive(List<Drop<T>> drops) {
        double max = Double.MIN_VALUE;
        int maxDropIndex = 0;

        for (Drop<T> drop : drops) {
            if (max < drop.getValue()) {
                max = drop.getValue();
                maxDropIndex = drop.getDropIndex();
            }
        }

        return maxDropIndex;
    }
}
