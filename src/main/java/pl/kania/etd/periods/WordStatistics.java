package pl.kania.etd.periods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@EqualsAndHashCode(of = "word")
public class WordStatistics {
    private final String word;
    private final double nutrition;
    private int tweets;
    @Setter
    private double energy;

    public WordStatistics(String word, double nutrition) {
        this.word = word;
        this.nutrition = nutrition;
    }

    public void incrementTweets() {
        tweets += 1;
    }

}
