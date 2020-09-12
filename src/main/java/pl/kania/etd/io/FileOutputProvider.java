package pl.kania.etd.io;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileOutputProvider {

    private final String pathToFolder;
    private final DateTimeFormatter dtf;

    public FileOutputProvider(@Autowired Environment environment) {
        this.pathToFolder = environment.getProperty("pl.kania.path.analysis");
        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
    }

    public Filepath get(String name) {
        String formattedDate = dtf.format(LocalDateTime.now());
        return new Filepath(formattedDate + "_" + name + ".csv", pathToFolder);
    }
}
