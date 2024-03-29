package io.rangermix.raptorrouter.routing.model;

import io.rangermix.raptorrouter.data.model.enums.LocationType;
import io.rangermix.raptorrouter.data.model.enums.RouteType;
import io.rangermix.raptorrouter.util.StopWatch;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.services.GtfsMutableDao;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class DataPackage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1354778733203723197L;
    @Getter
    private transient final GtfsMutableDao staticGTFSSource;
    public Object source;
    @Getter
    private final Map<String, Agency> agencyMap;
    @Getter
    private final Map<String, Stop> stopMap;
    @Getter
    private final Map<String, Route> metaRouteMap;
    @Getter
    private final Map<String, Service> serviceMap;
    @Getter
    private final Map<String, Trip> tripMap;

    private final Map<Object, String> errors = new HashMap<>();

    public DataPackage(Object source, Map<String, Agency> agencyMap, Map<String, Stop> stopMap, Map<String, Route> metaRouteMap, Map<String, Service> serviceMap, Map<String, Trip> tripMap) {
        this.staticGTFSSource = null;
        this.source = source;
        this.agencyMap = agencyMap;
        this.stopMap = stopMap;
        this.metaRouteMap = metaRouteMap;
        this.serviceMap = serviceMap;
        this.tripMap = tripMap;
    }

    public DataPackage(GtfsMutableDao staticGTFSSource) {
        this.staticGTFSSource = staticGTFSSource;

        var stopWatch = new StopWatch(log);
        stopWatch.start("enter DataPackage constructor");
        // The order of construction cannot be changed as the later one relies on previous ones.
        agencyMap = loadAgency(staticGTFSSource);
        stopWatch.lap("finished Agency");
        stopMap = loadStop(staticGTFSSource);
        stopWatch.lap("finished Stop");
        metaRouteMap = loadRoute(staticGTFSSource);
        stopWatch.lap("finished Route");
        serviceMap = loadService(staticGTFSSource);
        stopWatch.lap("finished Service");
        tripMap = loadTrip(staticGTFSSource);
        stopWatch.lap("finished Trip");
        stopWatch.lap("exit DataPackage constructor");
    }

    private Map<String, Agency> loadAgency(GtfsMutableDao source) {
        return source.getAllAgencies()
                .parallelStream()
                .map(this::convertAgency)
                .collect(Collectors.toMap(Agency::getId, Function.identity()));
    }

    private Map<String, Stop> loadStop(GtfsMutableDao source) {
        Map<String, Stop> stopMap = source.getAllStops()
                .parallelStream()
                .map(this::convertStop)
                .collect(Collectors.toMap(Stop::getId, Function.identity()));
        stopMap.forEach((id, stop) -> stop.parentStation = stopMap.get(source.getStopForId(new AgencyAndId(stop.agency.id,
                stop.id)).getParentStation()));
        StopWatch stopWatch = new StopWatch(log);
        stopWatch.start("start creating transfers");
        stopMap.values()
                .parallelStream()
                .forEach(a -> stopMap.values().parallelStream().forEach(b -> createTransfer(a, b)));
        stopWatch.lap("finished creating transfers");

        return stopMap;
    }

    private Map<String, Route> loadRoute(GtfsMutableDao source) {
        return source.getAllRoutes()
                .parallelStream()
                .map(this::convertRoute)
                .collect(Collectors.toMap(Route::getId, Function.identity()));
    }

    private Map<String, Service> loadService(GtfsMutableDao source) {
        return source.getAllCalendars()
                .parallelStream()
                .map(this::convertService)
                .collect(Collectors.toMap(Service::getId, Function.identity()));
    }

    private Map<String, Trip> loadTrip(GtfsMutableDao source) {
        var tripMap = source.getAllTrips()
                .parallelStream()
                .map(this::convertTrip)
                .collect(Collectors.toMap(Trip::getId, Function.identity()));
        tripMap = loadStopTime(source, tripMap);
        errors.putAll(checkEmptyRoutes(metaRouteMap));

        metaRouteMap.values().parallelStream().forEach(route -> {
            route.trips.parallelStream()
                    .collect(Collectors.groupingBy(trip -> trip.stopTimes.stream()
                            .map(StopTime::getStop)
                            .collect(Collectors.toList())))
                    .forEach((stops, trips) -> route.routePatterns.add(new RoutePattern(route, stops, trips)));
            route.routePatterns.forEach(stopPattern -> stopPattern.stops.forEach(stop -> {
                stop.routePatterns.add(stopPattern);
            }));
        });

        return tripMap;
    }

    private Map<String, Trip> loadStopTime(GtfsMutableDao source, Map<String, Trip> tripMap) {
        source.getAllStopTimes()
                .parallelStream()
                .map(stopTimeConverter(tripMap))
                .forEach(stopTime -> stopTime.trip.stopTimes.add(stopTime));
        tripMap.values()
                .parallelStream()
                .forEach(trip -> trip.stopTimes.sort(Comparator.comparingInt(stopTime -> stopTime.stopSequence)));
        errors.putAll(checkEmptyTrips(tripMap));
        return tripMap;
    }

    private Agency convertAgency(org.onebusaway.gtfs.model.Agency source) {
        return new Agency(source.getId(),
                source.getName(),
                source.getUrl(),
                TimeZone.getTimeZone(source.getTimezone()));
    }

    private Stop convertStop(org.onebusaway.gtfs.model.Stop source) {
        return new Stop(agencyMap.get(source.getId().getAgencyId()),
                source.getId().getId(),
                new Coordinate(source.getLat(), source.getLon()),
                LocationType.valueOf(source.getLocationType()));  // fill this later
    }

    private Route convertRoute(org.onebusaway.gtfs.model.Route source) {
        return new Route(source.getId().getId(),
                agencyMap.get(source.getId().getAgencyId()),
                RouteType.valueOf(source.getType()));
    }

    private Service convertService(org.onebusaway.gtfs.model.ServiceCalendar source) {
        BitSet weekMask = new BitSet(7);
        weekMask.set(0, source.getMonday() == 1);
        weekMask.set(1, source.getTuesday() == 1);
        weekMask.set(2, source.getWednesday() == 1);
        weekMask.set(3, source.getThursday() == 1);
        weekMask.set(4, source.getFriday() == 1);
        weekMask.set(5, source.getSaturday() == 1);
        weekMask.set(6, source.getSunday() == 1);
        var zoneId = agencyMap.get(source.getServiceId().getAgencyId()).timeZone.toZoneId();
        var startDate = source.getStartDate();
        var endDate = source.getEndDate();
        return new Service(source.getServiceId().getId(),
                weekMask,
                ZonedDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDay(), 0, 0, 0, 0, zoneId),
                ZonedDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDay(), 0, 0, 0, 0, zoneId));
    }

    private Trip convertTrip(org.onebusaway.gtfs.model.Trip source) {
        var route = metaRouteMap.get(source.getRoute().getId().getId());
        var trip = new Trip(route, serviceMap.get(source.getServiceId().getId()), source.getId().getId());
        route.trips.add(trip);
        return trip; // fill this later
    }

    @NotNull
    private Function<org.onebusaway.gtfs.model.StopTime, StopTime> stopTimeConverter(Map<String, Trip> tripMap) {
        return source -> new StopTime(tripMap.get(source.getTrip().getId().getId()),
                source.getArrivalTime(),
                source.getDepartureTime(),
                stopMap.get(source.getStop().getId().getId()),
                source.getStopSequence());
    }

    private void createTransfer(Stop a, Stop b) {
        if (a.hashCode() > b.hashCode() || a.equals(b))
            return;

        double distance = a.coordinate.distanceTo(b.coordinate);
        if (distance > 1000)
            return;

        Transfer transfer = new Transfer(ObjectId.get().toHexString(), a, b, distance);
        a.transfers.add(transfer);
        b.transfers.add(transfer);
    }

    public Map<Object, String> checkEmptyRoutes(Map<String, Route> routeMap) {
        Map<Object, String> errors = new HashMap<>();
        var emptyRoutes = routeMap.values()
                .parallelStream()
                .filter(route -> route.trips.isEmpty())
                .collect(Collectors.toList());
        emptyRoutes.forEach(route -> errors.put(route, "Route without any trips! Removed."));
        emptyRoutes.parallelStream().forEach(route -> routeMap.remove(route.id));
        return errors;
    }

    private Map<Object, String> checkEmptyTrips(Map<String, Trip> tripMap) {
        Map<Object, String> errors = new HashMap<>();
        var emptyTrips = tripMap.values()
                .parallelStream()
                .filter(trip -> trip.stopTimes.isEmpty())
                .collect(Collectors.toList());
        emptyTrips.forEach(route -> errors.put(route, "Trip without any stopTimes! Removed."));
        emptyTrips.parallelStream().forEach(trip -> {
            trip.route.trips.remove(trip);
            tripMap.remove(trip.id);
        });
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DataPackage that = (DataPackage) o;
        return Objects.equals(staticGTFSSource, that.staticGTFSSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staticGTFSSource);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ",
                DataPackage.class.getSimpleName() + "[",
                "]").add("staticGTFSSource=" + staticGTFSSource)
                .add("agencyMap=" + agencyMap.size())
                .add("stopMap=" + stopMap.size())
                .add("routeMap=" + metaRouteMap.size())
                .add("tripMap=" + tripMap.size())
                .toString();
    }
}
