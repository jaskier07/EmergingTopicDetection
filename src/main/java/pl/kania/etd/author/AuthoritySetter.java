package pl.kania.etd.author;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.content.AugmentedNormalizedTermFrequencyCounter;
import pl.kania.etd.debug.ProgressLogger;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthoritySetter {

    public static void setForAllAuthors(Authors authors, boolean authorityAugmented) {
        setForAllAuthors(authors, authorityAugmented, getMaxFollowersCount(authors.getAllAuthors()));
    }

    public static void setForAllAuthors(Authors authors, boolean authorityAugmented, final int maxFollowersCount) {
        ProgressLogger pl = new ProgressLogger("Setting authors' authority");

        authors.getAllAuthors().forEach(author -> {
            double value;
            if (authorityAugmented) {
                value = AuthorityCounter.countBasedOnANTF(author.getFollowers(), maxFollowersCount);
            } else {
                value = AuthorityCounter.countBasedOnFollowers(author, maxFollowersCount);
            }
            author.setAuthority(value);
            pl.log(2000);
        });

        pl.done("Setting authors' authority");
    }

    private static int getMaxFollowersCount(Collection<Author> authors) {
        return authors.stream()
                .map(Author::getFollowers)
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalStateException("Error finding author with max followers"));
    }
}
