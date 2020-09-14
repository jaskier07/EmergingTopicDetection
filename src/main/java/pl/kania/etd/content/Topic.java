package pl.kania.etd.content;

import lombok.Value;
import org.jgrapht.Graph;
import pl.kania.etd.debug.NumberFormatter;
import pl.kania.etd.graph.EdgeValue;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.WordStatistics;

import java.util.*;
import java.util.stream.Collectors;

@Value
public class Topic {
    Graph<String, EdgeValue> graph;
    double energy;

    public String toString(TimePeriod period) {
        List<WordStatistics> words = getWordsSortedByEnergy(period);
        return "[E: " + NumberFormatter.format(energy, 4) + "] " + words.stream()
                .map(w -> "(" + w.getWord() + ", " + NumberFormatter.format(w.getEnergy(), 3) + ")")
                .collect(Collectors.joining(", ")) ;
    }

    public List<WordStatistics> getWordsSortedByEnergy(TimePeriod period) {
        List<WordStatistics> words = new ArrayList<>();

        Map<String, WordStatistics> statistics = period.getWordStatistics();
        graph.vertexSet().forEach(w -> words.add(statistics.get(w)));

        words.sort(Comparator.comparingDouble(WordStatistics::getEnergy).reversed());
        return words;
    }
}
