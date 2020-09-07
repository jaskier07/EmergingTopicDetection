package pl.kania.etd.periods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.io.Serializable;

@Getter
@EqualsAndHashCode(of = {"word1", "word2"})
public class Cooccurrence implements Serializable {
    private final String word1;
    private final String word2;

    public Cooccurrence(String word1, String word2) {
        if (word1.compareTo(word2) > 0) {
            this.word1 = word1;
            this.word2 = word2;
        } else {
            this.word1 = word2;
            this.word2 = word1;
        }
    }
}
