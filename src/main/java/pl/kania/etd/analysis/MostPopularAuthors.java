package pl.kania.etd.analysis;

import pl.kania.etd.author.Author;
import pl.kania.etd.author.AuthoritySetter;
import pl.kania.etd.author.Authors;
import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.io.SimpleWritable;
import pl.kania.etd.periods.TimePeriod;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Requires setting time periods for tweets.
 */
public class MostPopularAuthors extends SimpleWritable {

    public MostPopularAuthors(FileOutputProvider fop) {
        super(fop);

        Authors authors = Authors.getInstance();
        List<Author> mostPopularAuthors = getMostPopularAuthors(authors);

        mostPopularAuthors.forEach(author -> {
            append(author.getUsername() + "," + author.getFollowers() + "," + author.getTweetsWritten() + "\n");
        });
    }

    private List<Author> getMostPopularAuthors(Authors authors) {
        return authors.getAllAuthors().stream()
                .sorted(Comparator.comparing(Author::getFollowers).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    protected String getFilename() {
        return "MostPopularAuthors";
    }

    @Override
    protected String getHeader() {
        return "username,followers,tweets\n";
    }
}
