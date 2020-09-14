package pl.kania.etd.analysis.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import pl.kania.etd.EmergingTopicDetectionApplication;
import pl.kania.etd.author.Author;
import pl.kania.etd.author.AuthoritySetter;
import pl.kania.etd.author.Authors;
import pl.kania.etd.debug.MemoryService;
import pl.kania.etd.io.CsvReader;
import pl.kania.etd.io.CsvReaderResult;
import pl.kania.etd.io.FileOutputProvider;

@Slf4j
@SpringBootApplication(scanBasePackages = "pl.kania")
public class AuthorsToFileApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(EmergingTopicDetectionApplication.class, args);

        Environment environment = ctx.getBean(Environment.class);
        String pathToDataset = environment.getProperty("pl.kania.path.dataset");

        FileOutputProvider fop = ctx.getBean(FileOutputProvider.class);
        CsvReader reader = ctx.getBean(CsvReader.class);

        AuthorsWriter aw = new AuthorsWriter(fop);

        Authors authors = new Authors();
        reader.readFile(authors, pathToDataset);
        MemoryService.saveAndPrintCurrentFreeMemory();

        AuthoritySetter.setForAllAuthors(authors, true);
        authors.getAllAuthors().forEach(author -> {
            aw.appendText(author.getUsername() + "," + author.getAuthority());
        });

        String filename = aw.write();
        log.info("Saved authors to file: " + filename);
    }
}
