package pl.kania.etd.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import pl.kania.etd.CriticalException;
import pl.kania.etd.author.Author;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.author.Authors;
import pl.kania.etd.debug.ProgressLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class CsvReader {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE LLL dd HH:mm:ss xxxx yyyy");

    private final Authors authors;
    private LocalDateTime firstTweetTime;
    private LocalDateTime lastTweetTime;

    public CsvReader() {
        this.authors = Authors.getInstance();
    }

    public CsvReaderResult readFile(String path) {
        return readFile(path, 0, Integer.MAX_VALUE);
    }

    public CsvReaderResult readFile(String path, int startFromTweet, int endOnTweet) {
        firstTweetTime = LocalDateTime.MAX;
        lastTweetTime = LocalDateTime.MIN;
        Set<Tweet> tweets = new HashSet<>();
        int currentTweet = 0;

        try (InputStream is = getClass().getResourceAsStream(path);
             InputStreamReader input = new InputStreamReader(is);
             CSVParser csvParser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(input)
        ) {
            ProgressLogger pl = new ProgressLogger("Reading file from " + startFromTweet + " to " + endOnTweet + " tweets");
            for (CSVRecord record : csvParser) {
                try {
                    boolean tweetNotToSave = currentTweet++ < startFromTweet;
                    Tweet tweet = getTweetFromRecord(record, tweetNotToSave);

                    if (tweetNotToSave) {
                        continue;
                    }

                    tweets.add(tweet);
                    pl.log();

                    if (tweets.size() == endOnTweet) {
                        log.info("Stopped reading file on " + endOnTweet + " tweet.");
                        break;
                    }
                } catch (DateTimeParseException dtpe) {
                    log.warn("Error parsing date");
                } catch (Exception e) {
                    log.warn("Problem with reading record. Record number: " + record.getRecordNumber(), e);
                }
            }
            pl.done("Reading file.");

            return new CsvReaderResult(tweets, firstTweetTime, lastTweetTime);
        } catch (IOException e) {
            log.error("Cannot find csv containing tweets", e);
            throw new CriticalException(e.getMessage());
        }
    }

    private Tweet getTweetFromRecord(CSVRecord record, boolean tweetNotToSave) {
        String username = record.get("user_screen_name");
        int followers = parseInt(record.get("user_followers_count"), username);
        Author author = authors.getAuthorAndAddIfNotExists(username, followers);

        LocalDateTime date = parseDate(record.get("created_at"), tweetNotToSave);
        String content = record.get("text");
        return new Tweet(author, content, date);
    }

    private int parseInt(String value, String username) {
        if (Strings.isBlank(value)) {
            log.warn("User " + username + " has no user_followers_count value ");
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("Cannot parse int: " + e);
            return 0;
        }
    }

    private LocalDateTime parseDate(String value, boolean tweetNotToSave) {
        if (Strings.isBlank(value)) {
            return null;
        }
        LocalDateTime date = LocalDateTime.from(dtf.parse(value));
        if (!tweetNotToSave) {
            if (date.isBefore(firstTweetTime)) {
                firstTweetTime = date;
            }
            if (date.isAfter(lastTweetTime)) {
                lastTweetTime = date;
            }
        }
        return date;
    }
}
