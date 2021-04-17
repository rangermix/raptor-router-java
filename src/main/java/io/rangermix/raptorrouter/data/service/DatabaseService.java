package io.rangermix.raptorrouter.data.service;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.rangermix.raptorrouter.data.model.*;
import io.rangermix.raptorrouter.data.model.enums.BoardingInstruction;
import io.rangermix.raptorrouter.data.model.enums.LocationType;
import io.rangermix.raptorrouter.data.model.enums.Possibility;
import io.rangermix.raptorrouter.data.model.enums.RouteType;
import io.rangermix.raptorrouter.data.repository.*;
import io.rangermix.raptorrouter.routing.model.Coordinate;
import io.rangermix.raptorrouter.routing.model.DataPackage;
import io.rangermix.raptorrouter.util.StopWatch;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jetbrains.annotations.NotNull;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@org.springframework.stereotype.Service
@Setter(onMethod = @__({@Autowired}))
@Slf4j
public class DatabaseService {
    private AgencyRepository agencyRepository;
    private RouteRepository routeRepository;
    private RoutePatternRepository routePatternRepository;
    private ServiceRepository serviceRepository;
    private StopRepository stopRepository;
    private StopTimeRepository stopTimeRepository;
    private TransferRepository transferRepository;
    private TripRepository tripRepository;

    @Setter(AccessLevel.NONE)
    private DataPackage dataPackageCache;

    public DataPackage getDataPackage() {
        if (dataPackageCache == null) {
            synchronized (this) {
                if (dataPackageCache == null) {
                    dataPackageCache = createDataPackageFromDatabase();
                }
            }
        }
        return dataPackageCache;
    }

    public void saveDataPackageToDatabase(DataPackage dataPackage) {
        StopWatch stopWatch = new StopWatch(log);
        stopWatch.start("enter saveDataPackageToDatabase");
        var gtfsDao = dataPackage.getStaticGTFSSource();

        // we build the documents bottom-up, i.e. transfers->stops
        // Transfer
        var transferMap = getTransferTransferMap(dataPackage);
        transferRepository.deleteAll();
        transferRepository.saveAll(transferMap.values());
        stopWatch.lap("finished saving Transfer");

        // Stop
        var stopList = getStopList(dataPackage, gtfsDao);
        stopRepository.deleteAll();
        stopRepository.saveAll(stopList);
        stopWatch.lap("finished saving Stop");

        // StopTime
        var tripStopTimeMap = getTripStopTimeMap(dataPackage, gtfsDao);
        stopTimeRepository.deleteAll();
        tripStopTimeMap.values().forEach(stopTimeRepository::saveAll);
        stopWatch.lap("finished saving StopTime");

        // Trip
        var tripList = getTripList(dataPackage, gtfsDao, tripStopTimeMap);
        tripRepository.deleteAll();
        tripRepository.saveAll(tripList);
        stopWatch.lap("finished saving Trip");

        // Route
        var routeList = getRouteList(dataPackage, gtfsDao);
        routeRepository.deleteAll();
        routeRepository.saveAll(routeList);
        stopWatch.lap("finished saving Route");

        // Agency
        var agencyList = getAgencyList(dataPackage, gtfsDao);
        agencyRepository.deleteAll();
        agencyRepository.saveAll(agencyList);
        stopWatch.lap("finished saving Agency");

        // RoutePattern
        var routePatternList = getRoutePatternList(dataPackage);
        routePatternRepository.deleteAll();
        routePatternRepository.saveAll(routePatternList);
        stopWatch.lap("finished saving RoutePattern");

        // Service
        var serviceList = getServiceList(dataPackage);
        serviceRepository.deleteAll();
        serviceRepository.saveAll(serviceList);
        stopWatch.stop("finished saving Service");
    }

    private List<Stop> getStopList(DataPackage dataPackage, org.onebusaway.gtfs.services.GtfsMutableDao gtfsDao) {
        return dataPackage.getStopMap().values().parallelStream().map(stop -> {
            var agencyId = stop.agency.getId();
            var gtfsStop = gtfsDao.getStopForId(new AgencyAndId(agencyId, stop.id));
            return Stop.builder()
                    .id(stop.id)
                    .agencyId(agencyId)
                    .name(gtfsStop.getName())
                    .code(gtfsStop.getCode())
                    .desc(gtfsStop.getDesc())
                    .zoneId(gtfsStop.getZoneId())
                    .url(gtfsStop.getUrl())
                    .locationType(LocationType.valueOf(gtfsStop.getLocationType()))
                    .parentStationId(gtfsStop.getParentStation())
                    .wheelchairBoarding(BoardingInstruction.valueOf(gtfsStop.getWheelchairBoarding()))
                    .direction(gtfsStop.getDirection())
                    .timezone(gtfsStop.getTimezone())
                    .vehicleType(gtfsStop.getVehicleType())
                    .platformCode(gtfsStop.getPlatformCode())
                    .point(new GeoJsonPoint(gtfsStop.getLon(), gtfsStop.getLat()))
                    .transferIds(stop.transfers.stream()
                            .map(io.rangermix.raptorrouter.routing.model.Transfer::getId)
                            .collect(Collectors.toList()))
                    .routePatternIds(stop.routePatterns.stream()
                            .map(routePattern -> routePattern.id)
                            .collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());
    }

    @NotNull
    private List<Route> getRouteList(DataPackage dataPackage, org.onebusaway.gtfs.services.GtfsMutableDao gtfsDao) {
        return dataPackage.getMetaRouteMap().values().parallelStream().map(route -> {
            var gtfsRoute = gtfsDao.getRouteForId(new AgencyAndId(route.agency.getId(), route.id));
            return Route.builder()
                    .id(route.id)
                    .agencyId(route.agency.getId())
                    .shortName(gtfsRoute.getShortName())
                    .longName(gtfsRoute.getLongName())
                    .type(RouteType.valueOf(gtfsRoute.getType()))
                    .desc(gtfsRoute.getDesc())
                    .url(gtfsRoute.getUrl())
                    .color(gtfsRoute.getColor())
                    .textColor(gtfsRoute.getTextColor())
                    .bikesAllowed(Possibility.valueOf(gtfsRoute.getBikesAllowed()))
                    .sortOrder(gtfsRoute.getSortOrder())
                    .routePatternIds(route.routePatterns.stream()
                            .map(routePattern -> routePattern.id)
                            .collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());
    }

    private Map<io.rangermix.raptorrouter.routing.model.Trip, List<StopTime>> getTripStopTimeMap(DataPackage dataPackage, org.onebusaway.gtfs.services.GtfsMutableDao gtfsDao) {
        var gtfsTripStopTimeMap = gtfsDao.getAllStopTimes()
                .parallelStream()
                .collect(Collectors.groupingBy(stopTime -> stopTime.getTrip().getId()));
        return dataPackage.getTripMap()
                .values()
                .parallelStream()
                .collect(Collectors.toMap(Function.identity(), trip -> trip.stopTimes.stream().map(stopTime -> {
                    var agencyAndId = new AgencyAndId(trip.route.agency.getId(), trip.id);
                    var gtfsStopTimes = gtfsTripStopTimeMap.get(agencyAndId)
                            .stream()
                            .collect(Collectors.toMap(org.onebusaway.gtfs.model.StopTime::getStopSequence,
                                    Function.identity()));
                    var gtfsStopTime = gtfsStopTimes.get(stopTime.stopSequence);
                    return StopTime.builder()
                            .id(stopTime.id)
                            .tripId(stopTime.trip.id)
                            .arrivalTime(stopTime.arrivalTime)
                            .departureTime(stopTime.departureTime)
                            .stopId(stopTime.stop.id)
                            .stopSequence(stopTime.stopSequence)
                            .stopHeadsign(gtfsStopTime.getStopHeadsign())
                            .pickupType(BoardingInstruction.valueOf(gtfsStopTime.getPickupType()))
                            .dropOffType(BoardingInstruction.valueOf(gtfsStopTime.getDropOffType()))
                            .shapeDistTraveled(gtfsStopTime.getShapeDistTraveled())
                            //                            .farePeriodId(Accuracy.valueOf(gtfsStopTime.getFarePeriodId()))
                            .build();
                }).collect(Collectors.toList())));
    }

    @NotNull
    private List<Trip> getTripList(DataPackage dataPackage, org.onebusaway.gtfs.services.GtfsMutableDao gtfsDao, Map<io.rangermix.raptorrouter.routing.model.Trip, List<StopTime>> tripStopTimeMap) {
        return dataPackage.getTripMap().values().parallelStream().map(trip -> {
            var agencyAndId = new AgencyAndId(trip.route.agency.getId(), trip.id);
            var gtfsTrip = gtfsDao.getTripForId(agencyAndId);
            return Trip.builder()
                    .id(trip.id)
                    .routeId(trip.route.id)
                    .serviceId(trip.service.id)
                    .tripHeadsign(gtfsTrip.getTripHeadsign())
                    .tripShortName(gtfsTrip.getTripShortName())
                    .directionId(gtfsTrip.getDirectionId())
                    .blockId(gtfsTrip.getBlockId())
                    .shapeId(gtfsTrip.getShapeId().getId())
                    .wheelchairAccessible(Possibility.valueOf(gtfsTrip.getWheelchairAccessible()))
                    .bikesAllowed(Possibility.valueOf(gtfsTrip.getBikesAllowed()))
                    .stopTimeIds(tripStopTimeMap.get(trip).stream().map(StopTime::getId).collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());
    }

    @NotNull
    private List<Agency> getAgencyList(DataPackage dataPackage, org.onebusaway.gtfs.services.GtfsMutableDao gtfsDao) {
        return dataPackage.getAgencyMap().values().stream().map(agency -> {
            var gtfsAgency = gtfsDao.getAgencyForId(agency.getId());
            return new Agency(gtfsAgency.getId(),
                    gtfsAgency.getName(),
                    gtfsAgency.getUrl(),
                    gtfsAgency.getTimezone(),
                    gtfsAgency.getLang(),
                    gtfsAgency.getPhone(),
                    gtfsAgency.getFareUrl(),
                    gtfsAgency.getEmail());
        }).collect(Collectors.toList());
    }

    @NotNull
    private List<RoutePattern> getRoutePatternList(DataPackage dataPackage) {
        return dataPackage.getMetaRouteMap()
                .values()
                .parallelStream()
                .flatMap(route -> route.routePatterns.parallelStream())
                .map(routePattern -> RoutePattern.builder()
                        .id(routePattern.id)
                        .routeId(routePattern.route.id)
                        .stopIds(routePattern.stops.parallelStream().map(stop -> stop.id).collect(Collectors.toList()))
                        .tripIds(routePattern.trips.parallelStream().map(trip -> trip.id).collect(Collectors.toList()))
                        .sampleTripId(routePattern.sampleTrip.id)
                        .build())
                .collect(Collectors.toList());
    }

    @NotNull
    private List<Service> getServiceList(DataPackage dataPackage) {
        return dataPackage.getServiceMap()
                .values()
                .parallelStream()
                .map(service -> Service.builder()
                        .id(service.id)
                        .dayMask(service.dayMask.toByteArray())
                        .zoneId(service.startDate.getZone())
                        .startDate(service.startDate.toInstant())
                        .endDate(service.endDate.toInstant())
                        .numDays(service.numDays)
                        .build())
                .collect(Collectors.toList());
    }

    @NotNull
    private Map<io.rangermix.raptorrouter.routing.model.Transfer, Transfer> getTransferTransferMap(DataPackage dataPackage) {
        return dataPackage.getStopMap()
                .values()
                .parallelStream()
                .flatMap(stop -> stop.transfers.stream())
                .distinct()
                .collect(Collectors.toMap(Function.identity(),
                        transfer -> Transfer.builder()
                                .id(transfer.id)
                                .stopAId(transfer.a)
                                .stopBId(transfer.b)
                                .distance(transfer.distance)
                                .time(transfer.time)
                                .build()));
    }

    private DataPackage createDataPackageFromDatabase() {
        StopWatch stopWatch = new StopWatch(log);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongo = MongoClients.create(MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).build());
        var gtfs = mongo.getDatabase("gtfs_sydney");



        var agencyMap = agencyRepository.findAll()
                .parallelStream()
                .map(agency -> new io.rangermix.raptorrouter.routing.model.Agency(agency.getId(),
                        agency.getName(),
                        agency.getUrl(),
                        TimeZone.getTimeZone(agency.getTimeZone())))
                .collect(Collectors.toMap(io.rangermix.raptorrouter.routing.model.Agency::getId, Function.identity()));
        stopWatch.lap("finished getting Agency");

        var transferMap = transferRepository.findAll()
                .parallelStream()
                .collect(Collectors.toMap(Transfer::getId,
                        transfer -> new io.rangermix.raptorrouter.routing.model.Transfer(transfer.getId(),
                                transfer.getStopAId(),
                                transfer.getStopBId(),
                                transfer.getDistance(),
                                transfer.getTime())));
        stopWatch.lap("finished getting Transfer");

        var allStops = stopRepository.findAll();
        var stopMap = allStops.parallelStream()
                .map(stop -> new io.rangermix.raptorrouter.routing.model.Stop(agencyMap.get(stop.getAgencyId()),
                        stop.getId(),
                        new Coordinate(stop.getPoint().getY(), stop.getPoint().getX()),
                        stop.getLocationType()))
                .collect(Collectors.toMap(io.rangermix.raptorrouter.routing.model.Stop::getId, Function.identity()));
        stopWatch.lap("finished getting Stop");

        var serviceMap = serviceRepository.findAll()
                .parallelStream()
                .map(service -> new io.rangermix.raptorrouter.routing.model.Service(service.getId(),
                        BitSet.valueOf(service.getDayMask()),
                        service.getStartDate().atZone(service.getZoneId()),
                        service.getEndDate().atZone(service.getZoneId()),
                        service.getNumDays()))
                .collect(Collectors.toMap(io.rangermix.raptorrouter.routing.model.Service::getId, Function.identity()));
        stopWatch.lap("finished getting Service");

        var allRoutes = routeRepository.findAll();
        var routeMap = allRoutes.parallelStream()
                .map(route -> new io.rangermix.raptorrouter.routing.model.Route(route.getId(),
                        agencyMap.get(route.getAgencyId()),
                        route.getType()))
                .collect(Collectors.toMap(io.rangermix.raptorrouter.routing.model.Route::getId, Function.identity()));
        stopWatch.lap("finished getting Route");

        var allTrips = tripRepository.findAll();
        var tripMap = allTrips.parallelStream()
                .map(trip -> new io.rangermix.raptorrouter.routing.model.Trip(routeMap.get(trip.getRouteId()),
                        serviceMap.get(trip.getServiceId()),
                        trip.getId()))
                .collect(Collectors.toMap(io.rangermix.raptorrouter.routing.model.Trip::getId, Function.identity()));
        stopWatch.lap("finished getting Trip");

        var list = gtfs.getCollection("stopTime", StopTime.class)
                .find()
                .map(stopTime -> new io.rangermix.raptorrouter.routing.model.StopTime(stopTime.getId(),
                        tripMap.get(stopTime.getTripId()),
                        stopTime.getArrivalTime(),
                        stopTime.getDepartureTime(),
                        stopMap.get(stopTime.getStopId()),
                        stopTime.getStopSequence()))
                .into(new ArrayList<>());
        stopWatch.lap("finished getting StopTime native");

        var stopTimeMap = stopTimeRepository.findAll()
                .parallelStream()
                .collect(Collectors.toMap(StopTime::getId,
                        stopTime -> new io.rangermix.raptorrouter.routing.model.StopTime(stopTime.getId(),
                                tripMap.get(stopTime.getTripId()),
                                stopTime.getArrivalTime(),
                                stopTime.getDepartureTime(),
                                stopMap.get(stopTime.getStopId()),
                                stopTime.getStopSequence())));
        stopWatch.lap("finished getting StopTime spring");

        allTrips.parallelStream()
                .forEach(trip -> tripMap.get(trip.getId()).stopTimes.addAll(trip.getStopTimeIds()
                        .stream()
                        .map(stopTimeMap::get)
                        .collect(Collectors.toList())));
        stopWatch.lap("finished inserting StopTime");

        var routePatternMap = routePatternRepository.findAll()
                .parallelStream()
                .map(routePattern -> new io.rangermix.raptorrouter.routing.model.RoutePattern(routePattern.getId(),
                        routeMap.get(routePattern.getRouteId()),
                        routePattern.getStopIds().stream().map(stopMap::get).collect(Collectors.toList()),
                        routePattern.getTripIds().stream().map(tripMap::get).collect(Collectors.toList()),
                        tripMap.get(routePattern.getSampleTripId())))
                .collect(Collectors.toMap(io.rangermix.raptorrouter.routing.model.RoutePattern::getId,
                        Function.identity()));

        allRoutes.parallelStream()
                .forEach(route -> routeMap.get(route.getId()).routePatterns.addAll(route.getRoutePatternIds()
                        .stream()
                        .map(routePatternMap::get)
                        .collect(Collectors.toList())));
        allStops.parallelStream().forEach(stop -> {
            var routingStop = stopMap.get(stop.getId());
            routingStop.transfers.addAll(stop.getTransferIds()
                    .stream()
                    .map(transferMap::get)
                    .collect(Collectors.toList()));
            routingStop.routePatterns.addAll(stop.getRoutePatternIds()
                    .stream()
                    .map(routePatternMap::get)
                    .collect(Collectors.toList()));
            routingStop.parentStation = stopMap.get(stop.getParentStationId());
        });
        stopWatch.stop("finished getting RoutePattern");

        return new DataPackage(this, agencyMap, stopMap, routeMap, serviceMap, tripMap);
    }

}
