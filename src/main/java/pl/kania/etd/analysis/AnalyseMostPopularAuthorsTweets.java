package pl.kania.etd.analysis;

import pl.kania.etd.author.Author;
import pl.kania.etd.author.Authors;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.io.SimpleWritable;
import pl.kania.etd.io.Writable;
import pl.kania.etd.periods.TimePeriod;

import java.util.*;

public class AnalyseMostPopularAuthorsTweets extends SimpleWritable {

    public AnalyseMostPopularAuthorsTweets(FileOutputProvider fop, List<TimePeriod> periods, Set<String> authorsToAnalyze) {
        super(fop);

        Map<TimePeriod, Tweet> authorsTweet = new HashMap<>();

        periods.forEach(period -> {
            period.getTweets().stream()
                    .filter(t -> authorsToAnalyze.stream().anyMatch(a -> t.getAuthor().getUsername().equals(a)))
                    .forEach(t -> authorsTweet.put(period, t));
        });

        authorsTweet.forEach((p, t) -> {
            append(t.getAuthor().getUsername() + "," + t.getAuthor().getAuthority() + "," + p.getIndex() + "," + t.getContent() + "\n");
        });
        write();
    }

    @Override
    protected String getFilename() {
        return "MostPopularAuthorsTweets";
    }

    @Override
    protected String getHeader() {
        return "username,reputation,period,tweet\n";
    }
}
