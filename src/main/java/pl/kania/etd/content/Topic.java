package pl.kania.etd.content;

import lombok.Value;
import lombok.extern.jbosslog.JBossLog;
import org.jgrapht.Graph;
import pl.kania.etd.graph.EdgeValue;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.WordStatistics;

import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class Topic {
    private final List<WordStatistics> words = new ArrayList<>();

    public Topic(Collection<String> topicWords, TimePeriod period) {
        Map<String, WordStatistics> statistics = period.getWordStatistics();
        topicWords.forEach(w -> words.add(statistics.get(w)));
        words.sort((w1, w2) -> -Double.compare(w1.getEnergy(), w2.getEnergy()));
    }

    @Override
    public String toString() {
        return words.stream()
                .map(w -> "(" + w.getWord() + ", " + w.getEnergy() + ")")
                .collect(Collectors.joining(", "));
    }
}
