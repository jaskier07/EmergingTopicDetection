package pl.kania.etd.debug;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PercentageFormatter {

    public static <T extends Number> String format(T value, T max) {
        return String.format("%.2f", " (" + 100. * value.doubleValue() / max.doubleValue()) + "%) ";
    }
}
