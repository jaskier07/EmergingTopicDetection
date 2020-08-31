package pl.kania.etd.content;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WordFrequenciesInTextCounter {

    private static final String DELIMITERS = "!\"$%&\\'()*+,.:;<=>?@[]^_`{|}~ …”‘’“\r\n#";

    public static Map<String, Integer> count(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, DELIMITERS);
        Map<String, Integer> wordFrequencies = new HashMap<>();

        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if (Strings.isNotBlank(token) && !isDelimiter(token.trim())) {
                wordFrequencies.merge(token.toLowerCase(), 1, (word1freq, word2freq) -> word1freq + 1);
            } else {
                log.debug("Dropped token: " + token);
            }
        }

        return wordFrequencies;
    }

    private static boolean isDelimiter(String s) {
        return DELIMITERS.contains(s);
    }

}
