package pl.kania.etd.graph.drop;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(of = {"firstElementIndex"})
public class Drop<T extends HasValue<T>> {

    // drop is between previous and next elements
    int dropIndex;
    int firstElementIndex;
    int secondElementIndex;
    double value;
    T previousElement;
    T nextElement;

    public Drop(int dropIndex, double value, T previousElement, T nextElement) {
        this.dropIndex = dropIndex;
        this.firstElementIndex = dropIndex ;
        this.secondElementIndex = dropIndex + 1;
        this.value = value;
        this.previousElement = previousElement;
        this.nextElement = nextElement;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
