package pl.kania.etd.analysis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.io.Filepath;
import pl.kania.etd.io.Writable;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.WordStatistics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class WordUsageInExistingPeriods extends Writable {

    private final String word;
    private final List<TimePeriod> periods;
    private final FileOutputProvider fop;

    public WordUsageInExistingPeriods(String word, List<TimePeriod> periods, FileOutputProvider fop) {
        this.word = word;
        this.periods = periods;
        this.fop = fop;
    }

    @Override
    protected void writeToFile(FileWriter fw) {
        log.info("WORD USAGE OF \"" + word + "\" (known periods)");

        periods.stream()
                .map(p -> {
                    WordStatistics stats = p.getWordStatistics().get(word);
                    int tweets = stats == null ? 0 : stats.getTweets();
                    return new WordInPeriod(tweets, p.getIndex());
                }).forEachOrdered(w -> writeToFile(fw, w));
    }

    private static void writeToFile(FileWriter fw, WordInPeriod w) {
        try {
            fw.write(w.getPeriodIndex() + ", " + w.getTweets() + "\n");
        } catch (IOException e) {
            log.error("Error writing to file", e);
        }
    }

    @Override
    public FileOutputProvider getFileOutputProvider() {
        return fop;
    }

    @Override
    public String getFilename() {
        return "wordUsageInExistingPeriods_" + word;
    }

    @Override
    protected String getHeader() {
        return "period_index, num_tweets\n";
    }

    @Value
    static class WordInPeriod {
        int tweets;
        int periodIndex;
    }
}
