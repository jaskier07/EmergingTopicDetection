package pl.kania.etd.periods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.kania.etd.SavingMemory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimePeriods {

    public static TimePeriod getPeriodFromDate(final List<TimePeriod> periods, final LocalDateTime date) {
        return periods.stream()
                .filter(p -> (date.isBefore(p.getEndTime()) && date.isAfter(p.getStartTime()) || date.isEqual(p.getStartTime())))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Period for date " + date + " not found! Available periods: " + getPeriodRanges(periods)));
    }

    private static String getPeriodRanges(final List<TimePeriod> periods) {
        return periods.stream()
                .map(TimePeriod::toString)
                .collect(Collectors.joining());
    }
}
