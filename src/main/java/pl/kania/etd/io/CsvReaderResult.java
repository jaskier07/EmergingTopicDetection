package pl.kania.etd.io;

import lombok.Value;
import pl.kania.etd.model.Tweet;

import java.time.LocalDateTime;
import java.util.Set;

@Value
public class CsvReaderResult {
    Set<Tweet> tweetSet;
    LocalDateTime firstTweetDate;
    LocalDateTime lastTweetDate;
}
