package pl.kania.etd.analysis;

import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.periods.TimePeriod;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class AnalyseWordUsage {

    private final FileOutputProvider fop;

    public AnalyseWordUsage(FileOutputProvider fop) {
        this.fop = fop;
    }

    public void analyse(List<TimePeriod> periods) {
        new WordUsageInExistingPeriods("covid", periods, fop).write();
        new WordUsageInExistingPeriods("floyd", periods, fop).write();

        new WordUsageInDuration("covid", periods, ChronoUnit.HOURS.getDuration(), fop).write();
        new WordUsageInDuration("floyd", periods, ChronoUnit.HOURS.getDuration(), fop).write();
    }
}
