package pl.kania.etd.author;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "username")
public class Author {

    private final String username;
    private final int followers;
    private int tweetsWritten = 1;
    @Setter
    private double authority;

    public Author(String username, int followers) {
        this.username = username;
        this.followers = followers;
    }

    @Override
    public String toString() {
        return username;
    }

    public void incrementTweetsWritten() {
        tweetsWritten++;
    }
}
