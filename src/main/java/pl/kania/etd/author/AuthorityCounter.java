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

    public static double countBasedOnANTF(int followers, int highestFollowers) {
        return AugmentedNormalizedTermFrequencyCounter.count(followers, highestFollowers);
    }

    public static double countBasedOnFollowers(Author author, int maxFollowersCount) {
        return 1. * author.getFollowers() / maxFollowersCount;
    }
}
