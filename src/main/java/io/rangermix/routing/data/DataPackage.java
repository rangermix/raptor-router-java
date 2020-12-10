package io.rangermix.routing.data;

import io.rangermix.routing.enums.LocationType;
import io.rangermix.routing.enums.RouteType;
import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.model.AgencyAndId;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataPackage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1354778733203723197L;
    private transient final GtfsDaoImpl staticGTFSSource;
    private final Map<String, Agency> agencyMap;
    private final Map<String, Stop> stopMap;
    private final Map<String, Route> routeMap;
    private final Map<String, Service> serviceMap;
    private Map<String, Trip> tripMap;

    public DataPackage(GtfsDaoImpl staticGTFSSource) {
        this.staticGTFSSource = staticGTFSSource;

        agencyMap = loadAgency(staticGTFSSource);
        stopMap = loadStop(staticGTFSSource);
        routeMap = loadRoute(staticGTFSSource);
        serviceMap = loadService(staticGTFSSource);
        tripMap = loadTrip(staticGTFSSource);
        tripMap = loadStopTime(staticGTFSSource, tripMap);
    }

    private Map<String, Agency> loadAgency(GtfsDaoImpl source) {
        return source.getAllAgencies().stream().map(this::convertAgency).collect(Collectors.toMap(Agency::getId, Function.identity()));
    }

    private Map<String, Stop> loadStop(GtfsDaoImpl source) {
        Map<String, Stop> stopMap = source.getAllStops().stream().map(this::convertStop).collect(Collectors.toMap(Stop::getId, Function.identity()));
        stopMap.forEach((id, stop) -> stop.setParentStation(stopMap.get(source.getStopForId(new AgencyAndId(stop.agency.id, stop.id)).getParentStation())));
        return stopMap;
    }

    private Map<String, Route> loadRoute(GtfsDaoImpl source) {
        return source.getAllRoutes().stream().map(this::convertRoute).collect(Collectors.toMap(Route::getId, Function.identity()));
    }

    private Map<String, Service> loadService(GtfsDaoImpl source) {
        return source.getAllCalendars().stream().map(this::convertService).collect(Collectors.toMap(Service::getId, Function.identity()));
    }

    private Map<String, Trip> loadTrip(GtfsDaoImpl source) {
        return source.getAllTrips().stream().map(this::convertTrip).collect(Collectors.toMap(Trip::getId, Function.identity()));
    }

    private Map<String, Trip> loadStopTime(GtfsDaoImpl source, Map<String, Trip> tripMap) {
        source.getAllStopTimes().stream().map(this::convertStopTime).forEach(stopTime -> stopTime.getTrip().getStopTimes().add(stopTime));
        tripMap.values().forEach(trip -> trip.getStopTimes().sort(Comparator.comparingInt(StopTime::getStopSequence)));
        return this.tripMap;
    }

    private Agency convertAgency(org.onebusaway.gtfs.model.Agency source) {
        return new Agency(source.getId(), source.getName(), source.getUrl(), TimeZone.getTimeZone(source.getTimezone()));
    }

    private Stop convertStop(org.onebusaway.gtfs.model.Stop source) {
        return new Stop(
                agencyMap.get(source.getId().getAgencyId()),
                source.getId().getId(),
                new Coordinate(source.getLat(), source.getLon()),
                LocationType.valueOf(source.getLocationType()),
                null);  // fill this later
    }

    private Route convertRoute(org.onebusaway.gtfs.model.Route source) {
        return new Route(source.getId().getId(), agencyMap.get(source.getId().getAgencyId()), RouteType.valueOf(source.getType()));
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
        var agency = agencyMap.get(source.getServiceId().getAgencyId());
        return new Service(
                String.valueOf(source.getServiceId().getId()),
                weekMask,
                source.getStartDate().getAsCalendar(agency.getTimeZone()),
                source.getEndDate().getAsCalendar(agency.getTimeZone()));
    }

    private Trip convertTrip(org.onebusaway.gtfs.model.Trip source) {
        return new Trip(
                routeMap.get(source.getRoute().getId().getId()),
                serviceMap.get(source.getServiceId().getId()),
                source.getId().getId(),
                new ArrayList<>()); // fill this later
    }

    private StopTime convertStopTime(org.onebusaway.gtfs.model.StopTime source) {
        return new StopTime(
                tripMap.get(source.getTrip().getId().getId()),
                source.getArrivalTime(),
                source.getDepartureTime(),
                stopMap.get(source.getStop().getId().getId()),
                source.getStopSequence());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataPackage that = (DataPackage) o;
        return Objects.equals(staticGTFSSource, that.staticGTFSSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staticGTFSSource);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DataPackage.class.getSimpleName() + "[", "]")
                .add("staticGTFSSource=" + staticGTFSSource)
                .add("agencyMap=" + agencyMap)
                .add("stopMap=" + stopMap)
                .add("routeMap=" + routeMap)
                .add("tripMap=" + tripMap)
                .toString();
    }
}
