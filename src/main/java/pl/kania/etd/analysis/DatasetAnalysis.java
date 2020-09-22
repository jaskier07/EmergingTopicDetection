package pl.kania.etd.analysis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import pl.kania.etd.EmergingTopicDetectionApplication;
import pl.kania.etd.debug.MemoryService;
import pl.kania.etd.io.CsvReader;
import pl.kania.etd.io.CsvReaderResult;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.TimePeriodGenerator;
import pl.kania.etd.periods.TimePeriodInTweetsSetter;

import java.util.List;
import java.util.Set;

/**
 * App printing statistics about a dataset.
 *
 * Results are saved in multiple files.
 *
 * App is memory consuming. Run it with -Xmx8192m JVM parameter while using provided coronavirus
 * tweet dataset.
 */
@Slf4j
@SpringBootApplication
public class DatasetAnalysis {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(EmergingTopicDetectionApplication.class, args);

        Environment environment = ctx.getBean(Environment.class);
        FileOutputProvider fop = ctx.getBean(FileOutputProvider.class);
        String pathToDataset = environment.getProperty("pl.kania.path.dataset-analysis");

        CsvReader reader = ctx.getBean(CsvReader.class);
        CsvReaderResult csvReaderResult = reader.readFile(pathToDataset);

        List<TimePeriod> periods = TimePeriodGenerator.generate(csvReaderResult.getFirstTweetDate(), csvReaderResult.getLastTweetDate(), environment);
        TimePeriodInTweetsSetter.setTimePeriod(periods, csvReaderResult.getTweetSet());
        MemoryService.printCurrentFreeMemory();

        new TweetsInPeriodCounter(fop, periods).write();
        new AnalyseWordUsage(fop).analyse(periods);
        new AnalyseAuthorsTweets(fop, periods, Set.of("realDonaldTrump", "CNN", "WhiteHouse", "ArvindKejriwal", "premierleague"));
        new MostPopularAuthors(fop).write();
        new MostPopularWords(fop, periods).write();
        new StatisticsInDataset(fop, periods).write();
    }
}
