package pl.kania.etd.author;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class Author {

    private final String id;
    private final String username;
    private final int followers;
    private int tweetsWritten = 1;
    @Setter
    private double authority;

    public Author(String username, int followers) {
        this.username = username;
        this.id = UUID.randomUUID().toString();
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
