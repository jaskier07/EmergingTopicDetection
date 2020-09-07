package pl.kania.etd.content;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.kania.etd.content.preproc.TweetContentPreprocessor;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.author.Author;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class Tweet implements Serializable {
    private final String id;
    private transient final Author author;
    private final String content;
    private final LocalDateTime createdAt;
    @Setter
    private TimePeriod timePeriod;
    private Set<Word> words;

    public Tweet(Author author, String content, LocalDateTime createdAt) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.content = new TweetContentPreprocessor().performPreprocessing(content);
        this.createdAt = createdAt;
        this.words = ContentSplitter.splitIntoWords(this.content);
        this.words.forEach(w -> w.setTweet(this));
    }

    @Override
    public String toString() {
        return "Author: " + author + "; " + content;
    }
}
