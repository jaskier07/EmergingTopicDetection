package pl.kania.etd.graph;

import lombok.Value;
import pl.kania.etd.graph.drop.HasValue;

import java.util.UUID;

@Value
public class EdgeValue implements HasValue<EdgeValue> {

    String id = UUID.randomUUID().toString();
    double value;

    @Override
    public int compareTo(EdgeValue o) {
        if (o == null) {
            return -1;
        }
        return value - o.getValue() > 0 ? 1 : -1;
    }
}
