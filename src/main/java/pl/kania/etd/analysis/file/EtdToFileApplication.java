package pl.kania.etd.analysis.file;

import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import pl.kania.etd.EmergingTopicDetectionApplication;
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
public class EtdToFileApplication {

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

        int startFromTweet = 396331;
        int currentPeriodIndex = 120;
        int currentFindTopicsPeriodIndex = 72;

        FileOutputProvider fop = ctx.getBean(FileOutputProvider.class);
        CsvReader reader = ctx.getBean(CsvReader.class);
        ResultWriter writer = new ResultWriter(fop);

        CsvReaderResult csvReaderResult = reader.readFile(pathToDataset, startFromTweet, startFromTweet + tweetBatchSize);
        MemoryService.saveAndPrintCurrentFreeMemory();
        boolean stepOut = false;

//        while (!csvReaderResult.getTweetSet().isEmpty()) {
            AuthoritySetter.setForAllAuthors(authorityAugmented);
            Authors.getInstance().printMostImportantAuthors();
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

            // -1 because don't save last period, which will be too small
            for (int periodIndex = currentPeriodIndex - currentFindTopicsPeriodIndex; periodIndex < periods.size() - 1; periodIndex++) {
                TimePeriod period = periods.get(periodIndex);

                if (period.getTweetsCount() == 0) {
                    log.warn("Stepping out of loop");
                    stepOut = true;
                    break;
                }

                CorrelationVectorCounter.countCorrelationAndFillWords(period);
                MemoryService.saveAndPrintCurrentFreeMemory();

                period.setCorrelationGraph(GraphGenerator.generate(period.getWordStatistics()));
                AdaptiveGraphEdgesCutOff.perform(period.getCorrelationGraph());
                List<Graph<String, EdgeValue>> sccGraphs = StronglyConnectedComponentsFinder.find(period.getCorrelationGraph());
                Set<Graph<String, EdgeValue>> topics = StronglyConnectedComponentsWithEmergingTweetsFinder.find(period.getEmergingWords(), sccGraphs);

                sccGraphs.clear();
                MemoryService.saveAndPrintCurrentFreeMemory();
                List<Topic> sortedTopics = GraphSorter.sortByEnergy(topics, period.getWordStatistics());

                sortedTopics.forEach(topic -> AdaptiveGraphEdgesCutOff.perform(topic.getGraph()));
                new ProgressLogger().done();
                GraphFilter.filterOutGraphsSmallerThan(minClusterSize, sortedTopics);
                GraphFilter.cropGraphsBiggerThan(maxClusterSize, sortedTopics, period.getWordStatistics());

                for (Topic topic : sortedTopics.subList(0, Math.min(10, sortedTopics.size()))) {
                    writer.appendText(period.getIndex() + "," + period.getStartTime() + "," + period.getEndTime() + ","
                            + NumberFormatter.format(topic.getEnergy(), 5) + ","
                            + topic.getWordsSortedByEnergy(period)
                            .stream()
                            .map(WordStatistics::getWord)
                            .collect(Collectors.joining(",")) + "\n");
                }
                log.info("period:" + period.getIndex());

                MemoryService.saveAndPrintCurrentFreeMemory();
                period.freeCorrelation();
            }
//            if (stepOut) {
//                break;
//            }

            // start reading earlier, to get energy from previous periods
            int periodIndexToStartReadingFile = periods.size() - numPreviousPeriods - 1;
            int tweetNumToCountEnergyButNotToProcess = periods.subList(0, periodIndexToStartReadingFile).stream()
                    .map(TimePeriod::getTweetsCount)
                    .reduce(Integer::sum)
                    .orElse(0);

            // we start counting energy in this period
            currentPeriodIndex = periodIndexToStartReadingFile;
            // we start searching for topics in the same period in which we haven't processed
            currentFindTopicsPeriodIndex = periods.get(periods.size() - 1).getIndex();
            startFromTweet += tweetNumToCountEnergyButNotToProcess;

            periods.clear();
            MemoryService.saveAndPrintCurrentFreeMemory();

//            log.warn("===============================================> NEXT ROUND!");
//            csvReaderResult = reader.readFile(pathToDataset, startFromTweet, startFromTweet + tweetBatchSize);
//            MemoryService.saveAndPrintCurrentFreeMemory();
//        }

        writer.write();
    }
}
