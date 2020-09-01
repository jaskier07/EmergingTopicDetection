package pl.kania.etd.graph;

import lombok.*;
import lombok.extern.jbosslog.JBossLog;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.debug.Counter;
import pl.kania.etd.debug.ProgressLogger;
import pl.kania.etd.periods.TimePeriod;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CorrelationVectorCounter {

    public static void countCorrelationAndFillWords(TimePeriod period) {
        String[] words = period.getWordStatistics().keySet().toArray(new String[0]);
        long allTweets = getAllTweets(period);
        Counter counter = new Counter();

        for (int i = 0; i < words.length; i++) {
            String word1 = words[i];
            long tweetsContainingWord1 = getTweetsContainingWord(word1, period);

            for (int j = i + 1; j < words.length; j++) {
                String word2 = words[j];
                long tweetsContainingWord2 = getTweetsContainingWord(word2, period);
                long tweetsContainingBothWords = getTweetsContainingBothWords(word1, word2, period);

                double correlation = countCorrelation(Parameters.builder()
                        .allTweets(allTweets)
                        .tweetsContainingBothWords(tweetsContainingBothWords)
                        .tweetsContainingWord1(tweetsContainingWord1)
                        .tweetsContainingWord1(tweetsContainingWord2)
                        .build());

                putRecordToCorrelationVector(period, word1, word2, correlation);
                putRecordToCorrelationVector(period, word2, word1, correlation);
                ProgressLogger.log(counter);
            }
        }

        ProgressLogger.done("period #" + period.getId());
    }

    private static Double putRecordToCorrelationVector(TimePeriod period, String word1, String word2, double correlation) {
        return period.getWordStatistics().get(word1).getCorrelationVector().put(word2, correlation);
    }

    public static double countCorrelation(Parameters p) {
        double nominatorExpression1 = 1. * p.getTweetsContainingBothWords() / (p.getTweetsContainingWord1() - p.getTweetsContainingBothWords());
        double denominatorExpression1 = (p.getTweetsContainingWord2() - p.getTweetsContainingBothWords()) /
                (1. * (p.getAllTweets() - p.getTweetsContainingWord2() - p.getTweetsContainingWord1() + p.getTweetsContainingBothWords()));
        double expression1 = Math.log10(nominatorExpression1 / denominatorExpression1);

        double expression2 = p.getTweetsContainingBothWords() * 1. / p.getTweetsContainingWord1();
        double expression3 = (p.getTweetsContainingWord2() * 1. - p.getTweetsContainingBothWords()) / (p.getAllTweets() - p.getTweetsContainingWord1());

        return expression1 * (expression2 - expression3);
    }

    private static long getTweetsContainingBothWords(String word1, String word2, TimePeriod period) {
        return period.getTweets().stream()
                .filter(t -> tweetContainsWord(word1, t) && tweetContainsWord(word2, t))
                .count();
    }

    private static long getTweetsContainingWord(String word, TimePeriod period) {
        return period.getTweets().stream()
                .filter(t -> tweetContainsWord(word, t))
                .count();
    }

    private static boolean tweetContainsWord(String word, Tweet tweet) {
        return tweet.getWords().stream().anyMatch(w -> w.getWord().equals(word));
    }

    private static int getAllTweets(TimePeriod period) {
        return period.getTweets().size();
    }

    @Value
    @Builder
    static class Parameters {
        /**
         * R_k
         */
        long tweetsContainingWord1;
        /**
         * n_z
         */
        long tweetsContainingWord2;
        /**
         * r_k,z
         */
        long tweetsContainingBothWords;
        /**
         * N
         */
        long allTweets;
    }
}
