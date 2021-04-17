package io.rangermix.raptorrouter.routing.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.StringJoiner;

//@Data
@RequiredArgsConstructor
public class Transfer implements Serializable {
    // https://en.wikipedia.org/wiki/Preferred_walking_speed
    public static final double WALKING_SPEED = 1.4;
    @Serial
    private static final long serialVersionUID = -3721063029381200014L;
    @Getter
    public final String id;
    public final String a;
    public final String b;
    public final double distance;
    public final long time;

    Transfer(String id, Stop a, Stop b, double distance) {
        this.id = id;
        this.a = a.id;
        this.b = b.id;
        this.distance = distance;
        this.time = (long) (distance / WALKING_SPEED);
    }

    public String getOtherStopId(Stop stop) {
        if (stop.id.equals(a)) {
            return b;
        } else if (stop.id.equals(b)) {
            return a;
        } else {
            throw new IllegalArgumentException("Stop don't belong to this transfer!");
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Transfer.class.getSimpleName() + "[", "]").add("a='" + a + "'")
                .add("b='" + b + "'")
                .add("distance=" + distance)
                .add("time=" + time)
                .toString();
    }
}
