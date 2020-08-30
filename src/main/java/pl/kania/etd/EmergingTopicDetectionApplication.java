package pl.kania.etd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import pl.kania.etd.io.CsvReader;
import pl.kania.etd.io.CsvReaderResult;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.TimePeriodGenerator;
import pl.kania.etd.periods.TimePeriodInTweetsSetter;
import pl.kania.etd.periods.TimePeriods;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootApplication
public class
EmergingTopicDetectionApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(EmergingTopicDetectionApplication.class, args);
		Environment environment = ctx.getBean(Environment.class);

		CsvReader reader = ctx.getBean(CsvReader.class);
		CsvReaderResult csvReaderResult = reader.readFile(environment.getProperty("pl.kania.path.dataset"));

		long periodStep = Long.parseLong(environment.getProperty("pl.kania.period-duration"));
		int chronoUnitOrdinal = Integer.parseInt(environment.getProperty("pl.kania.period-duration.chrono-unit-ordinal"));
		Duration periodDuration = Duration.of(periodStep, ChronoUnit.values()[chronoUnitOrdinal]);

		List<TimePeriod> periods = TimePeriodGenerator.generate(csvReaderResult.getFirstTweetDate(), csvReaderResult.getLastTweetDate(), periodDuration);
		TimePeriods.getInstance().addPeriods(periods);
		TimePeriodInTweetsSetter.setTimePeriod(csvReaderResult.getTweetSet());

	}

}
