package pl.kania.etd.author;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.SavingMemory;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Authors implements SavingMemory, Serializable {

    private final Map<String, Author> authors = new HashMap<>();

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
