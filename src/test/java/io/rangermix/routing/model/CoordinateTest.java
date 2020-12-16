package io.rangermix.routing.model;
import org.junit.jupiter.api.Test;

import io.rangermix.routing.model.Coordinate;

import static org.assertj.core.api.Assertions.assertThat;

public class CoordinateTest {

    @Test
    void testDistanceTo() {
        var a = new Coordinate(35, 100);
        var b = new Coordinate(35, 100.1);

        assertThat(a.distanceTo(b)).isBetween(9108d, 9109d);
        assertThat(a.distanceTo(b)).isEqualTo(b.distanceTo(a));
    }
}
