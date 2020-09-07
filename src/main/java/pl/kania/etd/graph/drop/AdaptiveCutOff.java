package pl.kania.etd.graph.drop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdaptiveCutOff {

    public static <T extends HasValue<T>> void performActionForElementsBeforeCriticalDrop(Collection<T> values, Consumer<List<T>> action) {
        if (values.size() < 2) {
            return;
        }

        List<Drop<T>> drops = DropsProvider.getDrops(values);
        int maximumDropIndexInclusive = MaximumDropSupplier.getMaximumDropIndexInclusive(drops);
        List<Drop<T>> dropsInRange = getDropsInRange(drops, maximumDropIndexInclusive);
        double averageDrop = AverageDropSupplier.getAverageDropInclusive(dropsInRange);
        int firstElementIndexBeforeCritical = CriticalDropSupplier.getFirstElementIndexBeforeCriticalDrop(dropsInRange, averageDrop);
        List<Drop<T>> dropsBeforeCritical = dropsInRange.subList(0, Math.min(dropsInRange.size(), firstElementIndexBeforeCritical + 1));
        action.accept(mapToType(dropsBeforeCritical));
    }

    public static <T extends HasValue<T>> void performActionForElementsAfterCriticalDrop(Collection<T> values, Consumer<List<T>> action) {
        if (values.isEmpty()) {
            return;
        } else if (values.size() == 1) {
            action.accept(new ArrayList<>(values));
            return;
        }

        List<T> sortedValues = values.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        List<Drop<T>> drops = DropsProvider.getDrops(values);
        int maximumDropIndexInclusive = MaximumDropSupplier.getMaximumDropIndexInclusive(drops);
        List<Drop<T>> dropsInRange = getDropsInRange(drops, maximumDropIndexInclusive);
        double averageDrop = AverageDropSupplier.getAverageDropInclusive(dropsInRange);
        List<Drop<T>> dropsAfterCritical = drops.subList(CriticalDropSupplier.getFirstElementIndexBeforeCriticalDrop(dropsInRange, averageDrop) + 1, drops.size());
        List<T> elements = mapToType(dropsAfterCritical);
        elements.add(sortedValues.get(sortedValues.size() - 1));
        action.accept(elements);
    }

    private static <T extends HasValue<T>> List<T> mapToType(List<Drop<T>> dropsBeforeCritical) {
        return dropsBeforeCritical.stream()
                .map(Drop::getPreviousElement)
                .collect(Collectors.toList());
    }

    private static <T extends HasValue<T>> List<Drop<T>> getDropsInRange(List<Drop<T>> drops, int maximumDropIndexInclusive) {
        if (maximumDropIndexInclusive == 0) {
            return drops.subList(0, 1);
        }
        return drops.subList(0, maximumDropIndexInclusive);
    }

}
