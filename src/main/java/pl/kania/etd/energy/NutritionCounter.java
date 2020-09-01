package pl.kania.etd.energy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.kania.etd.author.Author;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.content.Word;
import pl.kania.etd.periods.TimePeriod;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NutritionCounter {

    /**
     * NUTRITION = for each tweet containing word in a time period sum (word weight in a tweet * tweet's user authority)
     */
    public static void countNutritionInPeriod(TimePeriod period) {
        Map<String, List<Word>> wordOccurrences = getWordOccurrences(period.getWords());
        wordOccurrences.forEach((key, value) -> {
            double wordNutrition = value.stream()
                    .map(w -> {
                        double wordWeight = w.getWeight();
                        double authority = w.getTweet().getAuthor().getAuthority();
                        return wordWeight * authority;
                    }).reduce(Double::sum)
                    .orElseThrow(() -> new IllegalStateException("Error counting nutrition"));
            period.getWordStatistics().get(key).setNutrition(wordNutrition);
        });
    }

    private static Map<String, List<Word>> getWordOccurrences(Set<Word> words) {
        return words.stream()
                .collect(Collectors.groupingBy(Word::getWord));
    }
}
