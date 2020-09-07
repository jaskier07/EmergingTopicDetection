package pl.kania.etd.debug;

import lombok.Getter;

public class Counter {
    @Getter
    private int value;

    public void increment() {
        value++;
    }

    public int getValueAndIncrement() {
        int val = value;
        increment();
        return val;
    }

    public String getValueAsStringAndIncrement() {
        return Integer.toString(getValueAndIncrement());
    }
}
