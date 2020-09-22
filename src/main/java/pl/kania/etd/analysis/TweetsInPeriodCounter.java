package pl.kania.etd.analysis;

import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.io.SimpleWritable;
import pl.kania.etd.periods.TimePeriod;

import java.util.List;

/**
 * Finds how many tweets contained specified words in every period.
 */
public class TweetsInPeriodCounter extends SimpleWritable {

    public TweetsInPeriodCounter(FileOutputProvider fop, List<TimePeriod> periods) {
        super(fop);

        periods.forEach(period -> {
            append(period.getIndex() + "," + period.getStartTime() + "," + period.getEndTime() + "," + period.getTweets().size() + "\n");
        });
        Integer allTweets = periods.stream()
                .limit(periods.size() - 1)
                .map(p -> p.getTweets().size())
                .reduce(Integer::sum)
                .orElse(0);
        append("All tweets: " + allTweets + "\n");
    }

    @Override
    protected String getFilename() {
        return "PeriodStatistics";
    }

    @Override
    protected String getHeader() {
        return "index,from,to,tweets\n";
    }
}
