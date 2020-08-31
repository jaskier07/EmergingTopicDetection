package pl.kania.etd.periods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.content.Word;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class TimePeriod {

    private final String id;
    private final int number;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Set<Tweet> tweets = new HashSet<>();
    private final Set<Word> words = new HashSet<>();

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
    }
}
