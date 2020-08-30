package pl.kania.etd.author;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Authors {

    private static final Map<String, Author> authors = new HashMap<>();
    private static Authors instance;

    public static Authors getInstance() {
        if (instance == null) {
            instance = new Authors();
        }
        return instance;
    }

    public Author getAuthorAndAddIfNotExists(String username, int followers) {
        if (authors.containsKey(username)) {
            Author author = getAuthor(username);
            author.incrementTweetsWritten();
            return author;
        }
        return authors.put(username, new Author(username, followers));
    }

    public Author getAuthor(String username) {
        return authors.get(username);
    }

}
