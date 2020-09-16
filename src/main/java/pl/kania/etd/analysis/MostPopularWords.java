package pl.kania.etd.analysis;

import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.io.SimpleWritable;
import pl.kania.etd.periods.TimePeriod;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Requires setting time periods for tweets.
 */
public class MostPopularWords extends SimpleWritable {

    public MostPopularWords(FileOutputProvider fop, List<TimePeriod> periods) {
        super(fop);

        Map<String, Integer> wordUsage = new HashMap<>();
        periods.forEach(period -> {
            period.getTweets().forEach(tweet -> {
                tweet.getWords().forEach(word -> {
                    wordUsage.merge(word.getWord(), 1, (val1, val2) -> val1 + 1);
                });
            });
        });

        wordUsage.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> append(e.getKey() + "," + e.getValue() + "\n"));
    }

    @Override
    protected String getFilename() {
        return "MostPopularWords";
    }

    @Override
    protected String getHeader() {
        return "word,tweets\n";
    }
}
