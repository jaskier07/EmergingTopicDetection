package pl.kania.etd.periods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimePeriodGenerator {

    public static List<TimePeriod> generate(LocalDateTime start, LocalDateTime end, Environment environment) {
        return generate(start, end, environment, 0);
    }

    public static List<TimePeriod> generate(LocalDateTime start, LocalDateTime end, Environment environment, int startIndex) {
        long periodStep = Long.parseLong(environment.getProperty("pl.kania.period-duration"));
        int chronoUnitOrdinal = Integer.parseInt(environment.getProperty("pl.kania.period-duration.chrono-unit-ordinal"));
        Duration periodDuration = Duration.of(periodStep, ChronoUnit.values()[chronoUnitOrdinal]);
        return generate(start, end, periodDuration, startIndex);
    }

    public static List<TimePeriod> generate(LocalDateTime start, LocalDateTime end, Duration periodDuration) {
        return generate(start, end, periodDuration, 0);
    }

    public static List<TimePeriod> generate(LocalDateTime start, LocalDateTime end, Duration periodDuration, int startIndex) {
        List<TimePeriod> periods = new ArrayList<>();

        LocalDateTime periodStart = start;
        LocalDateTime periodEnd;
        int index = startIndex;

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
