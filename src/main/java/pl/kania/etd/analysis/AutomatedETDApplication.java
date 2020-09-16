package pl.kania.etd.analysis;

import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import pl.kania.etd.author.AuthoritySetter;
import pl.kania.etd.author.Authors;
import pl.kania.etd.content.Topic;
import pl.kania.etd.debug.Counter;
import pl.kania.etd.debug.MemoryService;
import pl.kania.etd.debug.TimeDifferenceCounter;
import pl.kania.etd.energy.AverageCounter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication(scanBasePackages = "pl.kania")
public class AutomatedETDApplication {

    public static void main(String[] args) {
        TimeDifferenceCounter overallTdc = new TimeDifferenceCounter();
        overallTdc.start();

        ConfigurableApplicationContext ctx = SpringApplication.run(pl.kania.etd.EmergingTopicDetectionApplication.class, args);
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
        ResultsWriter rw = new ResultsWriter(ctx.getBean(FileOutputProvider.class));

        List<Long> executionTimes = new ArrayList<>();
        for (TimePeriod period : periods) {
            TimeDifferenceCounter tdc = new TimeDifferenceCounter();
            tdc.start();
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

            GraphFilter.filterOutGraphsSmallerThan(minClusterSize, sortedTopics);
            GraphFilter.cropGraphsBiggerThan(maxClusterSize, sortedTopics, period.getWordStatistics());

            Counter ctr = new Counter();
            sortedTopics.stream().limit(10).forEach(topic -> {
                rw.append(period.getStartTime() + "," + period.getEndTime() + "," + period.getIndex() + "," +
                        topic.toString(period) + "\n");
                ctr.increment();
            });
            tdc.stop();
            executionTimes.add(tdc.getDifferenceInMillis());

            MemoryService.saveAndPrintCurrentFreeMemory();
            period.freeCorrelation();
        }

        overallTdc.stop();
        log.info("Overall: " + overallTdc.getDifference() + " seconds");

        List<Double> doubleExecutionTime = executionTimes.stream().map(Long::doubleValue).collect(Collectors.toList());
        log.info("Average: " + AverageCounter.count(doubleExecutionTime) / 1000 + " seconds");

        log.info("Execution times: " + doubleExecutionTime.stream().map(Object::toString).collect(Collectors.joining(", ")));
        rw.write();
    }
}

