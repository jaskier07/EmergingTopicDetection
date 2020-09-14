package pl.kania.etd.analysis.file;

import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import pl.kania.etd.EmergingTopicDetectionApplication;
import pl.kania.etd.author.Author;
import pl.kania.etd.author.AuthoritySetter;
import pl.kania.etd.author.Authors;
import pl.kania.etd.content.Topic;
import pl.kania.etd.content.Word;
import pl.kania.etd.debug.*;
import pl.kania.etd.energy.EmergingWordSetter;
import pl.kania.etd.energy.EnergyCounter;
import pl.kania.etd.energy.NutritionCounter;
import pl.kania.etd.graph.*;
import pl.kania.etd.graph.scc.StronglyConnectedComponentsFinder;
import pl.kania.etd.graph.scc.StronglyConnectedComponentsWithEmergingTweetsFinder;
import pl.kania.etd.io.CsvReader;
import pl.kania.etd.io.CsvReaderResult;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.TimePeriodGenerator;
import pl.kania.etd.periods.TimePeriodInTweetsSetter;
import pl.kania.etd.periods.WordStatistics;

import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication(scanBasePackages = "pl.kania")
public class EnergyToFileApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(EmergingTopicDetectionApplication.class, args);

        Environment environment = ctx.getBean(Environment.class);
        int numPreviousPeriods = Integer.parseInt(environment.getProperty("pl.kania.num-previous-periods"));
        double thresholdEnergy = Double.parseDouble(environment.getProperty("pl.kania.threshold-energy"));
        String pathToDataset = environment.getProperty("pl.kania.path.dataset");
        int minClusterSize = Integer.parseInt(environment.getProperty("pl.kania.min-cluster-size"));
        int maxClusterSize = Integer.parseInt(environment.getProperty("pl.kania.max-cluster-size"));
        boolean autoThreshold = Boolean.parseBoolean(environment.getProperty("pl.kania.threshold-energy-auto"));
        boolean authorityAugmented = Boolean.parseBoolean(environment.getProperty("pl.kania.authority-augmented"));
        int tweetBatchSize = Integer.parseInt(environment.getProperty("pl.kania.to-file-tweet-batch-size"));

        int startFromTweet = 0;
        int currentPeriodIndex = 0;
        int currentFindTopicsPeriodIndex = 0;

        FileOutputProvider fop = ctx.getBean(FileOutputProvider.class);
        CsvReader reader = ctx.getBean(CsvReader.class);

        Authors authors = new Authors();

        PeriodWriter pw = new PeriodWriter(fop);
        EnergyWriter ew = new EnergyWriter(fop);
        AuthorsWriter aw = new AuthorsWriter(fop);

        CsvReaderResult csvReaderResult = reader.readFile(authors, pathToDataset, startFromTweet, startFromTweet + tweetBatchSize);
        MemoryService.saveAndPrintCurrentFreeMemory();

        AuthoritySetter.setForAllAuthors(authors, authorityAugmented);
        authors.getAllAuthors().forEach(author -> {
            aw.appendText(author.getUsername() + "," + author.getAuthority());
        });

        MemoryService.saveAndPrintCurrentFreeMemory();

        List<TimePeriod> periods = TimePeriodGenerator.generate(csvReaderResult.getFirstTweetDate(),
                csvReaderResult.getLastTweetDate(), environment, currentFindTopicsPeriodIndex);

        MemoryService.printCurrentFreeMemory();
        TimePeriodInTweetsSetter.setTimePeriod(periods, csvReaderResult.getTweetSet());

        periods.forEach(TimePeriod::dropRareWords);
        MemoryService.saveAndPrintCurrentFreeMemory();

        periods.forEach(NutritionCounter::countAndSetNutritionInPeriod);
        MemoryService.saveAndPrintCurrentFreeMemory();

        for (int periodIndex = 0; periodIndex < periods.size(); periodIndex++) {
            EnergyCounter.countAndSet(periods, periodIndex, numPreviousPeriods);
        }
        periods.forEach(period -> {
            if (autoThreshold) {
                EmergingWordSetter.setBasedOnThreshold(period, thresholdEnergy);
            } else {
                EmergingWordSetter.setBasedOnCriticalDrop(period.getWordStatistics().values(), period.getIndex());
            }
        });

        periods.forEach(period -> {
            pw.appendText(period.getIndex() + "," + period.getStartTime() + "," + period.getEndTime() + "," + period.getTweetsCount() + "\n");
            period.getWordStatistics().values().forEach(word -> {
                ew.appendText(period.getIndex() + "," + word.getWord() + "," + word.getEnergy() + "," + word.getTweets() + "\n");
            });
            period.freePeriod();
        });

        periods.clear();
        pw.write();
        ew.write();

        MemoryService.saveAndPrintCurrentFreeMemory();

    }
}
