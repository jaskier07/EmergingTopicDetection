package pl.kania.etd.periods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@EqualsAndHashCode(of = "word")
public class WordStatistics {
    private final String word;
    @Setter
    private double nutrition;
    private int tweets = 1;
    @Setter
    private double energy;

    public WordStatistics(String word) {
        this.word = word;
    }

    public void incrementTweets() {
        tweets += 1;
    }

}
