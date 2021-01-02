package io.rangermix.routing.model;

import io.rangermix.routing.enums.LocationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class Stop implements Serializable {
    @Serial
    private static final long serialVersionUID = 1212227387820663935L;
    public final Agency agency;
    @Getter
    public final String id;
    public final Coordinate coordinate;
    public final LocationType locationType;
    public Stop parentStation;
    public final List<Transfer> transfers = Collections.synchronizedList(new ArrayList<>());
    public final Set<MetaRoute> metaRoutes = ConcurrentHashMap.newKeySet();
    public final Set<Route> routes = ConcurrentHashMap.newKeySet();

    public boolean behindStopInRoute(Stop other, Route route) {
        for (var curr : route.stops) {
            if (curr == other)
                return false;
            if (curr == this)
                return true;
        }
        throw new IllegalArgumentException(String.format("The route %s doesn't have stop %s nor %s",
                route,
                this,
                other));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Stop stop = (Stop) o;
        return agency.equals(stop.agency) && id.equals(stop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agency, id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Stop.class.getSimpleName() + "[", "]").add("id='" + id + "'")
                .add("coordinate=" + coordinate)
                .add("locationType=" + locationType)
                .toString();
    }

}
