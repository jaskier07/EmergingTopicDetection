package pl.kania.etd.energy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.debug.Counter;
import pl.kania.etd.debug.PercentageFormatter;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.TimePeriods;
import pl.kania.etd.periods.WordStatistics;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmergingWordSetter {

    public static void setBasedOnThreshold(double threshold) {
        TimePeriods.getInstance().getAllPeriods().forEach(period -> {
            double dropInPeriod = threshold * sumEnergy(period) / allWordsInPeriod(period);

            Counter ctr = new Counter();
            period.getWordStatistics().values().forEach(word -> {
                if (word.getEnergy() > dropInPeriod) {
                    word.setEmerging(true);
                    ctr.increment();
                }
            });

            log.info("Period #" + period.getIndex() + ": drop(" + dropInPeriod + "), emergent %( " +
                    PercentageFormatter.format(ctr.getValue(), period.getWordStatistics().size()) + "), emergent(" + ctr.getValue() + ")");
        });
    }

    private static double sumEnergy(TimePeriod period) {
        return period.getWordStatistics().values().stream()
                .map(WordStatistics::getEnergy)
                .reduce(Double::sum)
                .orElseThrow(() -> new IllegalStateException("Error summing energy in period"));
    }

    private static int allWordsInPeriod(TimePeriod period) {
        return period.getWords().size();
    }
}
