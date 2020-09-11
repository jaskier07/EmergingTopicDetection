package pl.kania.etd.periods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import pl.kania.etd.SavingMemory;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.content.Word;
import pl.kania.etd.graph.EdgeValue;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(of = "index")
public class TimePeriod implements SavingMemory {

    private final int index;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Set<Tweet> tweets = new HashSet<>();
    private final Map<String, WordStatistics> wordStatistics = new HashMap<>();
    private final Map<Cooccurrence, Integer> cooccurrences = new HashMap<>();
    @Setter
    private SimpleDirectedWeightedGraph<String, EdgeValue> correlationGraph;
    @Setter
    private double thresholdEnergy;
    @Setter
    private double dropInPeriod;
    @Setter
    private boolean periodToFree;

    public TimePeriod(int index, LocalDateTime startTime, LocalDateTime endTime) {
        this.index = index;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "#" + index + getTimeRange();
    }

    public String getTimeRange() {
        return " [" + (startTime == null ? "?" : startTime.toString()) + ", " + (endTime == null ? "?" : endTime.toString()) + "]";
    }

    @Override
    public void saveMemory() {
        tweets.forEach(Tweet::saveMemory);
    }

    public void dropRareWords() {
        Set<String> wordsToDrop = wordStatistics.entrySet().stream()
                .filter(e -> e.getValue().getTweets() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        tweets.forEach(t -> t.getWords().removeIf(w -> wordsToDrop.contains(w.getWord())));
        wordStatistics.entrySet().removeIf(w -> wordsToDrop.contains(w.getKey()));
    }

    public void addTweet(Tweet tweet) {
        tweets.add(tweet);

        final Word[] words = tweet.getWords().toArray(new Word[0]);
        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j < words.length; j++) {
                cooccurrences.merge(new Cooccurrence(words[i].getWord(), words[j].getWord()), 1, (w1, w2) -> w1 + 1);
            }

            WordStatistics wordTimePeriod = new WordStatistics(words[i].getWord());
            wordStatistics.merge(words[i].getWord(), wordTimePeriod, (w1, w2) -> {
                w1.incrementTweets();
                return w1;
            });
        }
    }

    public Set<String> getEmergingWords() {
        return wordStatistics.values().stream()
                .filter(WordStatistics::isEmerging)
                .map(WordStatistics::getWord)
                .collect(Collectors.toSet());
    }

    public Set<Word> getWords() {
        Set<Word> words = new HashSet<>();
        tweets.forEach(tweet -> words.addAll(tweet.getWords()));
        return words;
    }

    public void freeCorrelation() {
        wordStatistics.values().forEach(WordStatistics::clearCorrelationVector);
        correlationGraph = null;
    }

    public void freePeriod() {
        tweets.clear();
        wordStatistics.clear();
        cooccurrences.clear();
        correlationGraph = null;
        periodToFree = true;
    }
}
