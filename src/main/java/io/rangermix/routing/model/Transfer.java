package io.rangermix.routing.model;

import java.io.Serial;
import java.io.Serializable;

//@Data
public class Transfer implements Serializable {
    // https://en.wikipedia.org/wiki/Preferred_walking_speed
    public static final double WALKING_SPEED = 1.4;
    @Serial
    private static final long serialVersionUID = -3721063029381200014L;
    final String a;
    final String b;
    final double distance;
    final long time;

    Transfer(Stop a, Stop b, double distance) {
        this.a = a.getId();
        this.b = b.getId();
        this.distance = distance;
        this.time = (long) (distance * 1000 / WALKING_SPEED);
    }
}
