package pl.kania.etd.analysis;

import pl.kania.etd.author.Author;
import pl.kania.etd.author.Authors;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.io.SimpleWritable;
import pl.kania.etd.periods.TimePeriod;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Finds:
 * - number of tweets written per hour of the day,
 * - number of authors in a dataset,
 * - number of unique words in a dataset.
 */
public class StatisticsInDataset extends SimpleWritable {

    public StatisticsInDataset(FileOutputProvider fop, List<TimePeriod> periods) {
        super(fop);

        appendWordSetSize(periods);
        appendAuthorsSetSize();
        appendHourPublished(periods);
    }

    private void appendHourPublished(List<TimePeriod> periods) {
        Map<Integer, Integer> tweetsPerHours = IntStream.range(0, 24).boxed().collect(Collectors.toMap(Function.identity(), v -> 0));

        periods.forEach(period -> {
            period.getTweets().forEach(tweet -> {
                tweetsPerHours.merge(tweet.getCreatedAt().getHour(), 1, (val1, val2) -> val1 + 1);
            });
        });

        tweetsPerHours.forEach((hour, val) -> {
            append("Hour: " + hour + "," + val + "\n");
        });
    }

    private void appendAuthorsSetSize() {
        Collection<Author> authors = Authors.getInstance().getAllAuthors();

        append("All authors in all periods: " + authors.size() + "\n");
    }

    private void appendWordSetSize(List<TimePeriod> periods) {
        Set<String> words = new HashSet<>();
        periods.forEach(period -> {
            period.getTweets().forEach(tweet -> {
                tweet.getWords().forEach(word -> {
                    words.add(word.getWord());
                });
            });
        });

        append("All words in all periods: " + words.size() + "\n");
    }

    @Override
    protected String getFilename() {
        return "WordStatistics";
    }

    @Override
    protected String getHeader() {
        return "";
    }
}
