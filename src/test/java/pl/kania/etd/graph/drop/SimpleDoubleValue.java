package pl.kania.etd.graph.drop;

import lombok.Value;

@Value
class SimpleDoubleValue implements HasValue<SimpleDoubleValue> {
    double value;

    @Override
    public int compareTo(SimpleDoubleValue o) {
        if (o == null) {
            return -1;
        }
        return Double.compare(value, o.getValue());
    }
}