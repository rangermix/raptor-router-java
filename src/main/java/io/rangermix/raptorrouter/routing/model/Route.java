package io.rangermix.raptorrouter.routing.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Route implements Serializable {
    @Serial
    private static final long serialVersionUID = 6874317310518777358L;
    public final MetaRoute metaRoute;
    public final List<Stop> stops;
    public final List<Trip> trips;
    public Trip sampleTrip;

    public Route(MetaRoute metaRoute, List<Stop> stops, List<Trip> trips) {
        this.metaRoute = metaRoute;
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
                    .atZone(metaRoute.agency.timeZone.toZoneId())
                    .toLocalDate());
            if (nextDep >= arriveTime && nextDep < earliestDep) {
                earliestDep = nextDep;
                bestTrip = trip;
            }
        }
        return bestTrip == null ? Collections.emptyIterator() : bestTrip.stopTimes.listIterator(stopIndex);
    }
}
