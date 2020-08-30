package pl.kania.etd.periods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.kania.etd.model.Tweet;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimePeriodInTweetsSetter {

    public static void setTimePeriod(Collection<Tweet> tweets) {
        TimePeriods periods = TimePeriods.getInstance();
        tweets.forEach(tweet -> tweet.setTimePeriod(periods.getPeriodFromDate(tweet.getCreatedAt())));
    }

}
