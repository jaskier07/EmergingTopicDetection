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

    public static <T extends Durable>T getPeriodFromDate(final List<T> periods, final LocalDateTime date) {
        return periods.stream()
                .filter(p -> (date.isBefore(p.getEndTime()) && date.isAfter(p.getStartTime()) || date.isEqual(p.getStartTime())))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Period for date " + date + " not found! Available periods: " + getPeriodRanges(periods)));
    }

    private static <T extends Durable>String getPeriodRanges(final List<T> periods) {
        return periods.stream()
                .map(T::toString)
                .collect(Collectors.joining());
    }
}
