package pl.kania.etd.debug;

import lombok.NoArgsConstructor;
import pl.kania.etd.SavingMemory;

/**
 *  Prints data to console to indicate progress.
 */
@NoArgsConstructor
public class ProgressLogger implements SavingMemory {
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
        saveMemory();
    }

    public void done() {
        System.out.println("Done.");
        saveMemory();
    }

    public void log(String s) {
        System.out.print(s);
    }

    @Override
    public void saveMemory() {
        counter = null;
    }
}
