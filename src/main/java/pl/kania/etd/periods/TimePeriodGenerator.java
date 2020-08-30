package pl.kania.etd.periods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimePeriodGenerator {

    public static List<TimePeriod> generate(LocalDateTime start, LocalDateTime end, Duration periodDuration) {
        List<TimePeriod> periods = new ArrayList<>();

        LocalDateTime periodStart = start;
        LocalDateTime periodEnd;
        int index = 0;

        while (periodStart.isBefore(end)) {
            periodEnd = periodStart.plus(periodDuration);
            TimePeriod newPeriod = new TimePeriod(index++, periodStart, periodEnd);
            periods.add(newPeriod);
            periodStart = periodEnd;

            log.debug("Period created: " + newPeriod.toString());
        }

        log.info("Created " + periods.size() + " periods.");
        return periods;
    }
}
