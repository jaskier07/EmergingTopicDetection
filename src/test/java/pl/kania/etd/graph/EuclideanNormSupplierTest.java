package pl.kania.etd.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EuclideanNormSupplierTest {

    @Test
    void givenVectorCountItsNorm() {
        List<Double> vector = Arrays.asList(5., 5., 3., 2., 1.);
        double norm = EuclideanNormSupplier.get(vector);
        Assertions.assertEquals(8, norm, 0.01);
    }

}