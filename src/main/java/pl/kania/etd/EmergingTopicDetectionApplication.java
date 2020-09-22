package pl.kania.etd;

import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import pl.kania.etd.author.AuthoritySetter;
import pl.kania.etd.author.Authors;
import pl.kania.etd.content.Topic;
import pl.kania.etd.debug.*;
import pl.kania.etd.energy.EmergingWordSetter;
import pl.kania.etd.energy.EnergyCounter;
import pl.kania.etd.energy.NutritionCounter;
import pl.kania.etd.graph.*;
import pl.kania.etd.graph.scc.StronglyConnectedComponentsFinder;
import pl.kania.etd.graph.scc.StronglyConnectedComponentsWithEmergingTweetsFinder;
import pl.kania.etd.io.CsvReader;
import pl.kania.etd.io.CsvReaderResult;
import pl.kania.etd.io.IntReader;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.TimePeriodGenerator;
import pl.kania.etd.periods.TimePeriodInTweetsSetter;

import java.util.List;
import java.util.Set;

/**
 * App detecting popular topics in period specified during app runtime.
 *
 * First counts energy for all tweets in all time periods, than in a loop asks user to provide
 * period index. In this period algorithm will search for popular topics.
 *
 * App is memory consuming. Run it with -Xmx8192m JVM parameter while using provided coronavirus
 * tweet dataset.
 */
@Slf4j
@SpringBootApplication
public class EmergingTopicDetectionApplication {

    public static void main(String[] args) {
        TimeDifferenceCounter tdc = new TimeDifferenceCounter();
        tdc.start();

        ConfigurableApplicationContext ctx = SpringApplication.run(EmergingTopicDetectionApplication.class, args);
        Environment environment = ctx.getBean(Environment.class);
        int numPreviousPeriods = Integer.parseInt(environment.getProperty("pl.kania.num-previous-periods"));
        double thresholdEnergy = Double.parseDouble(environment.getProperty("pl.kania.threshold-energy"));
        String pathToDataset = environment.getProperty("pl.kania.path.dataset");
        int minClusterSize = Integer.parseInt(environment.getProperty("pl.kania.min-cluster-size"));
        int maxClusterSize = Integer.parseInt(environment.getProperty("pl.kania.max-cluster-size"));
        boolean autoThreshold = Boolean.parseBoolean(environment.getProperty("pl.kania.threshold-energy-auto"));
        boolean authorityAugmented = Boolean.parseBoolean(environment.getProperty("pl.kania.authority-augmented"));

        CsvReader reader = ctx.getBean(CsvReader.class);
        CsvReaderResult csvReaderResult = reader.readFile(pathToDataset);
        MemoryService.saveAndPrintCurrentFreeMemory();

        List<TimePeriod> periods = TimePeriodGenerator.generate(csvReaderResult.getFirstTweetDate(), csvReaderResult.getLastTweetDate(), environment);
        MemoryService.printCurrentFreeMemory();
        TimePeriodInTweetsSetter.setTimePeriod(periods, csvReaderResult.getTweetSet());
        periods.forEach(TimePeriod::dropRareWords);
        MemoryService.saveAndPrintCurrentFreeMemory();

        AuthoritySetter.setForAllAuthors(authorityAugmented);
        Authors.getInstance().printMostImportantAuthors();
        MemoryService.saveAndPrintCurrentFreeMemory();

        periods.forEach(NutritionCounter::countAndSetNutritionInPeriod);
        MemoryService.saveAndPrintCurrentFreeMemory();

        for (int periodIndex = 0; periodIndex < periods.size(); periodIndex++) {
            EnergyCounter.countAndSet(periods, periodIndex, numPreviousPeriods);
        }
        periods.forEach(period -> {
            if (autoThreshold) {
                EmergingWordSetter.setBasedOnCriticalDrop(period.getWordStatistics().values(), period.getIndex());
            } else {
                EmergingWordSetter.setBasedOnThreshold(period, thresholdEnergy);
            }
        });

        Authors.getInstance().saveMemory();
        tdc.stop();



        int periodIndex = new IntReader().read("Provide period index to search for popular topics");
        while (periodIndex != -1) {
            tdc.start();

            int finalPeriodIndex = periodIndex;
            TimePeriod period = periods.stream().filter(f -> f.getIndex() == finalPeriodIndex).findFirst().get();
            log.info("Preserved period: " + period.toString());

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

            log.warn("POPULAR TOPICS:");
            Counter ctr = new Counter();
            sortedTopics.stream().limit(30).forEach(topic -> {
                log.info("#" + ctr.getValue() + topic.toString(period));
                ctr.increment();
            });
            tdc.stop();
            log.info(tdc.getDifference());

            MemoryService.saveAndPrintCurrentFreeMemory();
            period.freeCorrelation();

            periodIndex = new IntReader().read("Provide period index to search for popular topics");
        }
    }
}
