package pl.kania.etd.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kania.etd.CriticalException;
import pl.kania.etd.author.Author;
import pl.kania.etd.model.Tweet;
import pl.kania.etd.author.Authors;
import pl.kania.etd.util.ProgressLogger;

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
        firstTweetTime = LocalDateTime.MAX;
        lastTweetTime = LocalDateTime.MIN;
        Set<Tweet> tweets = new HashSet<>();

        try (InputStream is = getClass().getResourceAsStream(path);
             InputStreamReader input = new InputStreamReader(is)
        ) {
            log.info("File is being read...");
            CSVParser csvParser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(input);
            for (CSVRecord record : csvParser) {
                try {
                    Tweet tweet = getTweetFromRecord(record);
                    tweets.add(tweet);
                    ProgressLogger.log(record.getRecordNumber());
                } catch (Exception e) {
                    log.warn("Problem with reading record. Record number: " + record.getRecordNumber(), e);
                }
            }
            ProgressLogger.done();

            log.info("Finished reading file.");
            return new CsvReaderResult(tweets, firstTweetTime, lastTweetTime);
        } catch (IOException e) {
            log.error("Cannot find csv containing tweets", e);
            throw new CriticalException(e.getMessage());
        }
    }

    private Tweet getTweetFromRecord(CSVRecord record) {
        String username = record.get("user_screen_name");
        int followers = parseInt(record.get("user_followers_count"), username);
        Author author = authors.getAuthorAndAddIfNotExists(username, followers);

        LocalDateTime date = parseDate(record.get("created_at"));
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

    private LocalDateTime parseDate(String value) {
        if (Strings.isBlank(value)) {
            return null;
        }
        LocalDateTime date = LocalDateTime.from(dtf.parse(value));
        if (date.isBefore(firstTweetTime)) {
            firstTweetTime = date;
        }
        if (date.isAfter(lastTweetTime)) {
            lastTweetTime = date;
        }
        return date;
    }
}
