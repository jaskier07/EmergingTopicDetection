package pl.kania.etd.periods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.kania.etd.content.Tweet;
import pl.kania.etd.debug.ProgressLogger;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimePeriodInTweetsSetter {

    public static void setTimePeriod(Collection<Tweet> tweets) {
        ProgressLogger pl = new ProgressLogger("Setting time periods for tweets");

        TimePeriods periods = TimePeriods.getInstance();
        tweets.forEach(tweet -> {
            TimePeriod period = periods.getPeriodFromDate(tweet.getCreatedAt());
            tweet.setTimePeriod(period);
            period.addTweet(tweet);

            pl.log();
        });

        pl.done("Setting timer periods for tweets.");
    }

}
