package pl.kania.etd.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorrelationVectorCounterTest {

    public static final double DELTA = 0.01;

    @Test
    void test() {
        CorrelationVectorCounter.Parameters parameters = CorrelationVectorCounter.Parameters
                .builder()
                .tweetsContainingWord1(10)
                .tweetsContainingWord2(20)
                .tweetsContainingBothWords(5)
                .allTweets(50)
                .build();

        double correlation = CorrelationVectorCounter.countCorrelation(parameters);
        Assertions.assertEquals(0.027, correlation, DELTA);

        parameters = CorrelationVectorCounter.Parameters
                .builder()
                .tweetsContainingWord1(20)
                .tweetsContainingWord2(10)
                .tweetsContainingBothWords(5)
                .allTweets(50)
                .build();

        correlation = CorrelationVectorCounter.countCorrelation(parameters);
        Assertions.assertEquals(0.027, correlation, DELTA);
    }

}