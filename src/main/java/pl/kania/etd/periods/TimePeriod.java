package pl.kania.etd.periods;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class TimePeriod {

    private final String id;
    private final int number;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TimePeriod(int index, LocalDateTime startTime, LocalDateTime endTime) {
        this.number = index;
        this.id = UUID.randomUUID().toString();
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "[" + (startTime == null ? "?" : startTime.toString()) + ", " + (endTime == null ? "?" : endTime.toString()) + "]";
    }
}
