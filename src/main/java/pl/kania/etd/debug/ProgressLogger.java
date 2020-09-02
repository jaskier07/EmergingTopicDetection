package pl.kania.etd.debug;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProgressLogger {
    private Counter counter = new Counter();

    public ProgressLogger(String text) {
        System.out.println(text);
    }

    public void log() {
        log(5000);
    }

    public void log(int threshold) {
        counter.increment();
        if (counter.getValue() % threshold == 0) {
            System.out.print('.');
        }
    }

    public void done(String text) {
        System.out.println("Done: " + text);
    }

    public void done() {
        System.out.println("Done.");
    }

    public void log(String s) {
        System.out.print(s);
    }

}
