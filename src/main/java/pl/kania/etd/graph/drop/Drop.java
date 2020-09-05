package pl.kania.etd.graph.drop;

import lombok.Value;

@Value
public class Drop {
    int lastIndex;
    double value;

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
