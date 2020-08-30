package pl.kania.etd.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class Word implements Comparable<Word> {

    private final String id = UUID.randomUUID().toString();
    private final String word;
    private final double weight;

    public Word(String word, double weight) {
        this.weight = weight;
        this.word = word;
    }

    public boolean equalsWord(Word other) {
        return word.equals(other.getWord());
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
