package pl.kania.etd.author;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthoritySetter {

    public static void setForAllAuthors() {
        Authors authors = Authors.getInstance();

        int maxFollowersCount = getMaxFollowersCount(authors);
        authors.getAllAuthors().forEach(author -> {
            author.setAuthority(1. * author.getFollowers() / maxFollowersCount);
        });
    }

    private static int getMaxFollowersCount(Authors authors) {
        return authors.getAllAuthors().stream()
                .map(Author::getFollowers)
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalStateException("Error finding author with max followers"));
    }
}
