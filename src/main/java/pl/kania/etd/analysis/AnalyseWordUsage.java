package pl.kania.etd.analysis;

import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.periods.TimePeriod;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class AnalyseWordUsage {

    private final FileOutputProvider fop;

    public AnalyseWordUsage(FileOutputProvider fop) {
        this.fop = fop;
    }

    public void analyse(List<TimePeriod> periods) {
//        new WordUsageInExistingPeriods("covid", periods, fop).write();
//        new WordUsageInExistingPeriods("floyd", periods, fop).write();
//
//        new WordUsageInDuration("covid", periods, ChronoUnit.HOURS.getDuration(), fop).write();
//        new WordUsageInDuration("floyd", periods, ChronoUnit.HOURS.getDuration(), fop).write();

//        WordUsageInDuration wuid = new WordUsageInDuration(Set.of("losses", "rip", "tremendous", "suffering"), periods, ChronoUnit.HOURS.getDuration(), fop);
//        wuid.write();

//        wuid.setWords(Set.of("disproportionately", "streets", "ahmaud"));
//        wuid.write();

//        WordUsageInDurationPercentage w = new WordUsageInDurationPercentage(Set.of("disproportionately", "streets", "ahmaud"), periods, ChronoUnit.HOURS.getDuration(), fop);
        WordUsageInDurationPercentage w = new WordUsageInDurationPercentage(new HashSet<>(), periods, ChronoUnit.HOURS.getDuration(), fop);
//        w.setWords(Set.of("coronavirus", "death", "people"));
        w.setWords(Set.of("blm", "blacklivesmatter"));

        w.write();

//        w.setWords(Set.of("covid", "death", "people"));
//        w.write();
//
//        w.setWords(Set.of("coronavirus", "death", "die", "person"));
//        w.write();

//        w.setWords(Set.of("epstein", "jeffrey", "pedophile"));
//        w.write();

//        w.setWords(Set.of("covid", "covid-19", "coronavirus"));
//        w.write();
    }
}
