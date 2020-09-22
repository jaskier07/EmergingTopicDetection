package pl.kania.etd.analysis;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.periods.TimePeriod;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Analyses how frequently were words used in dataset.
 */
@Slf4j
public class AnalyseWordUsage {

    private final FileOutputProvider fop;

    public AnalyseWordUsage(FileOutputProvider fop) {
        this.fop = fop;
    }

    @SneakyThrows
    public void analyse(List<TimePeriod> periods) {
        new WordUsageInExistingPeriods("covid", periods, fop).write();

        new WordUsageInDuration("covid", periods, ChronoUnit.HOURS.getDuration(), fop).write();

        WordUsageInDurationPercentage w = new WordUsageInDurationPercentage(new HashSet<>(), periods, ChronoUnit.HOURS.getDuration(), fop);
        w.setWords(Set.of("exposed", "hair", "hairstylist", "clients", "salon", "expose"));
        w.write();
    }
}
