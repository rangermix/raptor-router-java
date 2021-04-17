package io.rangermix.raptorrouter.routing.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@RequiredArgsConstructor
public class Trip implements Serializable {
    @Serial
    private static final long serialVersionUID = -860604145187379705L;
    public final Route route;
    public final Service service;
    @Getter
    public final String id;
    public final List<StopTime> stopTimes = Collections.synchronizedList(new ArrayList<>());

    public static final Comparator<Trip> TIME_COMPARATOR = (a, b) -> {
        int sizeA = a.stopTimes.size();
        int sizeB = b.stopTimes.size();
        int smallerSize = Math.min(sizeA, sizeB);
        for (int i = 0; i < smallerSize; ++i) {
            var c = Long.compare(a.stopTimes.get(i).arrivalTime, b.stopTimes.get(i).arrivalTime);
            if (c != 0)
                return c;
            c = Long.compare(a.stopTimes.get(i).departureTime, b.stopTimes.get(i).departureTime);
            if (c != 0)
                return c;
        }
        return Integer.compare(sizeA, sizeB);
    };

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Trip trip = (Trip) o;
        return route.equals(trip.route) && id.equals(trip.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, id);
    }
}
