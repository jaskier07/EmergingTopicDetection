package pl.kania.etd.periods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@EqualsAndHashCode(of = "word")
public class WordStatistics {
    private final String word;
    private int tweets = 1;
    @Setter
    private double nutrition;
    @Setter
    private double energy;
    @Setter
    private boolean emerging;

    public WordStatistics(String word) {
        this.word = word;
    }

    public void incrementTweets() {
        tweets += 1;
    }

}
