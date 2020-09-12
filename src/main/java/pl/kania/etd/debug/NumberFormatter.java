package pl.kania.etd.debug;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberFormatter {

    public static <T extends Number> String formatPercentage(T value, T max, int precision, boolean parentheses) {
        String str = !parentheses ? "" : " (";
        str += String.format("%." + precision + "f", 100. * value.doubleValue() / max.doubleValue());
        str += !parentheses ? "" : "%) ";
        return str;
    }

    public static <T extends Number> String formatPercentage(T value, T max, int precision) {
        return formatPercentage(value, max, precision, true);
    }

    public static <T extends Number> String formatPercentage(T value, T max) {
        return formatPercentage(value, max, 2);
    }

    public static <T extends Number> String format(T value) {
        return format(value, 2);
    }

    public static <T extends Number> String format(T value, int precision) {
        return String.format("%." + precision + "f", value.doubleValue());
    }
}
