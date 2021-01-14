package io.rangermix.raptorrouter.routing.model;

import io.rangermix.raptorrouter.routing.enums.RouteType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class MetaRoute implements Serializable {
    @Serial
    private static final long serialVersionUID = 5278546465986360990L;
    @Getter
    public final String id;
    public final Agency agency;
    public final RouteType type;
    public final List<Trip> trips = Collections.synchronizedList(new ArrayList<>());
    public final List<Route> routes = Collections.synchronizedList(new ArrayList<>());

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MetaRoute metaRoute = (MetaRoute) o;
        return Objects.equals(id, metaRoute.id) && Objects.equals(agency, metaRoute.agency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agency);
    }
}