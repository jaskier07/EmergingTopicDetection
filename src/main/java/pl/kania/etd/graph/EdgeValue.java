package pl.kania.etd.graph;

import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.kania.etd.graph.drop.HasValue;

import java.util.UUID;

@Value
@EqualsAndHashCode(of = "id")
public class EdgeValue implements HasValue<EdgeValue> {

    private static Integer currentId = 0;

    int id = currentId++;
    double value;

    @Override
    public int compareTo(EdgeValue o) {
        if (o == null) {
            return -1;
        } else if (value == o.value) {
            return 0;
        }
        return value - o.getValue() > 0 ? 1 : -1;
    }
}
