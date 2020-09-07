package pl.kania.etd.io.serialization;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilenameProvider {

    public static String get() {
        return "CurrentState_" + DateTimeFormatter.ofPattern("yy-MM-dd_hh_mm").format(LocalDateTime.now()) + ".txt";
    }
}
