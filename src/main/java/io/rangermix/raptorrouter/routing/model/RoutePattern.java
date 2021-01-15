package io.rangermix.raptorrouter.routing.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RoutePattern implements Serializable {
    @Serial
    private static final long serialVersionUID = 6874317310518777358L;
    public final Route route;
    public final List<Stop> stops;
    public final List<Trip> trips;
    public Trip sampleTrip;

    public RoutePattern(Route route, List<Stop> stops, List<Trip> trips) {
        this.route = route;
        this.stops = stops;
        this.trips = trips;
        trips.sort(Trip.TIME_COMPARATOR);
        sampleTrip = trips.get(0);
    }

    public Iterator<StopTime> stopTimeIterator(Stop stop, long arriveTime) {
        int stopIndex = stops.indexOf(stop);
        Trip bestTrip = null;
        long earliestDep = Long.MAX_VALUE;
        for (Trip trip : trips) {
            var stopTime = trip.stopTimes.get(stopIndex);
            var nextDep = stopTime.nextDepartureTimeFromDate(Instant.ofEpochSecond(arriveTime)
                    .atZone(route.agency.timeZone.toZoneId())
                    .toLocalDate());
            if (nextDep >= arriveTime && nextDep < earliestDep) {
                earliestDep = nextDep;
                bestTrip = trip;
            }
        }
        return bestTrip == null ? Collections.emptyIterator() : bestTrip.stopTimes.listIterator(stopIndex);
    }
}
