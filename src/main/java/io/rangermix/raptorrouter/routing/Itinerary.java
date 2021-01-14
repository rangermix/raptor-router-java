package io.rangermix.raptorrouter.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Itinerary {
    long startTime;
    long endTime;
    List<Leg> legs = new ArrayList<>();

    Itinerary(Stop from, Stop to) {
        startTime = from.bestArrivalTime;
        endTime = to.bestArrivalTime;
        for (Stop curr = to; curr != from; curr = curr.comeFromStop) {
            legs.add(new Leg(curr));
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Itinerary.class.getSimpleName() + "[", "]").add("startTime=" + startTime)
                .add("endTime=" + endTime)
                .add("legs=" + legs)
                .toString();
    }
}
