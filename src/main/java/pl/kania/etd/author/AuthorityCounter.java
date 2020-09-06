package pl.kania.etd.author;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.kania.etd.content.AugmentedNormalizedTermFrequencyCounter;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorityCounter {

    public static double countBasedOnANTF(Author author, Collection<Author> authors) {
        Map<Author, Integer> followersPerAuthor = authors.stream()
                .collect(Collectors.toMap(Function.identity(), Author::getFollowers));
        return AugmentedNormalizedTermFrequencyCounter.count(author, followersPerAuthor);
    }

    public static double countBasedOnFollowers(Author author, Collection<Author> authors) {
        int maxFollowersCount = getMaxFollowersCount(authors);
        return 1. * author.getFollowers() / maxFollowersCount;
    }

    private static int getMaxFollowersCount(Collection<Author> authors) {
        return authors.stream()
                .map(Author::getFollowers)
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalStateException("Error finding author with max followers"));
    }
}
