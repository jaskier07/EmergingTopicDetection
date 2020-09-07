package pl.kania.etd.io.serialization;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.WordStatistics;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class SerializationService {

    public void serializeCurrentState(@NonNull SerializationState state, String filename) {
        log.info("Saving current state");

        try (FileOutputStream fo = new FileOutputStream(filename);
             ObjectOutputStream os = new ObjectOutputStream(fo)) {
            os.writeObject(state);
            os.flush();
            log.info("Saved");
        } catch (Exception e) {
            log.error("Error during serialization", e);
        }
    }

    public SerializationState deserializeCurrentState(String filename) {
        log.info("Loading current state");

        try (FileInputStream is = new FileInputStream(filename);
             ObjectInputStream os = new ObjectInputStream(is)) {
            SerializationState state = (SerializationState)os.readObject();

            log.info("Loaded");
            return state;
        } catch (Exception e) {
            log.error("Error during deserialization", e);
            return null;
        }
    }
}
