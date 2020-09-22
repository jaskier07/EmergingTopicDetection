package pl.kania.etd.content.preproc;

import pl.kania.etd.content.Tweet;

public class TweetContentPreprocessor {

    public String performPreprocessing(String content) {
        content = MentionRemover.removeMentions(content);
        content = ShortcutReplacer.replace(content);
        content = LinkRemover.remove(content);
        return content;
    }
}
