package pl.kania.etd.io;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.content.Word;
import pl.kania.etd.io.serialization.SerializationService;
import pl.kania.etd.io.serialization.SerializationState;
import pl.kania.etd.periods.Cooccurrence;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.WordStatistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

class SerializationServiceTest {

    public static final String FILENAME = "test-serialization.txt";

    @AfterAll
    static void deleteFile() throws IOException {
        Files.delete(Paths.get(FILENAME));
    }

    @Test
    void test() {
        SerializationService serializationService = new SerializationService();

        TimePeriod tp = new TimePeriod(123, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10));

        WordStatistics ws = new WordStatistics("cat");
        ws.setNutrition(10.5);
        ws.setEmerging(true);
        ws.setEnergy(40.1);
        ws.getCorrelationVector().put("dog", 1.5);
        ws.incrementTweets();

        Tweet tweet = new Tweet(null, "dog is eating", LocalDateTime.now());
        tp.getTweets().add(tweet);
        tp.getWords().add(new Word("dog", 25.));
        tp.getWordStatistics().put("cat", ws);
        tp.getCooccurrences().put(new Cooccurrence("dog", "cat"), 5);

        SerializationState state = new SerializationState(Arrays.asList(tp), 3, 5, 0);
        serializationService.serializeCurrentState(state, FILENAME);

        SerializationState loadedState = serializationService.deserializeCurrentState("test-serialization.txt");

        Assertions.assertEquals(state.getChosenPeriodIndex(), loadedState.getChosenPeriodIndex());
        Assertions.assertEquals(state.getMaxClusterSize(), loadedState.getMaxClusterSize());
        Assertions.assertEquals(state.getMinClusterSize(), loadedState.getMinClusterSize());
        Assertions.assertEquals(state.getPeriods().size(), loadedState.getPeriods().size());

        TimePeriod loadedPeriod = loadedState.getPeriods().get(0);
        Assertions.assertEquals(tp.getWordStatistics(), loadedPeriod.getWordStatistics());
        Assertions.assertEquals(tp.getCooccurrences(), loadedPeriod.getCooccurrences());

        Assertions.assertEquals(tp.getId(), loadedPeriod.getId());
        Assertions.assertEquals(tp.getEndTime(), loadedPeriod.getEndTime());
        Assertions.assertEquals(tp.getStartTime(), loadedPeriod.getStartTime());
        Assertions.assertEquals(tp.getWords(), loadedPeriod.getWords());
        Assertions.assertEquals(tp.getTweets(), loadedPeriod.getTweets());
    }

}