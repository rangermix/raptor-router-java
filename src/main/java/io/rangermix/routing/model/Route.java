package io.rangermix.routing.model;

import java.io.Serial;
import java.io.Serializable;
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
}
