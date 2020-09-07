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
import pl.kania.etd.debug.Counter;
import pl.kania.etd.debug.NumberFormatter;
import pl.kania.etd.debug.ProgressLogger;
import pl.kania.etd.energy.EmergingWordSetter;
import pl.kania.etd.energy.EnergyCounter;
import pl.kania.etd.energy.NutritionCounter;
import pl.kania.etd.graph.*;
import pl.kania.etd.graph.scc.StronglyConnectedComponentsFinder;
import pl.kania.etd.graph.scc.StronglyConnectedComponentsWithEmergingTweetsFinder;
import pl.kania.etd.io.CsvReader;
import pl.kania.etd.io.CsvReaderResult;
import pl.kania.etd.io.serialization.FilenameProvider;
import pl.kania.etd.io.serialization.SerializationService;
import pl.kania.etd.io.serialization.SerializationState;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.TimePeriodGenerator;
import pl.kania.etd.periods.TimePeriodInTweetsSetter;
import pl.kania.etd.periods.TimePeriods;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootApplication
public class
EmergingTopicDetectionApplication {

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
        boolean loadState = Boolean.parseBoolean(environment.getProperty("pl.kania.load-state"));
        String pathToDeserialization = environment.getProperty("pl.kania.path.deserialization");

        List<TimePeriod> periods;
        int chosenPeriodIndex;

        if (loadState) {
            SerializationState state = new SerializationService().deserializeCurrentState(pathToDeserialization);
            periods = state.getPeriods();
            chosenPeriodIndex = state.getChosenPeriodIndex();
        } else {
            CsvReader reader = ctx.getBean(CsvReader.class);
            CsvReaderResult csvReaderResult = reader.readFile(pathToDataset);

            periods = TimePeriodGenerator.generate(csvReaderResult.getFirstTweetDate(), csvReaderResult.getLastTweetDate(), environment);
            TimePeriods.getInstance().addPeriods(periods);
            TimePeriodInTweetsSetter.setTimePeriod(csvReaderResult.getTweetSet());

            AuthoritySetter.setForAllAuthors(authorityAugmented);
            Authors.getInstance().printMostImportantAuthors();
            periods.forEach(NutritionCounter::countAndSetNutritionInPeriod);

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

            Authors.getInstance().saveMemory();
            periods.forEach(TimePeriod::dropRareWords);

//            new SerializationService().serializeCurrentState(new SerializationState(periods, minClusterSize, maxClusterSize, periods.size() - 1 - 1 - 1), FilenameProvider.get());
            chosenPeriodIndex = periods.size() - 1 - 1;
        }

        TimePeriod period = periods.get(chosenPeriodIndex); // or pre-last and last
        log.info("Preserved period: " + period.toString());
        periods.clear();
        System.gc();

        CorrelationVectorCounter.countCorrelationAndFillWords(period);
        period.saveMemory();
        System.gc();

        period.setCorrelationGraph(GraphGenerator.generate(period.getWordStatistics()));
        AdaptiveGraphEdgesCutOff.perform(period.getCorrelationGraph());
        List<Graph<String, EdgeValue>> sccGraphs = StronglyConnectedComponentsFinder.find(period.getCorrelationGraph());
        Set<Graph<String, EdgeValue>> topics = StronglyConnectedComponentsWithEmergingTweetsFinder.find(period.getEmergingWords(), sccGraphs);
        List<Topic> sortedTopics = GraphSorter.sortByEnergy(topics, period.getWordStatistics());

        sortedTopics.forEach(topic -> AdaptiveGraphEdgesCutOff.perform(topic.getGraph()));
        new ProgressLogger().done();
        GraphFilter.filterOutGraphsSmallerThan(minClusterSize, sortedTopics);
        GraphFilter.cropGraphsBiggerThan(maxClusterSize, sortedTopics, period.getWordStatistics());

        log.warn("POPULAR TOPICS:");
        Counter ctr = new Counter();
        sortedTopics.forEach(topic -> {
            log.info("#" + ctr.getValue() + topic.toString(period));
            ctr.increment();
        });
    }
}
