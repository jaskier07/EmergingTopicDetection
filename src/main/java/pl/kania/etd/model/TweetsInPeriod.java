package pl.kania.etd.model;

import lombok.Value;

import java.time.Period;
import java.util.Set;

@Value
public class TweetsInPeriod {
    Set<Tweet> tweets;
    Period period;
}
