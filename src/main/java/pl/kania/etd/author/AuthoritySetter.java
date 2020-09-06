package pl.kania.etd.author;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthoritySetter {

    public static void setForAllAuthors() {
        log.info("Setting authors' authority");

        Authors authors = Authors.getInstance();

        authors.getAllAuthors().forEach(author -> {
            double value = AuthorityCounter.countBasedOnANTF(author, authors.getAllAuthors());
            author.setAuthority(value);
        });

        log.info("Done.");
    }
}
