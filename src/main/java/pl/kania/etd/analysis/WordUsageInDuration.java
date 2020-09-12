package pl.kania.etd.analysis;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.io.Writable;
import pl.kania.etd.periods.Durable;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.TimePeriods;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class WordUsageInDuration extends Writable {

    private final Set<String> words;
    private final List<TimePeriod> periods;
    private final Duration duration;
    private final FileOutputProvider fop;

    public WordUsageInDuration(Set<String> words, List<TimePeriod> periods, Duration duration, FileOutputProvider fop) {
        this.words = words;
        this.periods = periods;
        this.duration = duration;
        this.fop = fop;
    }

    public WordUsageInDuration(String word, List<TimePeriod> periods, Duration duration, FileOutputProvider fop) {
        this(Set.of(word), periods, duration, fop);
    }

    @Override
    protected void writeToFile(FileWriter fw) {
        log.info("WORD USAGE OF " + words + " (multiple periods)\n");
        List<WordPeriod> wordPeriods = generatePeriods(periods, duration);
        countTweetsInPeriods(wordPeriods);
        writeWords(fw, wordPeriods);
    }

    private void countTweetsInPeriods(List<WordPeriod> wordPeriods) {
        periods.forEach(period -> {
            period.getTweets().forEach(tweet -> {
                if (words.stream().anyMatch(tweet::containsWord)) {
                    TimePeriods.getPeriodFromDate(wordPeriods, tweet.getCreatedAt()).incrementTweets();
                }
            });
        });
    }

    private void writeWords(FileWriter fw, List<WordPeriod> wordPeriods) {
        wordPeriods.forEach(p -> {
            try {
                fw.write(p.getStartTime() + ", " + p.getEndTime() + ", " + p.getTweets() + "\n");
            } catch (IOException e) {
                log.error("Error writing to file " + getFilename());
            }
        });
    }

    @Override
    protected FileOutputProvider getFileOutputProvider() {
        return fop;
    }

    @Override
    protected String getFilename() {
        return "WordUsageInDuration_" + String.join("-", words);
    }

    @Override
    protected String getHeader() {
        return "start, end, tweets\n";
    }

    private static List<WordPeriod> generatePeriods(List<TimePeriod> periods, Duration duration) {
        LocalDateTime start = periods.get(0).getStartTime();
        LocalDateTime end = periods.get(periods.size() - 1).getEndTime();

        List<WordPeriod> wordPeriods = new ArrayList<>();
        while (start.isBefore(end)) {
            LocalDateTime periodEnd = start.plusSeconds(duration.toSeconds());
            wordPeriods.add(new WordPeriod(start, periodEnd));
            start = periodEnd;
        }

        return wordPeriods;
    }

    @Getter
    @RequiredArgsConstructor
    static class WordPeriod implements Durable {
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private int tweets = 0;

        void incrementTweets() {
            tweets++;
        }
    }
}
