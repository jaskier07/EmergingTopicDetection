package pl.kania.etd;

import org.jgrapht.Graph;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import pl.kania.etd.author.AuthoritySetter;
import pl.kania.etd.author.Authors;
import pl.kania.etd.energy.EmergingWordSetter;
import pl.kania.etd.energy.EnergyCounter;
import pl.kania.etd.energy.NutritionCounter;
import pl.kania.etd.graph.AdaptiveGraphEdgesCutOff;
import pl.kania.etd.graph.CorrelationVectorCounter;
import pl.kania.etd.graph.EdgeValue;
import pl.kania.etd.graph.GraphGenerator;
import pl.kania.etd.graph.scc.StronglyConnectedComponentsFinder;
import pl.kania.etd.graph.scc.StronglyConnectedComponentsWithEmergingTweetsFinder;
import pl.kania.etd.io.CsvReader;
import pl.kania.etd.io.CsvReaderResult;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.TimePeriodGenerator;
import pl.kania.etd.periods.TimePeriodInTweetsSetter;
import pl.kania.etd.periods.TimePeriods;

import java.util.List;

@SpringBootApplication
public class
EmergingTopicDetectionApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(EmergingTopicDetectionApplication.class, args);

        Environment environment = ctx.getBean(Environment.class);
        int numPreviousPeriods = Integer.parseInt(environment.getProperty("pl.kania.num-previous-periods"));
        double thresholdEnergy = Double.parseDouble(environment.getProperty("pl.kania.emerging-terms-drop"));
        String pathToDataset = environment.getProperty("pl.kania.path.dataset");

        CsvReader reader = ctx.getBean(CsvReader.class);
        CsvReaderResult csvReaderResult = reader.readFile(pathToDataset);

        List<TimePeriod> periods = TimePeriodGenerator.generate(csvReaderResult.getFirstTweetDate(), csvReaderResult.getLastTweetDate(), environment);
        TimePeriods.getInstance().addPeriods(periods);
        TimePeriodInTweetsSetter.setTimePeriod(csvReaderResult.getTweetSet());

        AuthoritySetter.setForAllAuthors();
        periods = periods.subList(periods.size() - numPreviousPeriods, periods.size());
        periods.forEach(NutritionCounter::countNutritionInPeriod);

        for (int periodIndex = periods.size() - 1; periodIndex >= 0; periodIndex--) {
            EnergyCounter.count(periods, periodIndex, numPreviousPeriods);
        }
        EmergingWordSetter.setBasedOnThreshold(thresholdEnergy);

        TimePeriod period = periods.get(periods.size() - 1); // or pre-last and last
        periods.clear();
        Authors.getInstance().saveMemory();
        System.gc();

        CorrelationVectorCounter.countCorrelationAndFillWords(period);
        period.saveMemory();
        System.gc();

        period.setCorrelationGraph(GraphGenerator.generate(period.getWordStatistics()));
        AdaptiveGraphEdgesCutOff.perform(period.getCorrelationGraph());
        List<Graph<String, EdgeValue>> sccGraphs = StronglyConnectedComponentsFinder.find(period.getCorrelationGraph());
        StronglyConnectedComponentsWithEmergingTweetsFinder.find(period.getEmergingWords(), sccGraphs);
    }
}
