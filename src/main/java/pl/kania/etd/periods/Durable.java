package pl.kania.etd.periods;

import java.time.LocalDateTime;

public interface Durable {

    LocalDateTime getEndTime();

    LocalDateTime getStartTime();
}
