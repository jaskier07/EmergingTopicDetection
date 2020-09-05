package pl.kania.etd.content.preproc;

public class NumberRemover {

    public static String remove(String content) {
        return content.replaceAll("\\s[0-9]*\\s", " ");
    }
}
