package pl.kania.etd.content;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.kania.etd.SavingMemory;
import pl.kania.etd.content.preproc.TweetContentPreprocessor;
import pl.kania.etd.author.Author;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "id")
public class Tweet implements SavingMemory {
    private static Integer currentId = 0;

    private final int id = currentId++;
    private final Author author;
    private final LocalDateTime createdAt;
    private final Set<Word> words;

    public Tweet(Author author, String content, LocalDateTime createdAt) {
        this.author = author;
        this.createdAt = createdAt;
        this.words = ContentSplitter.splitIntoWords(new TweetContentPreprocessor().performPreprocessing(content));
        this.words.forEach(w -> w.setAuthorUserName(author.getUsername()));
    }

    @Override
    public String toString() {
        return "Author: " + author + "; " + words.toString();
    }

    @Override
    public void saveMemory() {
        words.clear();
    }
}
