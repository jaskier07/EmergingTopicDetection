package pl.kania.etd.analysis;

import pl.kania.etd.debug.NumberFormatter;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.periods.TimePeriod;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * Finds % of tweets containing specified words in every period.
 */
public class WordUsageInDurationPercentage extends WordUsageInDuration {
    public WordUsageInDurationPercentage(Set<String> words, List<TimePeriod> periods, Duration duration, FileOutputProvider fop) {
        super(words, periods, duration, fop);
    }

    public WordUsageInDurationPercentage(String word, List<TimePeriod> periods, Duration duration, FileOutputProvider fop) {
        super(word, periods, duration, fop);
    }

    @Override
    protected void writeRecord(FileWriter fw, WordPeriod p) throws IOException {
        fw.write(p.getStartTime() + "," + p .getEndTime() + "," + p.getTweets() + "," +
                NumberFormatter.formatPercentage(p.getTweets(), p.getAllTweets(), 5, false) +"\n");
    }

    @Override
    protected String getHeader() {
        return "start, end, tweets, tweets(%)\n";
    }

    @Override
    protected String getFilename() {
        return "Percentage" + super.getFilename();
    }
}
