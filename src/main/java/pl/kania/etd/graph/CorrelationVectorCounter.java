package pl.kania.etd.graph;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.debug.Counter;
import pl.kania.etd.debug.ProgressLogger;
import pl.kania.etd.periods.Cooccurrence;
import pl.kania.etd.periods.TimePeriod;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CorrelationVectorCounter {

    public static void countCorrelationAndFillWords(TimePeriod period) {
        String[] words = period.getWordStatistics().keySet().toArray(new String[0]);
        long allTweets = getAllTweets(period);
        ProgressLogger pl = new ProgressLogger("Counting correlation vector");

        for (int i = 0; i < words.length; i++) {
            String word1 = words[i];
            long tweetsContainingWord1 = getTweetsContainingWord(word1, period);

            for (int j = i + 1; j < words.length; j++) {
                String word2 = words[j];
                long tweetsContainingWord2 = getTweetsContainingWord(word2, period);
                long tweetsContainingBothWords = getTweetsContainingBothWords(word1, word2, period);

                Optional<Double> result = countCorrelation(Parameters.builder()
                        .allTweets(allTweets)
                        .tweetsContainingBothWords(tweetsContainingBothWords)
                        .tweetsContainingWord1(tweetsContainingWord1)
                        .tweetsContainingWord2(tweetsContainingWord2)
                        .build());

                double correlation = result.orElse(0.);
                if (result.isPresent()) {
                    putRecordToCorrelationVector(period, word1, word2, correlation);
                    putRecordToCorrelationVector(period, word2, word1, correlation);
                }
            }
            pl.log(300);
        }

        pl.done("period #" + period.getIndex());
    }

    private static Double putRecordToCorrelationVector(TimePeriod period, String word1, String word2, double correlation) {
        return period.getWordStatistics().get(word1).getCorrelationVector().put(word2, correlation);
    }

    public static Optional<Double> countCorrelation(Parameters p) {
        if (!p.isDataValid()) {
            return Optional.empty();
        }

        double nominatorExpression1 = 1. * p.getTweetsContainingBothWords() / (p.getTweetsContainingWord1() - p.getTweetsContainingBothWords());
        double denominatorExpression1 = (p.getTweetsContainingWord2() - p.getTweetsContainingBothWords()) /
                (1. * (p.getAllTweets() - p.getTweetsContainingWord2() - p.getTweetsContainingWord1() + p.getTweetsContainingBothWords()));
        double expression1 = Math.log10(nominatorExpression1 / denominatorExpression1);

        double expression2 = p.getTweetsContainingBothWords() * 1. / p.getTweetsContainingWord1();
        double expression3 = (p.getTweetsContainingWord2() * 1. - p.getTweetsContainingBothWords()) / (p.getAllTweets() - p.getTweetsContainingWord1());

        double result = expression1 * (expression2 - expression3);

        return Double.isFinite(result) ? Optional.of(result) : Optional.empty();
    }

    private static long getTweetsContainingBothWords(String word1, String word2, TimePeriod period) {
        Integer value = period.getCooccurrences().get(new Cooccurrence(word1, word2));
        return value == null ? 0 : value;
    }

    private static long getTweetsContainingWord(String word, TimePeriod period) {
        return period.getWordStatistics().get(word).getTweets();
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

        boolean isDataValid() {
            return tweetsContainingBothWords != 0;
        }
    }
}
