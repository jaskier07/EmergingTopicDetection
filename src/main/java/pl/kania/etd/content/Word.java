package pl.kania.etd.content;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = "id")
public class Word implements Comparable<Word> {
    private static Integer currentId = 0;

    private final int id = currentId++;
    private final String word;
    private final double weight;
    @Setter
    private String authorUserName;

    public Word(String word, double weight) {
        this.weight = weight;
        this.word = word;
    }

    @Override
    public String toString() {
        return word;
    }

    @Override
    public int compareTo(Word other) {
        if (other == null || other.getWord() == null) {
            return -1;
        } else if (word == null) {
            return 1;
        }
        return getWord().compareTo(other.getWord());
    }
}
