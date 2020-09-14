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

        Authors authors = new Authors();
        CsvReader reader = ctx.getBean(CsvReader.class);
        CsvReaderResult csvReaderResult = reader.readFile(authors, pathToDataset, 0, 500_000);
        MemoryService.saveAndPrintCurrentFreeMemory();

        List<TimePeriod> periods = TimePeriodGenerator.generate(csvReaderResult.getFirstTweetDate(), csvReaderResult.getLastTweetDate(), environment);
        MemoryService.printCurrentFreeMemory();
        TimePeriodInTweetsSetter.setTimePeriod(periods, csvReaderResult.getTweetSet());
        periods.forEach(TimePeriod::dropRareWords);
        MemoryService.saveAndPrintCurrentFreeMemory();

        AuthoritySetter.setForAllAuthors(authors, authorityAugmented);
        authors.printMostImportantAuthors();
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

        authors.saveMemory();
        tdc.stop();

        int periodIndex = new IntReader().read("Provide period index to search for popular topics");
        int periodsToDrop = new IntReader().read("How many periods to drop before?");
        int dropAfterSelected = new IntReader().read("Drop periods after selected? 1/0");
        while (periodIndex != -1) {
            tdc.start();

            int finalPeriodIndex = periodIndex;
            TimePeriod period = periods.stream().filter(f -> f.getIndex() == finalPeriodIndex).findFirst().get();
            log.info("Preserved period: " + period.toString());

            periods.stream().sequential().limit(periodsToDrop).forEach(TimePeriod::freePeriod);
            if (dropAfterSelected == 1) {
                periods.stream().filter(p -> p.getIndex() > period.getIndex()).forEach(p -> p.setPeriodToFree(true));
            }
            periods.removeIf(TimePeriod::isPeriodToFree);
            MemoryService.saveAndPrintCurrentFreeMemory();

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

            ETDLogger.printAllPeriods(periods);
            periodIndex = new IntReader().read("Provide period index to search for popular topics");
            periodsToDrop = new IntReader().read("How many periods to drop?");
            dropAfterSelected = new IntReader().read("Drop periods after selected? 1/0");
        }
    }
}
