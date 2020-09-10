package pl.kania.etd.periods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.debug.ProgressLogger;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimePeriodInTweetsSetter {

    public static void setTimePeriod(List<TimePeriod> periods, Collection<Tweet> tweets) {
        ProgressLogger pl = new ProgressLogger("Setting time periods for tweets");

        tweets.forEach(tweet -> {
            TimePeriod period = TimePeriods.getPeriodFromDate(periods, tweet.getCreatedAt());
            period.addTweet(tweet);

            pl.log();
        });

        pl.done("Setting timer periods for tweets.");
    }

}
