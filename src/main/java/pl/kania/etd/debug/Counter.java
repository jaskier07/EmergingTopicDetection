package pl.kania.etd.debug;

import lombok.Getter;

public class Counter {
    @Getter
    private int value;

    public void increment() {
        value++;
    }
}
