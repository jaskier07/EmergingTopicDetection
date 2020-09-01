package pl.kania.etd.periods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.content.Word;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@EqualsAndHashCode(of = "id")
public class TimePeriod {

    private final String id;
    private final int number;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Set<Tweet> tweets = new HashSet<>();
    private final Set<Word> words = new HashSet<>();
    private final Map<String, WordStatistics> wordStatistics = new HashMap<>();

    public TimePeriod(int index, LocalDateTime startTime, LocalDateTime endTime) {
        this.number = index;
        this.id = UUID.randomUUID().toString();
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "[" + (startTime == null ? "?" : startTime.toString()) + ", " + (endTime == null ? "?" : endTime.toString()) + "]";
    }

    public void addTweet(Tweet tweet) {
        tweets.add(tweet);
        words.addAll(tweet.getWords());
        tweet.getWords().forEach(word -> {
            WordStatistics wordTimePeriod = new WordStatistics(word.getWord());
            wordStatistics.merge(word.getWord(), wordTimePeriod, (w1, w2) -> {
                w1.incrementTweets();
                return w1;
            });
        });
    }
}
