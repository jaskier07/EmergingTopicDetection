package pl.kania.etd.debug;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.periods.TimePeriod;

import java.time.Period;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ETDLogger {

    public static void printAllPeriods(List<TimePeriod> periods) {
        periods.forEach(period -> {
           log.info(period.toString());
        });
    }

}
