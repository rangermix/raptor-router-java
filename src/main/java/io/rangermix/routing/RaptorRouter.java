package io.rangermix.routing;

import io.rangermix.routing.model.DataPackage;
import io.rangermix.routing.model.Route;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RaptorRouter {
    private final DataPackage dataPackage;

    public Itinerary route(Query query) {
        var stopMap = dataPackage.getStopMap()
                .values()
                .stream()
                .collect(Collectors.toMap(io.rangermix.routing.model.Stop::getId, Stop::new));
        var from = stopMap.get(query.from.getId());
        from.marked = true;
        from.bestArrivalTime = query.startTime;
        List<Stop> markedStops = new ArrayList<>();
        markedStops.add(from);

        // enter rounds
        for (int k = 0; k < 10; ++k) {
            // Accumulate routes serving marked stops from previous round
            Map<Route, Stop> Q = new HashMap<>();
            for (var p : markedStops) {
                for (var r : p.stop.routes) {
                    var pp = Q.get(r);
                    if (pp == null || pp.stop.behindStopInRoute(p.stop, r))
                        Q.put(r, p);
                }
                p.marked = false;
                markedStops.remove(p);
            }

            // Traverse each route
            Q.forEach((r, p) -> {
                var t = p.itinerary;

            });

            // Look at foot-paths
            for (var p : markedStops) {
                for (var trans : p.stop.transfers) {

                }
            }

            // Stopping criterion
            if (markedStops.isEmpty())
                break;
        }

        return null;
    }
}
