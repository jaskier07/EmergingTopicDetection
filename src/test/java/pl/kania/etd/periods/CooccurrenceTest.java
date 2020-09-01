package pl.kania.etd.periods;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CooccurrenceTest {

    @Test
    void givenTwoWordsInDifferentOrderReturnTheSameObject() {
        String word1 = "dog";
        String word2 = "cat";

        Assertions.assertEquals(new Cooccurrence(word1, word2), new Cooccurrence(word2, word1));
    }
}