package io.rangermix.raptorrouter.routing.model;

import io.rangermix.raptorrouter.data.model.enums.RouteType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class Route implements Serializable {
    @Serial
    private static final long serialVersionUID = 5278546465986360990L;
    @Getter
    public final String id;
    public final Agency agency;
    public final RouteType type;
    public final List<Trip> trips = Collections.synchronizedList(new ArrayList<>());
    public final List<RoutePattern> routePatterns = Collections.synchronizedList(new ArrayList<>());

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id) && Objects.equals(agency, route.agency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agency);
    }
}
