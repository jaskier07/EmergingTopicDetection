package pl.kania.etd.author;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.SavingMemory;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Authors implements SavingMemory {

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
        authors.put(username, new Author(username, followers));
        return getAuthor(username);
    }

    public Author getAuthor(String username) {
        return authors.get(username);
    }

    public Collection<Author> getAllAuthors() {
        return authors.values();
    }

    @Override
    public void saveMemory() {
        authors.clear();
    }

    public void printMostImportantAuthors() {
        log.info("MOST IMPORTANT AUTHORS:");
        log.info(authors.values().stream()
                .sorted(Comparator.comparing(Author::getAuthority).reversed())
                .limit(10)
                .map(a -> a.getUsername() + ": " + a.getAuthority() + ", " + a.getFollowers())
                .collect(Collectors.joining("\n")));
    }
}
