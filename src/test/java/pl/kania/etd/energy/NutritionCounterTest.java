package pl.kania.etd.energy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kania.etd.author.Author;
import pl.kania.etd.author.AuthoritySetter;
import pl.kania.etd.author.Authors;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.content.Word;
import pl.kania.etd.periods.TimePeriod;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NutritionCounterTest {

    private static final double DELTA = 0.01;

    @Test
    void givenTimePeriodWithTweetsCountWordsNutrition() {
        Author author1 = Authors.getInstance().getAuthorAndAddIfNotExists("jaskier07", 10);
        Author author2 = Authors.getInstance().getAuthorAndAddIfNotExists("oloolo", 5);
        AuthoritySetter.setForAllAuthors();

        Tweet tweet1 = new Tweet(author1, "doing nothing", LocalDateTime.now());
        Tweet tweet2 = new Tweet(author1, "nothing nothing", LocalDateTime.now());
        Tweet tweet3 = new Tweet(author2, "doing doing nothing", LocalDateTime.now());

        TimePeriod timePeriod = new TimePeriod(0, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        timePeriod.addTweet(tweet1);
        timePeriod.addTweet(tweet2);
        timePeriod.addTweet(tweet3);

        NutritionCounter.countNutritionInPeriod(timePeriod);
        Set<Word> words = timePeriod.getWords();

        Assertions.assertEquals(2.375, findWord("nothing", words), DELTA);
        Assertions.assertEquals(1.5, findWord("doing", words), DELTA);
    }

    private double findWord(String word, Set<Word> words) {
        return words.stream()
                .filter(w -> w.getWord().equals(word))
                .findFirst()
                .map(Word::getNutrition)
                .orElseThrow(() -> new NoSuchElementException("Error finding word " + word));
    }

}