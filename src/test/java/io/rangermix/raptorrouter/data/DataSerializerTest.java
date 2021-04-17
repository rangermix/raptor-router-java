package io.rangermix.raptorrouter.data;

import io.rangermix.raptorrouter.routing.model.DataPackage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DataSerializerTest {

    @Test
    void testBuildSydney() throws IOException {
        DataSerializer.buildSydney();
    }

    @Test
    void testGTFSRouteAndTrips() throws IOException {
        var dao = DataManager.getSydneyGtfsDao(DataManager.createInmemoryStore());
        var dataPackage = new DataPackage(dao);
        var routesWithTripsWithDifferentStops = dataPackage.getMetaRouteMap()
                .values()
                .stream()
                .filter(route -> route.trips.stream().mapToInt(trip -> trip.stopTimes.size()).distinct().count() > 1)
                .collect(Collectors.toList());
        assertThat(routesWithTripsWithDifferentStops).hasSize(0);
    }
}
