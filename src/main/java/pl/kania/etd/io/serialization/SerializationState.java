package pl.kania.etd.io.serialization;

import lombok.Builder;
import lombok.Value;
import pl.kania.etd.author.Author;
import pl.kania.etd.periods.TimePeriod;

import java.io.Serializable;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Value
public class SerializationState implements Serializable {
    List<TimePeriod> periods;
    int minClusterSize;
    int maxClusterSize;
    int chosenPeriodIndex;
}
