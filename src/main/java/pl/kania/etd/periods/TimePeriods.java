package pl.kania.etd.periods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimePeriods {

    private static TimePeriods instance;
    private static List<TimePeriod> periods = new ArrayList<>();

    public static TimePeriods getInstance() {
        if (instance == null) {
            instance = new TimePeriods();
        }
        return instance;
    }

    public void addPeriod(TimePeriod period) {
        periods.add(period);
    }

    public void addPeriods(Collection<TimePeriod> period) {
        periods.addAll(period);
    }

    public TimePeriod getPeriodFromDate(LocalDateTime date) {
        return periods.stream()
                .filter(p -> (date.isBefore(p.getEndTime()) && date.isAfter(p.getStartTime()) || date.isEqual(p.getStartTime())))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Period for date " + date + " not found! Available periods: " + getPeriodRanges()));
    }

    public List<TimePeriod> getAllPeriods() {
        return new ArrayList<>(periods);
    }

    private String getPeriodRanges() {
        return periods.stream()
                .map(TimePeriod::toString)
                .collect(Collectors.joining());
    }
}
