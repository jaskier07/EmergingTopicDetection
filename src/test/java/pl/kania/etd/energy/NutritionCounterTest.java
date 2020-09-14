package pl.kania.etd.energy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kania.etd.author.Author;
import pl.kania.etd.author.AuthoritySetter;
import pl.kania.etd.author.Authors;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.WordStatistics;

import java.time.LocalDateTime;
import java.util.Map;

class NutritionCounterTest {

    private static final double DELTA = 0.01;

    @Test
    void givenTimePeriodWithTweetsCountWordsNutrition() {
        Authors authors = new Authors();
        Author author1 = authors.getAuthorAndAddIfNotExists("jaskier07", 10);
        Author author2 = authors.getAuthorAndAddIfNotExists("oloolo", 5);
        AuthoritySetter.setForAllAuthors(authors, true);

        Tweet tweet1 = new Tweet(author1, "doing nothing", LocalDateTime.now());
        Tweet tweet2 = new Tweet(author1, "nothing nothing", LocalDateTime.now());
        Tweet tweet3 = new Tweet(author2, "doing doing nothing", LocalDateTime.now());

        TimePeriod timePeriod = new TimePeriod(0, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        timePeriod.addTweet(tweet1);
        timePeriod.addTweet(tweet2);
        timePeriod.addTweet(tweet3);

        NutritionCounter.countAndSetNutritionInPeriod(timePeriod);
        Map<String, WordStatistics> wordStatistics = timePeriod.getWordStatistics();

        double auth1 = 0.5 + 0.5 * 1;
        double auth2 = 0.5 + 0.5 * 0.5;
        Assertions.assertEquals(auth1 * (1 + 1) + auth2 * 0.75, wordStatistics.get("nothing").getNutrition(), DELTA);
        Assertions.assertEquals(auth1 * 1 + auth2 * 1, wordStatistics.get("doing").getNutrition(), DELTA);
    }
}