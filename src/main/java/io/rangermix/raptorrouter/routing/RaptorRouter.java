package io.rangermix.raptorrouter.routing;

import io.rangermix.raptorrouter.routing.model.DataPackage;
import io.rangermix.raptorrouter.routing.model.Route;
import io.rangermix.raptorrouter.routing.model.Trip;
import io.rangermix.raptorrouter.util.StopWatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class RaptorRouter {
    private final DataPackage dataPackage;
    private Map<String, Stop> stopMap;
    private Set<Stop> markedStops;
    private final StopWatch stopWatch = new StopWatch(log);
    private Stop from;
    private Stop to;

    public Itinerary route(Query query) {
        stopWatch.start("entered route with {}", query);
        initRouter(query);

        // enter rounds
        for (int k = 0; k < 10; ++k) {
            stopWatch.lap("start round {}", k);

            // Accumulate routes serving marked stops from previous round
            Map<Route, Stop> Q = findMarkedStopsWithRoute();

            // Traverse each route
            Q.forEach(this::expandThroughRoute);

            expandThroughFootPaths(stopMap, markedStops);

            // Stopping criterion
            if (markedStops.isEmpty())
                break;
        }

        return to.bestArrivalTime == Long.MAX_VALUE ? null : new Itinerary(from, to);
    }

    @NotNull
    private Map<Route, Stop> findMarkedStopsWithRoute() {
        Map<Route, Stop> Q = new HashMap<>();
        for (var p : markedStops) {
            for (var r : p.stop.routes) {
                var pp = Q.get(r);
                if (pp == null || pp.stop.behindStopInRoute(p.stop, r))
                    Q.put(r, p);
            }
            p.marked = false;
        }
        markedStops.clear();
        return Q;
    }

    private void expandThroughRoute(Route r, Stop p) {
        Trip t = null;    // the current trip
        LocalDate date = Instant.ofEpochSecond(p.bestArrivalTime)
                .atZone(p.stop.agency.getTimeZone().toZoneId())
                .toLocalDate();

        r.stopTimeIterator(p.stop, p.bestArrivalTime).forEachRemaining(stopTime -> {
            // Can the label be improved in this round? Includes local and target pruning
            var pi = stopMap.get(stopTime.stop.id);
            var arrPi = stopTime.nextArrivalTimeFromDate(date);
            if (arrPi < Math.min(to.bestArrivalTime, pi.bestArrivalTime)) {
                pi.addTransfer(markedStops, p, stopTime, arrPi);
            }

            // Can we catch an earlier trip at p_i?
            if (pi.lastArrivalTime <= stopTime.nextDepartureTimeFromDate(date)) {
                // TODO: recalculate r.stopTimeIterator
            }

        });
    }

    private void initRouter(Query query) {
        stopMap = dataPackage.getStopMap()
                .values()
                .stream()
                .collect(Collectors.toMap(io.rangermix.raptorrouter.routing.model.Stop::getId, Stop::new));
        stopWatch.lap("finished creating stopMap");
        from = stopMap.get(query.from.id);
        to = stopMap.get(query.to.id);
        from.marked = true;
        from.bestArrivalTime = query.startTime;
        markedStops = new HashSet<>();
        markedStops.add(from);
        // initial foot-path expansion
        expandThroughFootPaths(stopMap, markedStops);
        stopWatch.lap("finished initial foot-path expansion");
    }

    private void expandThroughFootPaths(Map<String, Stop> stopMap, Set<Stop> markedStops) {
        // Look at foot-paths
        Set<Stop> newMarkedStops = new HashSet<>();
        for (var p : markedStops) {
            for (var transfer : p.stop.transfers) {
                var pp = stopMap.get(transfer.getOtherStopId(p.stop));
                var arrivalTimeFromP = p.bestArrivalTime + transfer.time;
                if (arrivalTimeFromP < pp.bestArrivalTime)
                    pp.addTransfer(newMarkedStops, p, transfer, arrivalTimeFromP);
            }
        }
        markedStops.addAll(newMarkedStops);
    }

}
