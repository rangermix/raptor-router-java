package io.rangermix.routing;

import java.util.ArrayList;
import java.util.List;

public class Itinerary {
    long startTime;
    long endTime;
    List<Leg> legs = new ArrayList<>();

    Itinerary(long time) {
        startTime = time;
        endTime = time;
    }
}
